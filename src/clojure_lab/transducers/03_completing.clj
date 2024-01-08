(ns clojure-lab.transducers.03-completing)

;;example one - calulating average when division is the last step
(def events                             ;
  (apply concat (repeat
                 [{:device "AX31F" :owner "heathrow"
                   :date "2016-11-19T14:14:35.360Z"
                   :payload {:temperature 62.0
                             :wind-speed 22
                             :solar-radiation 470.2
                             :humidity 38
                             :rain-accumulation 2}}
                  {:device "AX31F" :owner "heathrow"
                   :date "2016-11-19T14:15:38.360Z"
                   :payload {:wind-speed 17
                             :solar-radiation 200.2
                             :humidity 46
                             :rain-accumulation 12}}
                  {:device "AX31F" :owner "heathrow"
                   :date "2016-11-19T14:16:35.360Z"
                   :payload {:temperature 63.0
                             :wind-speed 18
                             :humidity 38
                             :rain-accumulation 2}}])))


(defn average [k n]                     ;
  (transduce
   (comp
    (map (comp k :payload))
    (remove nil?)
    (take n))
   (completing + #(/ % n))
   events))

(average :temperature 10)

(average :solar-radiation 60)

;;STATEFUL TRANSDUCER
;;Example 2 - simple logging in transducers and completing functions
(def xform                              ;
  (comp (map inc) ;; xform is composition of high order functions some-f => some-other-f
        (partition-all 3) ;; a stateful transducer with an internal buffer length 3
        cat)) ;; unwraps any inner lists created by partition-all

;;completing is the last parameter in function chain composition
(def last-step (completing + #(do (print "#done! ") %)))
(def xform-reductor (xform last-step))

(xform-reductor 0 0)  ;;state [1]

(xform-reductor 0 0) ;;state [1,1]

(xform-reductor 0)  ;;one param triggers completion so 1+1

(transduce xform last-step (range 10))
(transduce xform + (range 10))


