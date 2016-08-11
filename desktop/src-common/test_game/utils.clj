(ns test-game.utils
  (:require [play-clj.core :refer :all]
           )
  )


(def ^:const game_width 800)
(def ^:const game_height 600)
(def ^:const frame-height 100)

(def ^:const max-velocity 200)
(def ^:const acceleration 85)
(def ^:const gravity 15)
(def ^:const obstacle-velocity 60)


(defn ^:private get-player-velocity
  [{:keys [x-velocity y-velocity]}]
  [ 0
   (cond 
     (key-pressed? :space)
     (min max-velocity (+ y-velocity acceleration))
     :else 
     (max (* -1.5 max-velocity) (- y-velocity gravity))
     )
   ])

(defn ^:private get-obstacle-velocity
  [{:keys [x-velocity y-velocity]}]
  [(* -1 obstacle-velocity) 0]
  )

(defn get-velocity
  [entities {:keys [player? obstacle?] :as entity}]
  (cond
    player? (get-player-velocity entity)
    obstacle? (get-obstacle-velocity entity)
    :else [0 0]))