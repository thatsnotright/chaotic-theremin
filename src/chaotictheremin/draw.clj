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
  (smooth) ;;Turn on anti-aliasing
  (frame-rate 15) ;;Set framerate to 15 FPS
  (background 0)) ;;Set the background colour to

(def screen-w 1000)
(def screen-h 500)
(def last-y (atom -9999999.0))
(def max-time (atom 30))

(defn scale-y [yvals]
  (if (= 0 (count yvals))
    1
    (reset! last-y (apply max [@last-y (Math/abs (last yvals))]))
    )
  )

(defn draw []
  (background 0)
  (translate 0 (/ screen-h 2))
  (scale
    (/ screen-w @max-time)
    (/ (/ screen-h 2) (scale-y (@plot-points :y ))
      )
    )
  (push-matrix)
  (stroke-weight 3)
  (stroke 128)

  (line 0 0 screen-w 0 )
  (stroke-weight 1)
  (stroke 255)

  (dosync
    (let [xs (@plot-points :x )
          ys (@plot-points :y )
          line-args (line-join-points xs ys)]
      (dorun (map #(apply line %) line-args)))
    )
  (pop-matrix)
  )

(defsketch chaoticgraph ;;Define a new sketch named example
  :title "Waveform" ;;Set the title of the sketch
  :setup setup ;;Specify the setup fn
  :draw draw ;;Specify the draw fn
  :renderer :p3d
  :size [screen-w screen-h])