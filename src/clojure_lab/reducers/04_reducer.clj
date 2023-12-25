(ns clojure-lab.reducers.04-reducer
  (:require [clojure.core.reducers :as r]))

;;reducer - functions which modifies reduce process
(def range-to-100-by-3 (range 0 100 3))
;;standard reduce
(reduce conj [] range-to-100-by-3)
;;map reducer
(reduce conj (r/map inc range-to-100-by-3))
;;mapcat reducer
(reduce conj (r/mapcat (fn [e] [(dec e) (inc e)]) range-to-100-by-3))
;;take reducer
(reduce conj (r/take 5 range-to-100-by-3))
;;take-while reducer
(reduce conj (r/take-while #(< % 20) range-to-100-by-3))
;;composed reducers
(reduce conj ( (comp (r/take 5) (r/map inc) (r/map #(* % 2))) range-to-100-by-3))




;;CUSTOM REDUCER
;; (defn reducer [coll xf] ...
;; .. coll-reduce coll (xf f1) init) )

;; xf is a function A -> (B,C) -> D, usually 
;; A - delegate reducing function (acc,e) -> result
;; (B,C) -> wrapper reducing function
(defn xf [red-fn]
  ;;leave divisible by 5
  (fn [acc el]
    (if (zero? (mod el 5)) (red-fn acc el) acc)))

;;reducer factory with hardcoded function like  (r/map inc)
(defn divisible-by-5-reducer [coll] (r/reducer coll xf))

(reduce conj [] (divisible-by-5-reducer range-to-100-by-3))

;;composing with build in reducers
(def composed (comp (r/map inc) (partial divisible-by-5-reducer)))

(reduce conj [] (composed range-to-100-by-3))




