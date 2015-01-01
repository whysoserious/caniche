(defproject caniche "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[compojure "1.3.1"]
                 [commons-io/commons-io "2.4"]
                 [crypto-random "1.2.0"]
                 [hickory "0.5.4"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [http-kit "2.1.16"]
                 [ring/ring-codec "1.0.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-jetty-adapter "1.3.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler caniche.server/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})












