(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]))

;; TODO route not found
(defroutes app
  (GET "/" [] "<h1>Hello World!</h1>")
  (POST "/upload"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
        {:status 200
   :headers {"Content-Type" "text/html"}
   :body params}))
