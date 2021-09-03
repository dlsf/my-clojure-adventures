(ns cross-sum)

(defn cross-sum [n]
  "Calculates the cross sum of a number."
  (->> (str n)
       (map #(Integer/parseInt (str %)))
       (reduce +)))

(defn -main []
  (println "Please enter the number you want to calculate the cross sum of:")
  (println (cross-sum (read-line))))
