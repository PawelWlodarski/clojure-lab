(ns clojure-lab.async.aX-buffers
  (:require [clojure.core.async :as a]))

;; Use `dropping-buffer` to drop newest values when the buffer is full:
(def c1 (a/chan (a/dropping-buffer 2)))

;; Use `sliding-buffer` to drop oldest values when the buffer is full:
(def c2 (a/chan (a/sliding-buffer 2)))


@(future
   (dotimes [x 5]
     (a/>!! c2 x)
     (println "sent : " x))
   (println "done")
   (a/close! c1)
   (a/close! c2))


(future
  (dotimes [_ 2]
    (println "got : " (a/<!! c2))))





