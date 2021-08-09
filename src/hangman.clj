(ns hangman
  (:require [clojure.string :as str]))

(defn get-random-line
  "Chooses a random line from the given file."
  [file-name]
  (-> (slurp file-name) (str/split-lines) (rand-nth)))

(defn repeat-string
  "Repeats the given string n times."
  [string n]
  (str/join "" (repeat n string)))

(defn show-hidden-characters
  "Replaces all occurrences of a given character in a hidden string so it matches the target string."
  [hidden-text target-text character]
  (str/join "" (map (fn [hidden target] (if (= target (first character)) character hidden)) hidden-text target-text)))


(defn next-round
  "Handles a round of Hangman."
  [guessed-word target-word wrong-attempts guessed-characters]
  (println (repeat-string "*" 50))
  (cond
    (= wrong-attempts 7) (do (println "You were unable to guess the word in 7 attempts, therefore you loose.")
                             (println "The word was" target-word))
    (= guessed-word target-word) (println "You successfully guessed the word! Congratulations, you won!")
    :else (do (println "The word is" guessed-word)
              (println "Wrong guesses:" wrong-attempts)
              (println "All guessed characters:" (str/join ", " guessed-characters))
              (println "Please guess a character of this word:")
              (let [guess (str/upper-case (read-line))]
                (if (.matches guess "^[A-Za-z]$")
                  (if (.contains target-word guess)
                    (do (println "The word contains" guess)
                        (recur (show-hidden-characters guessed-word target-word guess) target-word wrong-attempts (conj guessed-characters guess)))
                    (do (println "The word unfortunately does not contain" guess)
                        (recur guessed-word target-word (inc wrong-attempts) (conj guessed-characters guess))))
                  (do (println "Please only enter one character!")
                      (recur guessed-word target-word wrong-attempts guessed-characters)))))))

(defn -main []
  (println "Welcome to Hangman!")
  (println "Win by guessing the characters of a common but random English word")
  (println "Be aware: You may only make 7 mistakes!")
  (let [target-word (get-random-line "resources/hangman_input.txt")
        hidden-word (repeat-string "_" (count target-word))]
    (next-round hidden-word target-word 0 '[])))
