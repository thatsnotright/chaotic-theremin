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
    (ref-set plot-points {:x (conj (@plot-points :x) x), :y (conj (@plot-points :y) (- y))}
    )
  ))
(defn please-exit []
  (System/exit 0))
(defn -main [& args]
  (let
    [
      filename (first args)
      var (read-string (nth args 1))
      time 0
      x 1
      y 2
      z 3
      ]

    (definst theremin [adj 0]
      (let [snd (bpf (* (sin-osc 100000) (sin-osc (+ 100500 (* adj 75)))))]
        (normalizer snd)
        ))

    (theremin :adj 0)
    (def start (now))
    (doseq [item (take-csv filename)]
      ;["29.999" "0.288" "0.397" "21.147" "1.5579291285e+172" "3.4344899764e+172" "8.86189945762e+170" "1.32206346732e+172" "2.91451879525e+172" "7.52023523385e+170" "-5.11707321298e+171" "-1.12807035551e+172" "-2.91072215681e+170"]
      (let [
           v_time (read-string (nth item time))
           v_var (read-string (nth item var))
           apply_time (+ (* v_time 1000) start)
           ]
        (apply-at apply_time ctl [theremin :adj v_var])
        (apply-at apply_time addval [v_time v_var] )
        )
      )
    (apply-at (+ 30000 start) stop [])
    (apply-at (+ 30000 start) please-exit [])

    )
  )
