(ns chaotictheremin.core
  (:gen-class )
  )

(require '[clojure.data.csv :as csv]
  '[clojure.java.io :as io])
(use 'overtone.live)
(use 'chaotictheremin.draw)

(defn take-csv
  "Takes file name and reads data."
  [fname]
  (with-open [file (io/reader fname)]
    (csv/read-csv (slurp file))))

(defn append [& parts] (apply concat parts))
(defn addval [x y]
  (dosync
    (ref-set plot-points {:x (conj (@plot-points :x ) x), :y (conj (@plot-points :y ) (- y))}
      )
    ))
(defn please-exit []
  (System/exit 0))
(defn random-string [length]
  (let [ascii-codes (concat (range 48 58) (range 66 91) (range 97 123))]
    (apply str (repeatedly length #(char (rand-nth ascii-codes))))))
(defn -main [& args]
  (let
    [
      filename (first args)
      var (read-string (nth args 1))
      scale (read-string (nth args 2))
      vol (read-string (nth args 3))
      x_idx (read-string (nth args 4))
      y_idx (read-string (nth args 5))
      time 0
      outputname (str (random-string 10) ".wav")
      ]
    (def maxvol (atom 2.0))
    (println (str "recording to " outputname))
    (recording-start outputname)
    (definst theremin [adj 0 volume 1]
      (let [snd (lpf (* (sin-osc 100000) (sin-osc (+ 100250 (* adj scale)))) 12000)
            ;            vol (if (> volume 0)
            ;                  0.10
            ;                  -0.10
            ;                  )
            ]
        ;        (if (and (> @maxvol 0) (< @maxvol 1))
        ;          (reset! maxvol (+ @maxvol vol))
        ;          )
        (* snd volume)
        ))

    (theremin :adj 0 :vol 1.5)
    (def start (now))
    (doseq [item (take-csv filename)]
      ;["29.999" "0.288" "0.397" "21.147" "1.5579291285e+172" "3.4344899764e+172" "8.86189945762e+170" "1.32206346732e+172" "2.91451879525e+172" "7.52023523385e+170" "-5.11707321298e+171" "-1.12807035551e+172" "-2.91072215681e+170"]
      (let [
             v_time (read-string (nth item time))
             v_var (read-string (nth item var))
             v_vol (read-string (nth item vol))
             apply_time (+ (* v_time 1000) start)
             v_xval (read-string (nth item x_idx))
             v_yval (read-string (nth item y_idx))
             ]
        (apply-at apply_time ctl [theremin :adj v_var])
        ;(apply-at apply_time ctl [theremin :volume v_vol])
        ; discretize the notes perhaps? somthing like volume control (apply-at (+ 100 apply_time ()
        (apply-at apply_time addval [v_xval v_yval])
        (reset! max-time v_time)
        )
      )
    (let [
           endtime (+ (* @max-time 1000) start)
           ]
      (apply-at endtime stop [])
      (apply-at endtime recording-stop [])
      (apply-at endtime please-exit [])
      )

    )
  )
