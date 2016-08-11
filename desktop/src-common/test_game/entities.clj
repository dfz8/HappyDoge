(ns test-game.entities
  (:require 
    [test-game.utils :as u]
    [play-clj.core :refer :all]
    [play-clj.g2d :refer :all]))


(defn create-pipe
  [img]
  (let [gap-height  (+ (/ u/game_height 4) (rand-int (/ u/game_height 2)))]
    [(assoc img
    :width 100
    :height u/game_height
    :x-velocity -50
    :y-velocity 0
    :x u/game_width
    :y (+ gap-height 100)
    :obstacle? true
    )
    (assoc img
    :width 100
    :height u/game_height
    :x-velocity -50
    :y-velocity 0
    :x u/game_width
    :y (- gap-height 100 u/game_height)
    :obstacle? true
    )
     ]
    )
)

(defn create-player
  [img]
  (assoc img
    :width 100
    :height 100
    :x-velocity 0
    :y-velocity 0
    :x (- (/ u/game_width 2) 50)
    :y (+ (/ u/game_height 2) 50)
    :player? true))


(defn move
  [{:keys [delta-time]} entities {:keys [x y] :as entity}]
  (let [
        [x-velocity y-velocity] (u/get-velocity entities entity)        
        x-change (* x-velocity delta-time)
        y-change (* y-velocity delta-time)]
    (if (or (not= 0 x-change) (not= 0 y-change))
      (assoc entity
             :x-velocity x-velocity 
             :y-velocity y-velocity 
             :x-change x-change
             :y-change y-change
             :x (+ x x-change)
             :y (+ y y-change))
      entity)))