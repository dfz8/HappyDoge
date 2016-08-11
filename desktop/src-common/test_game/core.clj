(ns test-game.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]
            [test-game.entities :as e]))

(declare test-game text-screen main-screen)

(def ^:const WIDTH 800)
(def ^:const HEIGHT 600)
(def ^:const FHEIGHT 100)
(def ^:const XVEL 5)
(def ^:const YVEL 5)

(defn move
  [entity direction]
  (case direction
    :down 
    (if (<= (+ FHEIGHT YVEL) (:y entity))
      (update entity :y - YVEL))
    :up 
    (if (>= (- HEIGHT YVEL (:height entity)) (:y entity))
      (update entity :y + YVEL))
    :left 
    (if (<= XVEL (:x entity))
      (update entity :x  - XVEL))
    :right 
    (if (>= (- WIDTH XVEL (:width entity)) (:x entity))
      (update entity :x + XVEL))
    nil))


(defn render-if-necessary!
  [screen entities]
  (render! screen (remove #(>= 0 (:x %)) entities))
  entities)

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (let [screen (update! screen 
                          :camera (orthographic)
                          :renderer (stage) )
          player-image (texture "doge.png")
          pipe-image (texture "pipe.png")]
      
      (add-timer! screen :spawn-enemy 10 2 4)
      [(assoc (texture "Clojure_logo.gif")
         :x 200 :y 300 :width 50 :height 50 :bg? true)
       (e/create-player player-image)
       (e/create-pipe pipe-image)]      
      ))  

  :on-timer
  (fn [screen entities]
    (case (:id screen)
      :spawn-enemy
      (println "spawned enemy")
      :spawn-ally
      (println "spawned ally")
      nil))  
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
    (height! screen HEIGHT))  
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
    (height! screen HEIGHT))
  )

(defgame test-game-game
  :on-create
  (fn [this]
    (set-screen! this main-screen text-screen)))
