(ns clojure-lab.async.a1_1_async_Intro
  (:require [clojure.core.async :as async :refer [go <! >!! <!!]]))


(defn thread-name []
  (.getName (Thread/currentThread)))

(defn print-thread-name [id]
  (println (str "current thread : " id " : " (thread-name))))

(defn example1-basic-go-block 
  "External Go-BLOCK is calling internal GO-BLOCK and take result from its channel.
   m1 - start in main thread 
   t1 - firstgo block run in separate thread
      -- go result is channel
      when calling <! current thread is parked
   t3 - now we are in inner go because outer go is parked
   t2 - finally we return to outer go but in a different  thread
   "
  []
  (letfn [(go-with-sleep [t]
            (go
              (print-thread-name "inner go 1") ;; t3
              (Thread/sleep t)
              (print-thread-name "inner go 2") ;; t3
              69))]
    (print-thread-name "main 1") ;;m1
    (go
      (print-thread-name "outer go 1") ;;t1
      (let [result (go-with-sleep 500)]
        (println  (str "go result is : " (type result)))
        (print-thread-name "outer go 2") ;; t1
        (println (<! result))
        (print-thread-name "outer go 3") ;; t2
        )
      )
    ))


(defn example2-go-block-proper-blocking
  "External Go-BLOCK is calling internal THREAD - 
     which runs on different thread pool -  
     and take result from its channel.
     m1 - starts in main thread
     t1 - thread from core.async pool
     t2 - inner thread runs on a separate unbounded thread pool
          (defonce ^:private ^Executor thread-macro-executor
          (Executors/newCachedThreadPool (conc/counted-thread-factory \" async-thread-macro-%d \" true)))
  
      while go-block is a fixed pool with 8 threads
      https://github.com/clojure/core.async/blob/322adc72852bb79c143ebe5d203d100534fda8f2/src/main/clojure/clojure/core/async/impl/exec/threadpool.clj
      "
  
  
  []
  
  (letfn [(go-with-sleep [t]
            (async/thread
              (print-thread-name "inner go 1") ;;t2
              (Thread/sleep t)
              (print-thread-name "inner go 2")
              69))]
    (print-thread-name "main 1") ;;m1
    (go
      (print-thread-name "outer go 1") ;;t1
      (let [result (go-with-sleep 500)]
        (println  (str "async/thread result is : " (type result)))
        (print-thread-name "outer go 2")
        (println (<! result))
        (print-thread-name "outer go 3")))))

(def c (async/chan)) ;;external channel
(defn example3-manual-channel-creation
    "External Go-BLOCK taking asynchronously value from external channel which
     is populated in separate thread"
  []
  
  (letfn [(future-with-sleep [t]
            (future
              (print-thread-name "inner future 1")'
              (Thread/sleep t)
              (print-thread-name "inner future 2")
              (>!! c 69)
              ;;(c >!! 69)
              ;; try (c >!! 69)
              (print-thread-name "inner future 3")
              ))]
    (print-thread-name "main 1")
    (go
      (print-thread-name "outer go 1") 
      (future-with-sleep 500)
      (print-thread-name "outer go 2")
      (println (str "result from manual channel : " (<! c)))
      (print-thread-name "outer go 3")))
  )

(defn example4-put-take
  "use of take! and put!. When flag on-caller? is set to false
  then function should be executed by async pool (default 8 threads)  
  but in example below I can not observe execution when any of put! take! is set to false
  * here is some explanation : https://github.com/loganpowell/cljs-guides/blob/master/src/guides/core-async-basics.md"
[]
  (let [c (async/chan 10)]
    (print-thread-name "main 1")
    (async/put! c "message1" #(println "first put : " %)) ;;because buffer is 10 put succeed imediatelly
    (async/take! c #(println (str "first take : " % ", in thread : " (thread-name))))
    (print-thread-name "main 2"))


  (print-thread-name "main 3")
  ;;second take
  (async/put! c "message2")
  (async/take! c #(println (str "second take : " % ", in thread : " (thread-name))) false) ;;on-caller? false 

  ;;third take
  (async/put! c "message3")
  (async/take! c #(println (str "third take : " % ", in thread : " (thread-name))))

  ;;fourth take
  (async/put! c "message4" false) ;;on-caller? false
  (async/take! c #(println (str "fourth take : " % ", in thread : " (thread-name))))

  ;;fifth take
  (async/put! c "message5" false) ;;on-caller? false
  (async/take! c #(println (str "fifth take : " % ", in thread : " (thread-name))) false)

  (print-thread-name "main 4")
  (Thread/sleep 2000)
  (async/close! c))

;;put will not succeed imediately
(defn example4b-randez-vous-channel []
  (let [c (async/chan)]
    (async/put! c "message1" #(println "first put : " %))
    (async/take! c #(println (str "first take : " % ", in thread : " (thread-name))))))

;;example with go-loop - not supported by common-try-catch
(defn example5-producer-consumer []
"
 A channel is created in 'main thread scope'
 Then future thread is putting there values every 500 ms and 
 another async thread in go block is consuming values as soon as they appear;
 "

  (letfn [(producer [n]
            (let [ex5-channel (async/chan)]
              (future
                (dotimes [current n]
                  (println "producing : " current)
                  (>!! ex5-channel current)
                  (Thread/sleep 500))
                (async/close! ex5-channel)) ;;easy to forget
              ex5-channel))
          (consumer [output]
            (println output)
            (async/go-loop [result (<! output)]
              (println "consuming : " result)
              (if result 
                (recur (<! output))
                "processing completed")))]

    (let
     [output (producer 10)
      final-channel (consumer output)] 
      (println "final result : " (<!! final-channel))
  )))