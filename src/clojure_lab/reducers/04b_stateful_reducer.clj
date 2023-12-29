(ns clojure-lab.reducers.04b-stateful-reducer
  (:require [clojure.core.reducers :as r]))

(defn find-max-reduction [next-reducer]
  (let [max (volatile! (Integer/MIN_VALUE))]
    (fn [_ e]
      (when (> e @max) (vreset! max e))
      (println "current max : " @max)
      (next-reducer @max e)
      )
    )
  )

(defn maximum [coll]
  (r/reducer coll find-max-reduction))

(->> 
 (range 10)
 maximum
 (reduce (fn [max _] max) 0)
 )

(defn even2 [e]
  (println "filtering : " e)
  (even? e))

(def maximum-even (comp maximum (r/filter even2)))

(->>
 (range 10)
 maximum-even
 (reduce (fn [max _] max) 0))