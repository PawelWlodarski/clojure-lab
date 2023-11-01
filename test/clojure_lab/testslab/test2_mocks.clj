(ns clojure-lab.testslab.test2-mocks
  (:require [clojure.test :refer [deftest testing is]]
            [matcher-combinators.test] 
            [spy.core :as spy :refer [spy]]))

(defn expensive-function []
  (Thread/sleep 5000)
  10
  )

(defn business-logic [param]
  (if param 5 (expensive-function)))

(deftest mocks_sample1
  (testing "business logic short route"
    ;;given
    (let [expensive-fn-spy (spy expensive-function)]
      (with-redefs [expensive-function expensive-fn-spy]
      ;;when
        (let [result (business-logic true)]
        ;;then
          (is (match? 5 result))
          (is (true? (spy/not-called? expensive-fn-spy))))))))