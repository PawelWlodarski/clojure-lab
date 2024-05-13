(ns clojure-lab.functions.a3-fnil)

;first example
(defn lookup [key]
  (key {:first "first" :second "second"}) 
  )

(def safe-lookup (fnil lookup :first))

(safe-lookup :second)
(safe-lookup nil)

;;second example
#_(update {:a 1 :b 2} "c" inc) ;;nullpointer exception
(update {:a 1 :b 2} "c" (fnil inc 0))


;;nil more arguments

(defn fnil+ [f & defaults]
  (let [apply-default (fn [value default] (if (nil? value) default value))]

    (fn [& args]
      (apply f (map apply-default args (concat defaults (repeat nil)))))))
;; => #'clojure-lab.functions.a3-fnil/fnil+

#_(+ 1 2 nil 4 5 nil)

(def zero-defaulting-sum 
  (apply fnil+ + (repeat 0))
  )

(zero-defaulting-sum 1 2 nil 4 5 nil)