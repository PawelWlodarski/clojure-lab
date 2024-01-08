(ns clojure-lab.reducers.04-reduced)

;;signals end of the reduction
(reduce
 (fn [acc el]          
   (if (> el 5)
     (reduced acc)     
     (+ acc el)))
 (range 10))
