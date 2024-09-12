(ns clojure-lab.functions.b1-recur)


;;simple recur
(defn print-down-to [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))

(print-down-to 7)


;;loop example
(defn sum2 [limit]
  (loop [sum 0, x limit]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))
      

(sum2 10)