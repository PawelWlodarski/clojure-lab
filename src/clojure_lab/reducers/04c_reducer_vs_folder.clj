(ns clojure-lab.reducers.04c-reducer-vs-folder
  (:require [clojure.core.reducers :as r])
  
  )


(comment 
  (time (->> (range 5000)
             (into [])
             (r/map range)
             (r/mapcat conj)
             (r/drop 0)                     ; only reducer in chain
             (r/filter odd?)
             (r/fold +)))
  )


(comment 
  (time (->> (range 5000)
             (into [])
             (r/map range)
             (r/mapcat conj)                   ; only folders in chain
             (r/filter odd?)
             (r/fold +)))
  )