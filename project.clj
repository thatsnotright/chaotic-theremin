(defproject chaotic-theremin "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [overtone "0.8.1" :exclusions [org.clojure/clojure]]
                 [konato/konato "1.0.0"]
                ]
:plugins [[lein-localrepo "0.4.1"]]
  :aot [chaotictheremin.core]
  :main chaotictheremin.core)
