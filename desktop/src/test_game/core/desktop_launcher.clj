(ns test-game.core.desktop-launcher
  (:require [test-game.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. test-game-game "test-game" WIDTH HEIGHT)
  (Keyboard/enableRepeatEvents true))
