(ns test-game.utils
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]))



(def ^:const max-velocity 50)


(defn ^:private get-player-velocity
  [{:keys [x-velocity y-velocity]}]
  [(cond
     (key-pressed? :dpad-left)
     (* -1 max-velocity)
     (key-pressed? :dpad-right)
     max-velocity
     :else
     x-velocity)
   (cond
     (key-pressed? :dpad-down)
     (* -1 max-velocity)
     (key-pressed? :dpad-up)
     max-velocity
     :else
     y-velocity)])

(defn ^:private get-obstacle-velocity
  [{:keys [x-velocity y-velocity]}]
  [x-velocity y-velocity]
  )

(defn get-velocity
  [entities {:keys [player? obstacle?] :as entity}]
  (cond
    player? (get-player-velocity entity)
    obstacle? (get-obstacle-velocity entity)
    :else [0 0]))