(ns clojure-lab.async.a3-2-alt
  (:require [clojure.core.async :refer [>!! <!! thread chan alts!!]]))

(defn example1-simple-alt []
  (let [c1 (chan 1)
        c2 (chan 1)]
    (>!! c1 10)
    (thread
      (let [[v c] (alts!! [c1 c2])]
        (println   "value:" v)
        (println "chan 1? : " (= c c1))
        (println "chan 2? : " (= c c2))))))