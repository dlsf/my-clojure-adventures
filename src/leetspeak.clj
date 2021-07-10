(ns leetspeak
  (:require [clojure.string :as str]))

(defn text-to-leetspeak [text]
  (let [chars {"i" 1
               "l" 1
               "z" 2
               "e" 3
               "a" 4
               "s" 5
               "t" 7
               "b" 8
               "g" 9
               "o" 0}]
    (->> text (map #(get chars (str/lower-case %) %)) (str/join ""))))

(defn -main []
  (println "Please enter the text:")
  (println (text-to-leetspeak (read-line))))
