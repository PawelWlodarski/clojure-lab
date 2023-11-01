(ns clojure-lab.testslab.test1-intro
(:require [clojure.test :refer [deftest testing is are]]
          [matcher-combinators.test] ;;for match? 
          ))

;;sample1 shows how failing report looks like for simple cases with/without matcher
(deftest sample1 []
  (testing "fact1 use 'is' to show 1+1 = 1"
    (let [result (+ 1 1)]
      (is (= 1 result))))

  (testing "fact2 use 'is' to show 1+1 = 2"
    (let [result (+ 1 1)] ;;structure
      (is (= 2 result))))

  ((testing "fact3 use 'match' to show 1+1 = 1"
     (let [result (+ 1 1)]
       (is (match? 1 result))))))


(deftest sample2-multi-scenario [] 
  (testing "feature 1" 
    (testing "scenario 1"
      (let [;;given
            fixture1 1
            fixture2 2
            ;;when
            result (+ fixture1 fixture2)]
        ;;then
        (is (match? 3 result))))
    (testing "scenario 2"
      (let [result 3]
        (is (match? 2 result))))))


(deftest sample3-wrong-structure
  (let [expected {:key1 "value1"
               :key2 "value2"
               :key3 {:key11 "value11"
                      :key12 {:key21 "value21"}}}
        input {:key1 "value1"
                  :key2 "value------3" ;;<-here
                  :key3 {:key11 "value11"
                         :key12 {:key21 "value---------21"}}} ;;<-here
        ]

    (is (match? expected input))))


(deftest sample4-tabular-tests
  (are [x y z] (match? x (+ y z))
    2 1 1
    4 2 2
    4 4 4
    5 2 3))