(ns hangman
  (:require [clojure.string :as str]))

(defn generate-random-word [file-name]
  "Chooses a random word from the given file."
  (let [lines (str/split-lines (slurp file-name))]
    (nth lines (rand-int (count lines)))))

(defn repeat-string [string n]
  "Repeats the given string n times."
  (reduce str (repeat n string)))

(defn show-hidden-characters [hidden-text target-text character]
  "Replaces all occurrences of a given character in a hidden string so it matches the target string."
  (loop [output (vec hidden-text) index 0]
    (if (= (count hidden-text) index)
      (str/join "" output)
      (if (= character (str (nth target-text index)))
        (recur (assoc output index character) (inc index))
        (recur output (inc index))))))


(defn next-round [guessed-word target-word wrong-attempts guessed-characters]
  "Handles a round of Hangman."
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
  (let [target-word (generate-random-word "resources/hangman_input.txt")
        hidden-word (repeat-string "_" (count target-word))]
    (next-round hidden-word target-word 0 '[])))
