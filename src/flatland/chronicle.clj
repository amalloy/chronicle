(ns flatland.chronicle
  (:require [clj-time.core :as time :refer [minutes]]
            [clj-time.periodic :refer [periodic-seq]])
  (:import (org.joda.time DateTime Partial DateTimeFieldType)))

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

(defn round-time [^DateTime time]
  (.withFields time (Partial.
                     (into-array DateTimeFieldType
                                 [(DateTimeFieldType/secondOfMinute)
                                  (DateTimeFieldType/millisOfSecond)])
                     (int-array [0 0]))))

(defn times-for
  "Get all times starting at `start` that match the spec.
   A spec is a map that represents something very similar
   to a cron job. See default-spec for an example."
  [spec start]
  (let [spec (make-spec spec)]
    (->> (for [time (periodic-seq (round-time start) (minutes 1))
               :when (time-match? spec time)]
           time)
         (drop-while #(time/before? % start)))))
