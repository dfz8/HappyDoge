(ns test-game.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]
            [test-game.entities :as e]
            [test-game.utils :as u]))

(declare test-game text-screen main-screen)

(defn render-if-necessary!
  [screen entities]
  (render! screen (remove #(>= 0 (+ (:x %) (:width %))) entities))
  entities)

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [screen (update! screen 
                          :camera (orthographic)
                          :renderer (stage) )
          player-image (texture "doge.png")
          pipe-image (texture "pipe.png")]
      
      (add-timer! screen :spawn-pipe 0 5 29)
      [(e/create-player player-image)]      
      ))  
  :on-timer
  (fn [screen entities]
    (case (:id screen)
      :spawn-pipe
      (apply conj entities (e/create-pipe (texture "pipe.png")))
      entities))  
  :on-render
  (fn [screen entities]
    (clear!)
    (->> entities 
         (map (fn [entity]
                (->> entity
                     (e/move screen entities)
                     )))
         (render-if-necessary! screen)
         ))
  :on-resize
  (fn [screen entities]
    (height! screen u/game_height))  
  )



(defscreen text-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "0" (color :white))
      :id :fps
      :x 5))
  :on-render
  (fn [screen entities]
    (->> (for [entity entities]
           (case (:id entity)
             :fps (doto entity (label! :set-text (str (game :fps))))
             entity))
         (render! screen)))
  :on-resize
  (fn [screen entities]
    (height! screen u/game_height))
  )

(defgame test-game-game
  :on-create
  (fn [this]
    (set-screen! this main-screen text-screen)))
