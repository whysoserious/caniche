(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [clojure.java.io :as io]
           [ring.middleware.params :as params]
           [ring.middleware.multipart-params :as multipart-params]))

(defn handle-upload [tempfile filename]
  "File String"
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "OK\n"})

;; TODO route not found
(defroutes caniche-routes
  (POST "/upload"
        {{{tempfile :tempfile filename :filename} "file"} :multipart-params}
        (handle-upload tempfile filename)))

(def app
  (-> 
   caniche-routes
   params/wrap-params
   multipart-params/wrap-multipart-params))






















