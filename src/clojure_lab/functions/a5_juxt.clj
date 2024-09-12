(ns clojure-lab.functions.a5-juxt)

;;first example
((juxt first second last) (range 10))

;;second example
(def words ["book" "this" "an" "awesome" "is"])
(map (juxt count identity) words)

;;non trivial example
(def people
  [{:name "Alice" :age 30 :salary 50000}
   {:name "Bob" :age 25 :salary 48000}
   {:name "Carol" :age 35 :salary 55000}])

(defn age-group [age]
  (cond
    (< age 25) "Youth"
    (< age 35) "Adult"
    :else "Senior"))

(defn salary-bracket [salary]
  (cond
    (< salary 50000) "Bracket A"
    :else "Bracket B"))

;; Using juxt to create a composite function
(def person-info
  (juxt :name
        (comp age-group :age)
        (comp salary-bracket :salary)))

;; Applying the function to each person


(defn non-trivial-example []
  (map person-info people))

;; another non trivial example
(def person-table ;
  [{:id 1234567 :name "Annette Kann" :age 31 :nick "Ann" :sex :f}
   {:id 1000101 :name "Emma May" :age 33 :nick "Emma" :sex :f}
   {:id 1020010 :name "Johanna Reeves" :age 31 :nick "Jackie" :sex :f}
   {:id 4209100 :name "Stephen Grossmann" :age 33 :nick "Steve" :sex :m}])


(def sort-criteria (juxt :age :nick)) ;
(def group-criteria (juxt :age :sex))

;; Sort person-table by age
(defn sort-by-age [t]
  (->> t
       (sort-by sort-criteria)
       (map sort-criteria)))

(sort-by-age person-table)

(defn group-by-age-sex [t]
  (->> t
       (group-by group-criteria)
       (map (fn [[k v]] (cons k (map sort-criteria v))))))

(group-by-age-sex person-table)






