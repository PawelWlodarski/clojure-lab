(ns clojure-lab.reducers.01-transduce)


(def processor (comp (filter odd?) (map inc)) )
(transduce processor + (range 10))

;;logging example
(defn logging-filter [pred]
  (fn [rf]
    (fn
      ([] (rf))
      ([result] (rf result))
      ([result input]
       (println "filtering : " input)
       (if (pred input)
         (rf result input)
         result))))
  )

(defn logging-mapping [f]
  (fn [rf]
    (fn
      ([] (rf))
      ([result] (rf result))
      ([result input]
       (println "mapping : "input)
       (rf result (f input)))
      ([result input & inputs]
       (rf result (apply f input inputs)))))
  )

(transduce (comp (logging-filter odd?) (logging-mapping inc)) + (range 10))


;;completing - completing function run for last iteration
(transduce (map inc) - 0 (range 10))  ;; (- -55)
(transduce (map inc) (completing -) 0 (range 10)) ;;  ([f] (completing f identity))