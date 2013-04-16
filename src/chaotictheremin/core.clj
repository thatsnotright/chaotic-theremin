(ns chaotictheremin.core
  (:require [chaotictheremin.rk4 :as rk4])
  (:gen-class))

(use 'chaotictheremin.rk4)
(defn pendulum []
  "Test the Runge-Kutta order 4 method for a classic pendulum dtheta/dt=omega,domega/dt=(-g/L)sin(theta),L=1m,theta(0)=20degrees,omega(0)=0"
  (let [sigma 10
        beta (/ 8 3)
        rho 28
        T0 0.0
        N 30
        DT 0.001
        fps  {:x #(* sigma (- (:y %) (:x %))),
              :y #(- (* (:x %) (- rho (:z %)) (:y %))),
              :z #(- (* (:x %) (:y %)) (- beta (:z %)))}
        outfps {:x #(:x %), :y #(:y %), :z #(:z %), :t #(:t %)}
        ]
    (rk4/odesolve rk4 fps {:x 0.0, :y 0.01 :z 0.01} :t T0 N DT outfps)
    ))

(defn -main [& args]
  (let [a (pendulum)]
    (doseq [item (apply mapv vector [(:x a) (:y a)])] (prn (clojure.string/join "," item))))
  )