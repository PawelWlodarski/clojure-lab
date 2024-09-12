(ns clojure-lab.async.examples.async-logging
  (:require [clojure.core.async :refer [chan >!! <!!]])
  )

(def logger (chan 24))

(defn printer []
  (future 
    (loop []
      (when-some [v (<!! logger)]
        (println v)
        (recur)))))

(defn log [& args]
  (>!! logger (apply str args)))

(defn run-worker []
  (future
    (dotimes [x 100]
      (log  "..." x " ..."))))

(do 
  (printer)
  (run-worker)
  (run-worker))

