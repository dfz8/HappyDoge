(ns test-game.entities
  (:require 
    [test-game.utils :as u]
    [play-clj.core :refer :all]
    [play-clj.g2d :refer :all]))


(defn create-pipe
  [img]
  (let [gap-height  (+ (/ u/game-height 4) (rand-int (/ u/game-height 2)))]
    [(assoc img
    :width 100
    :height u/game-height
    :x-velocity -50
    :y-velocity 0
    :x u/game-width
    :y (+ gap-height 100)
    :passed? false
    :obstacle? true
    )
    (assoc img
    :width 100
    :height u/game-height
    :x-velocity -50
    :y-velocity 0
    :x u/game-width
    :y (- gap-height 100 u/game-height)
    :passed? false
    :obstacle? true
    )]))

(defn create-player
  [img]
  (assoc img
    :width 100
    :height 100
    :x-velocity 0
    :y-velocity 0
    :x (- (/ u/game-width 2) 50)
    :y (+ (/ u/game-height 2) 50)
    :score 0
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

(defn prevent-move
  [entities {:keys [x y x-change y-change height player?] :as entity}]
  (if (and player? 
           (or (< y 0) ;floor bound for now...should = loss in the future
               (> y (- u/game-height height)) ;ceiling bound
               ))
    (assoc entity
           :x-velocity 0
           :y-velocity 0
           :x-change 0
           :y-change 0
           :x (- x x-change)
           :y (- y y-change))
    entity))