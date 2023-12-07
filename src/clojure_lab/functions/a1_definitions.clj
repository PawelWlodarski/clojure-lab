(ns clojure-lab.functions.a1-definitions)


;;example of potential meta fields

(defn ^{:main-meta 1} meta-example 
  "documentation"
  {:sub-main 2} (^{:arg-meta 3} [a b]
   {:body-meta 4} (+ a b))
  {:end-meta 5}
  )

(comment 
  (meta #'meta-example) 
  (meta (first (:arglists (meta #'meta-example))))
  )


;;an example of finding each function with meta tag ^:wrap and modifying it
(defn ^:wrap function1 [arg]
  (println "function1 : " arg))

(defn function2 [arg1 arg2]
  (println "function2 : " arg1 ":" arg2))

(defn ^:wrap function3 [arg1]
  (println "function3 : " arg1))

(defn- wrap-fn [f]
  (fn [& args]
    (println "wrapped function")
    (apply f args)))

(defn- tagged-by [tag ns-name]
  (->> (ns-publics ns-name)
       vals
       (filter #(get (meta %) tag))))

(defn- change-definition [f]
  (alter-var-root f (constantly (wrap-fn @f))))

(defn run-wrapper []
  (->> (tagged-by :wrap 'clojure-lab.functions.a1-definitions)
       (map change-definition)
       dorun
       )
  )


  
  
  
  