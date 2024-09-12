(ns clojure-lab.async.examples.backpressure
  (:require [clojure.core.async :refer [chan >! <!! go-loop]]))


(def c (chan))
;; (def c (chan 24))

(go-loop [i 0]
  (println "putting : " i)
  (>! c i)
  (recur (inc i)))

(<!! c)

