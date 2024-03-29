(ns clojure-lab.reducers.02-eduction
  (:import [java.util Date])
  )

(def ^:private data
  [{:fee-attributes [49 8 13 38 100]
    :product {:visible true
              :online true
              :name "Switcher AA126"
              :company-id 183
              :part-repayment true
              :min-loan-amount 5000
              :max-loan-amount 1175000
              :fixed true}
    :created-at 1504556932728}
   {:fee-attributes [11 90 79 7992]
    :product {:visible true
              :online true
              :name "Green Professional"
              :company-id 44
              :part-repayment true
              :min-loan-amount 25000
              :max-loan-amount 3000000
              :floating true}
    :created-at 15045569334789}
   {:fee-attributes [21 12 20 15 92]
    :product {:visible true
              :online true
              :name "Fixed intrinsic"
              :company-id 44
              :part-repayment true
              :min-loan-amount 50000
              :max-loan-amount 1000000
              :floating true}
    :created-at 15045569369839}])

(defn- merge-into [k ks]    ;
  (map (fn [m]
         (merge (m k) (select-keys m ks)))))


(defn- update-at [k f]
  (map (fn [m]
         (update m k f))))
(defn- if-key [k]
  (filter (fn [m]
            (if k (m k) true))))
(defn if-equal [k v]
  (filter (fn [m]
            (if v (= (m k) v) true))))

(defn if-range [k-min k-max v]
  (filter (fn [m]
            (if v (<= (m k-min) v (m k-max)) true))))

(def prepare-data           ;
  (comp
   (merge-into :product [:fee-attributes :created-at])
   (update-at :created-at #(Date. %))))

(defn filter-data [params]  ;
  (comp
   (if-key :visible)
   (if-key (params :rate))
   (if-equal :company-id (params :company-id))
   (if-key (params :repayment-method))
   (if-range :min-loan-amount
             :max-loan-amount
             (params :loan-amount))))

(defn xform [params]        ;
  (comp
   prepare-data
   (filter-data params)))


(defn- best-fee [p1 p2]               ;
  (if (< (peek (:fee-attributes p1))
         (peek (:fee-attributes p2)))
    p1 p2))

(str)
(defn best-product [params data best-fn]
  (reduce
   best-fn
   (eduction (xform params) data)))

(best-product
 {:repayment-method :part-repayment
  :loan-amount 500000}
 data
 best-fee)


