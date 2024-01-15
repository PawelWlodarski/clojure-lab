(ns clojure-lab.async.a5-mix-merge
  (:require [
             clojure.core.async :as async :refer [mix chan admix merge put! unmix toggle take! <!!]])
  )




(defn example1-2chan-merge 
  "Simple merge example : 2 input channels 1 output channel"
  []
  
  (let [input1 (chan)
        input2 (chan)
        output (merge [input1 input2]) ] ;;channels in list
 

    (put! input1 "message1 from channel1 to output")
    (put! input2 "message2 from channel2 to output")

    (println (<!! output))
    (println (<!! output))))


(defn example2-2chan-mix 
   "MIX scenario - 2 input 1 output. First input is then removed."
  []
  
  (let [output  (chan)
        input1 (chan)
        input2 (chan)
        mixer (mix output)]
    (admix mixer input1)
    (admix mixer input2)

    (put! input1 "message1 from input1")
    (put! input2 "message2 from input2")

    (println (<!! output))
    (println (<!! output))

    (unmix mixer input1)


    (put! input1 "message11 from input1") ;;we should not see this one
    (put! input2 "message22 from input2")
    
    (future
      (Thread/sleep 1000) ;;we are going to wait for last one because of blocking <!! in main
      (put! input2 "message23 from input2")
      )

    (println (<!! output))
    (println (<!! output ))))

(defn example3-mix-toggle-mute []
"
 :mute - keep taking from the input channel but discard any taken values
:pause - stop taking from the input channel
:solo - listen only to this (and other :soloed channels). Whether or not
 "

  (let [output  (chan)
        input1 (chan)
        input2 (chan)
        mixer (mix output)]
    (admix mixer input1)
    (admix mixer input2)

    (put! input1 "message1")
    (put! input2 "message2")

    (println (<!! output))
    (println (<!! output))

  ;;mute channel 1
    (toggle mixer {input1 {:mute true}})

    (put! input1 "message12") 
    (put! input2 "message22")
    (put! input1 "message13")
    (put! input2 "message23")
    (put! input1 "message14")
    (put! input1 "message15")
    (put! input1 "message16")
    
    (toggle mixer {input1 {:mute false}})  ;;uncomment for the second run

    (println "1: " (<!! output)) 
    (println "2: " (<!! output)) 
    (println "3: " (<!! output))
    
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    (println (take! output println))
    
    ))




