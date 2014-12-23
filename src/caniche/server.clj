(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [clojure.java.io :as io]))

;; TODO route not found
(defroutes caniche-routes
  (GET "/" [] "<h1>Hello World!</h1>\n")
  (POST "/x"
        params
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body (str params)})
  (POST "/upload"
        {{{tempfile :tempfile filename :filename} :file} :params :as params}
;;        (io/copy tempfile (io/file "resources" "public" filename))
        {:status 200
         :headers {"Content-Type" "text/html"}
         :body "OK\n"}))

(defn log-request [app]
  (fn [req]
    (doseq [p req] (prn p))
    (app req)))

(def app
  (-> caniche-routes
      log-request))
