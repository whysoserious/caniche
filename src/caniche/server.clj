(ns caniche.server
  (require [clojure.java.io :as io]
           [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [ring.util.codec :as c]
           [ring.middleware.params :as params]
           [ring.middleware.multipart-params :as multipart-params])
  (import [org.apache.commons.io IOUtils]))

(defn split-into-chunks [rdr size]
  "return lazy seq of chunks"
  (prn size))

(defroutes caniche-routes
  (POST "/upload"
        params
        (prn "===============================")
        (doseq [p params] (prn (str "> "  p)))
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body "OK!\n"}))

(defn mem-store [{filename :filename input-stream :stream}]
  {:filename filename
   :payload (IOUtils/toByteArray input-stream)})

(def app
  (-> 
   caniche-routes
   (multipart-params/wrap-multipart-params {:store mem-store})))
