(ns chaotictheremin.draw
  (:gen-class )
  (:use quil.core
        quil.applet
        [quil.helpers.drawing :only [line-join-points]]
        [quil.helpers.seqs :only [range-incl]]
        [quil.helpers.calc :only [mul-add]])
  )

(def plot-points (ref {:x [], :y []}))

(defn setup []
  (smooth)                          ;;Turn on anti-aliasing
  (frame-rate 30)                    ;;Set framerate to 1 FPS
  (background 200))                 ;;Set the background colour to

(defn custom-rand
  []
  (- 1 (pow (random 1) 5)))
(def screen-w 500)
(def screen-h 500)
(def scale-z 600)

(def start-x 0.0)
(def end-x 30.0)
(def start-y -30.0)
(def end-y 30.0)

(def rot-x (atom 0.0))
(def rot-y (atom 0.0))
(def last-x (atom 0.0))
(def last-y (atom 0.0))
(def dist-x (atom 0.0))
(def dist-y (atom 0.0))
;(def zoom-z -300)
(def zoom-z (atom -20000))

(defn draw []
  (background 0)
  (stroke 255)             ;;Set the stroke colour to a random grey
  (stroke-weight 1)       ;;Set the stroke thickness randomly
  (translate 0 (/ screen-h 2))
  (push-matrix)
  (.scale (current-applet) (/ screen-w 30.0) (/ screen-h 100.0))
  (dosync
    (let [xs        (@plot-points :x)
          ys        (@plot-points :y)
          line-args (line-join-points xs ys)]

      (dorun (map #(apply line %) line-args)))
    )
  (pop-matrix)
  )

(defsketch chaoticgraph                  ;;Define a new sketch named example
  :title "Waveform"  ;;Set the title of the sketch
  :setup setup                      ;;Specify the setup fn
  :draw draw                        ;;Specify the draw fn
  :renderer :p3d
  :size [screen-w screen-h])