(ns clojure-lab.functions.a4-complement)

( (complement {:a 1 :b 2}) :c)
( (complement {:a 1 :b nil}) :b) ;;present but nill
( (complement {:a 1 :b nil}) :a)


(defn remove [coll pred]
  (filter (complement pred) coll))
  
(remove [1 2 3 4 5] even?) ;;(1 3 5)

(defn index [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll))
  )

(defn pos [e coll]
  (for [[i v] (index coll) :when (= e v)]
    i))

(pos 3 [1 2 3 4 5]) ;;(2)

(map vector [1 2 3] [4 5 6])