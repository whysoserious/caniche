(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [clojure.java.io :as io]
           [ring.middleware.params :as params]
           [ring.middleware.multipart-params :as multipart-params]))


;; TODO route not found
(defroutes caniche-routes
  (GET "/" [] "<h1>Hello World!</h1>\n")
  (POST "/upload"
        params
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body "OK\n"}))

(defn log-request [app]
  (fn [req]
    (prn ">>>")
    (doseq [p req] (prn p))
    (app req)))

(def app
  (-> 
   caniche-routes
   log-request
   params/wrap-params
   multipart-params/wrap-multipart-params))
