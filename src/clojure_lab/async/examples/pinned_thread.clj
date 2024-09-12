(ns clojure-lab.async.examples.pinned-thread
  (:require [clojure.core.async :refer [go]])
  (:import [java.util.concurrent Executors]))

;;lein run -m clojure-lab.async.examples.pinned-thread/process-numbers
;;lein with-profile dev12  run -m clojure-lab.async.examples.pinned-thread/process-numbers


(defn some-lazy-seq []
  (lazy-seq  
   (iterate 
   (fn [n] 
     (println (str "producing " n " in " (Thread/currentThread)))
     (Thread/sleep 100)
     (inc n)) 0
    ))
  )

;; Function to create a virtual thread and process a lazy sequence
(defn process-numbers []
  (println (str "Starting process-numbers with java version " (System/getProperty "java.version")))
  (let [executor (Executors/newVirtualThreadPerTaskExecutor)
        action (fn [] (doseq [n (take 10 (some-lazy-seq))] 
                        (println (str "Consuming: " n
                                      " on thread :" (Thread/currentThread)))
                        ))
        ]
    (.submit executor ^Runnable action) 
    (Thread/sleep 2000)
    (.shutdown executor)))