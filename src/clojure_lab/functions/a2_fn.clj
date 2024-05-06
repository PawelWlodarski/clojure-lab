(ns clojure-lab.functions.a2-fn)


(def sample-person
  {:person_id 1234567
   :person_name  "John Doe"
   :image {:url "http://focus.on/me.jpg"
    :preview "http://corporate.com/me.png"}
   :person_short_name "John"})


(def cleanup
  {:person_id [:id str]
   :person_name [:name (memfn toLowerCase)]
   :image [:avatar :url]})

;;destruction
(defn transform [orig mapping]
  (apply merge
         (map (fn [[k [k' f]]] {k' (f (k orig))}) 
              mapping)))


(transform sample-person cleanup)


;;example of fn*
;; https://groups.google.com/g/clojure/c/Ys3kEz5c_eE
(defmacro future2 [& body] ;
  `(future-call (^{:once true} fn* [] ~@body)))

(def some-data [1 2 3])
(def test-fn* (^{:once true} fn* [data] 
           (println data)
           ))

(def test-fn2 (fn [data]
                (println data)))
 
(test-fn* some-data)
(test-fn* some-data)