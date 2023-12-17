(ns clojure-lab.reducers.02-concept 
  (:require [clojure.core.reducers :as r]
            [clojure.core.protocols]
            ))

(comment
  ;;standard reducing function 
  (let 
   [reducing-function +
    mapping-function inc 
    reduce-mapped-sum (fn [result element]  (reducing-function result (mapping-function element)))]
   
    (reduce reduce-mapped-sum (range 10))
    )
  )


(comment
   ;;reducing function with parameterized mapper
  (defn mapping1 [mapping-f]
    (fn [result element]
      (+ result (mapping-f element))))

  (reduce (mapping1 inc) (range 10)))


(comment
      ;;reducing function with parameterized mapper and 'reduction wrapper'
  (defn mapping [mapping-f]
    (fn [reducing-f]
      (fn [result element]
        (reducing-f result (mapping-f element)))))

  ;; (mapping inc) - mapper applied now we have function receiving reducing-f
  ;; ((mapping inc) +) - reducing function applied - now we have standard reduce function 
  ;; so technically we created a wrapper around reducing function with mapper injected 
  (reduce ((mapping inc) +) (range 10))

  ;;composition with our "reducers" works correct but not intuitive (different order that you expect)
  (def composed (comp (mapping inc) (mapping (partial * 10))))
  (reduce (composed +) (range 10))

  ;;core.reducers composition has different order that normal f composition but its more intuitive
  (def c2 (comp (r/map inc) (r/map (partial * 10))))
  (def c3 (comp (r/map (partial * 10)) (r/map inc)))

  (reduce + (c2 (range 10)))
  (reduce + (c3 (range 10)))

  (defn logging+
    ([] 0)
    ([sum e]
     (println e)
     (+ sum e)))

  ;;show step by step
  (reduce logging+ (c2 (range 10)))
  (reduce (composed logging+)  (range 10))

  )

(defn reducer-with-protocol
  ([coll mapper]
   (reify
     clojure.core.protocols/CollReduce
     (coll-reduce [_ f1 init]
       (clojure.core.protocols/coll-reduce coll (mapper f1) init)))))

(comment 
  (defn mapping [mapping-f]
    (fn [reducing-f]
      (fn [result element]
        (reducing-f result (mapping-f element)))))
  (reduce + 0 (reducer-with-protocol [1 2 3 4] (mapping inc)))
  )