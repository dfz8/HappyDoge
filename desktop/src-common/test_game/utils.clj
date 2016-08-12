(ns test-game.utils
  (:require [play-clj.core :refer :all]
           )
  )


(def ^:const game-width 800)
(def ^:const game-height 600)
(def ^:const frame-height 100)
(def ^:const collistion-dist 5)

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


(defn on-top?
  [player pipe]
  (and (<= (:x pipe) (+ (:width player) (:x player))) (>= (+ (:x pipe) (:width pipe)) (:x player))))

(defn pass-through?
  [player pipe-low pipe-high]
  (and (< (+ (:height player) (:y player)) (:y pipe-high))
       (> (:y player) (+ (:height pipe-low) (:y pipe-low)))
       )
  )

(defn near-entity?
  [{:keys [x y width height id] :as e} e2]
    (if (and (not= id (:id e2))
             (not (or 
                    (< (+ x width) (:x e2))
                    (> x (+ (:x e2) (:width e2)))
                    (< (+ y height) (:y e2))
                    (> y (+ (:y e2) (:height e2)))
                    ))
         )
      (println "overlap")
      )  
  )

(defn near-entities?
  [entity entities]
  (some #(near-entity? entity %) entities)
  )