(ns test-game.entities
  (:require 
    [test-game.utils :as u]
    [play-clj.core :refer :all]
    [play-clj.g2d :refer :all]))


(defn create-pipe
  [img]
  (assoc img
    :width 100
    :height 100
    :x-velocity -50
    :y-velocity 0
    :x 400
    :y 200
    :obstacle? true
    ))

(defn create-player
  [img]
  (assoc img
    :width 100
    :height 100
    :x-velocity 0
    :y-velocity 0
    :x 200
    :y 300
    :player? true))


(defn move
  [{:keys [delta-time]} entities {:keys [x y] :as entity}]
  (let [
        [x-velocity y-velocity] (u/get-velocity entities entity)        
        x-change (* x-velocity delta-time)
        y-change (* y-velocity delta-time)]
    (if (or (not= 0 x-change) (not= 0 y-change))
      (assoc entity
             :x-velocity x-velocity ;(u/decelerate x-velocity)
             :y-velocity y-velocity ;(u/decelerate y-velocity)
             :x-change x-change
             :y-change y-change
             :x (+ x x-change)
             :y (+ y y-change))
      entity)))