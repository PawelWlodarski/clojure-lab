(ns clojure-lab.reducers.01-composition
  (:require [clojure.core.reducers :as r]))

;;https://clojure.org/news/2012/05/15/anatomy-of-reducer
;;https://clojure.org/news/2012/05/08/reducers



;;reducers composition
(def ^:private increment-even (comp (r/filter even?) (r/map inc)))
(comment
  (increment-even [1 2 2 3 3 4])  ;;folder tpe
  (doall (increment-even [1 2 2 3 3 4])) ;;coll-reduce protocol
  (reduce + (increment-even [1 2 2 3 3 4]))
  (into [] (increment-even [1 2 2 3 3 4])))


;;problems with only with standard reduce
(comment
  ;; error (reduce even?)
  (let
   [bad-reducer (partial reduce even?)
    filtering-reducer (partial reduce (fn [r e] (if (even? e) (conj r e) r)))
    mapping-reducer (partial reduce (fn [r e]  (conj r (inc e))))]
    ;;(bad-reducer [1 2 2 3 4 5])
    (filtering-reducer [] [1 2 2 3 4 5])
    (mapping-reducer [] [1 2 2 3 4 5]))
  )

