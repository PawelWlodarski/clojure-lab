(ns clojure-lab.reducers.05-monoid
  (:require [clojure.core.reducers :as r])
  )
;;simple string monoid
(def string-monoid (r/monoid str (fn [] "START:")))
(r/fold string-monoid ["raz" " " "dwa" " " "trzy"])

;;domain monoid
(defn money [value currency]
  {:value value :currency currency})

(defn money-monoid [currency]
  (r/monoid (fn [{v1 :value c1 :currency} {v2 :value c2 :currency}]
              (when (not= c1 c2)
                (throw (RuntimeException. (str "different currencies c1: " c1 " c2:" c2))))
              {:value (+ v1 v2), :currency currency}) ;;op
            (constantly {:value 0 :currency currency}))) ;; zero

(reduce (money-monoid "PLN") [{:value 1 :currency "PLN"}
                              {:value 2 :currency "PLN"}
                              {:value 3 :currency "PLN"}])

;;reduction, monoid and mapping

(defn USD-to-PLN [{:keys [value currency] :as input-money}]
  (if (= currency "PLN")
    input-money
    {:value (* value 0.5) :currency "PLN"}))


(comment (reduce (money-monoid "PLN") [{:value 1 :currency "PLN"}
                              {:value 2 :currency "USD"}
                              {:value 3 :currency "PLN"}]))


(reduce (money-monoid "PLN")  (r/map USD-to-PLN [{:value 1 :currency "PLN"}
                                                 {:value 2 :currency "USD"}
                                                 {:value 3 :currency "PLN"}]))