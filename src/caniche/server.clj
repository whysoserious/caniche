(ns caniche.server
  (require [clojure.java.io :as io]
           [compojure.core :refer :all]
           [compojure.core :as route]
           [clojure.core.async :as async :refer [go chan <! >!]]
           [ring.adapter.jetty :as ring]
           [ring.util.codec :as c]
           [ring.middleware.multipart-params :as multipart-params])
  (import [org.apache.commons.io IOUtils]))

(def chunk-size 20)

(def min-story-id 1000)

(def max-story-id 73000)

(defn split-into-chunks [arr size]
  (partition size size [] arr))

(defn mem-store [{filename :filename input-stream :stream}]
  {:filename filename
   :payload (IOUtils/toByteArray input-stream)})

(defn process-chunk [chunk]
  (String. (chunk)))

(defn handle-upload [payload filename]
  (let [c (chan)
        chunks (split-into-chunks payload chunk-size)]
    (prn chunks)
    ;; (go
    ;;   (doseq [chunk chunks] (>! c (process-chunk chunk)))
 ;;     )
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "OK!\n"}))

(defroutes caniche-routes
  (POST "/upload"
        {{{payload :payload filename :filename} "file"} :multipart-params}
        (handle-upload payload filename)))

(def app
  (-> 
   caniche-routes
   (multipart-params/wrap-multipart-params {:store mem-store})))



