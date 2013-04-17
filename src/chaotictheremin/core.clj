(ns chaotictheremin.core
  (:gen-class))
(use 'com.konato.ode)
(use 'clojure.java.io)
(defn savedata
  "Copyright (c) 2009 Stephane Rousseau (stephaner konato.com). All rights reserved.

  The use and distribution terms for this software are covered by the Eclipse Public License 1.0,
  which can be found in the file epl-v10.html at the root of this distribution.
  By using this software in any fashion, you are agreeing to be bound by the terms of this license.
  You must not remove this notice, or any other, from this software."
  [filename & more]
  (loop [
          sqs more
          onetime true
          ]
    (if (empty? (first sqs))
      nil
      (let [toprn (map #(first %) sqs)]
        (if onetime
          (with-open [wrtr (writer filename)]
            (doall
              (map #(.write wrtr (str %))
                (interpose " " toprn)))
            (.write wrtr "\n"));delete first
          (with-open [wrtr (writer filename :append true)]
            (doall
              (map #(.write wrtr (str %))
                (interpose " " toprn)))
            (.write wrtr "\n")))
        (recur (map #(rest %)  sqs) false))))
  )

(defn lorenz []
  (let [sigma 28.0
        beta 4
        rho 46.92
        T0 0.0
        N 3
        DT 0.01
        fps {
              :xdot #(* sigma (- (:y %) (:x %))),
              :ydot #(- (* (:x %) (- rho (:z %))) (:y %)),
              :zdot #(- (* (:x %) (:y %)) (* beta (:z %)))
              :x #(:xdot %),
              :z #(:zdot %),
              :y #(:ydot %),
              }
        outfps {:x #(:x %), :y #(:y %), :z #(:z %), :t #(:t %)}
        ]
    (odesolve rk4 fps {:xt 0.0, :yt 0.0, :zt 0.0, :x 0.0, :y 1.0, :z 0.0, :xdot 0.0, :ydot 1.0, :zdot 0.0 } :t T0 N DT outfps)
    ))

(defn -main [& args]
  (let
    [a (lorenz)]
  ;[a (dampedho 1 2 100 0.9 0.0 0.0 2.0 0.01)]
    ;(doseq [item (apply mapv vector [(:x a)  (:t a)])] (println (format "%.6f,%.6f" (nth item 0) (nth item 1)))))
    (savedata "d.csv" (:t a) (:x a) (:y a) (:z a))
    )
  )