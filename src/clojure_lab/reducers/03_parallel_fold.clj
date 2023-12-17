(ns clojure-lab.reducers.03-parallel-fold
  (:require [clojure.core.reducers :as r])
  )


;;should be parallel
(def ^:private increment-even (comp (r/filter even?) (r/map inc)))
(comment
  (r/fold + (increment-even [1 2 2 3 3 4]))
  (r/fold into conj (increment-even [1 1 1 2 2 3 3 4 5 5]))

  (defn logging-inc [i]
    (println "processing : " i "in thread:"  (Thread/currentThread))
    (inc i))
  (def logging-reducer (comp (r/filter even?) (r/map logging-inc)))

  (r/fold into conj (logging-reducer (range 10000))) ;;serial - list not supported
  
  (def wektor (vec (range 10000)))
  (r/fold into conj (logging-reducer wektor)) 
  (r/fold 3000 into conj (logging-reducer wektor));;parallel :vectors, maps and foldcat objects supported
  )