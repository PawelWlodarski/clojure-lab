(ns clojure-lab.async.a3-channels
  (:require [clojure.core.async :refer [chan >!! <!!]])
  )
 

;;example 1 creation
(defn exmple1-channel-creation []
  (let [c (chan)] ;;remove () and see fking useless error message
    (future
      (println "writting to channel")
      (>!! c "dupa"))
    (future
      (println "read from channel : " (<!! c)))))


;;example 2 - multiple clients
(defn example2-multiple-clients []
  (let [c (chan)]
    (future
      (dotimes [x 20]
        (println "PRODUCTING : " x)
        (>!! c x)))

    (future
      (dotimes [_ 10]
        (println "CONSUMER 1 :" (<!! c))))
    (future
      (dotimes [_ 10]
        (println "CONSUMER 2 :" (<!! c))))))