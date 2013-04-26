(ns flatland.chronicle
  (:require [clj-time.core :as time :refer [minutes]]
            [clj-time.periodic :refer [periodic-seq]]))

(def extractors
  {:minute time/minute
   :hour time/hour
   :day time/day
   :month time/month
   :day-of-week time/day-of-week})

(def default-spec
  {:minute (range 60)
   :hour (range 24)
   :day (range 1 32)
   :month (range 1 13)
   :day-of-week (range 1 8)})

(defn make-spec [spec]
  (into {} (for [[k v] (merge default-spec spec)]
             [k (set v)])))

(defn time-match?
  "Determine if the given time matches the cron spec."
  [spec time]
  (every? true?
          (for [[k v] extractors]
            (contains? (get spec k) (v time)))))

(defn round-time [time]
  (time/date-time (time/year time)
                  (time/month time)
                  (time/day time)
                  (time/hour time)
                  (time/minute time)))

(defn times-for [spec start]
  (let [spec (make-spec spec)]
    (for [time (periodic-seq (round-time start) (minutes 1))
          :when (time-match? spec time)]
      time)))
