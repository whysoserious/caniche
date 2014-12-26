(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [clojure.java.io :as io]
           [ring.middleware.params :as params]
           [ring.middleware.multipart-params :as multipart-params]
           [clj-http.client :as client]))

(def chunk-size 200)

(def max-story-id 70000)

(def hostname "www.pudelek.pl")

;; TODO connection-manager, 
(defn create-comment [story-id body nick]
  (let [body (str
              "?article_comment[aid]=" story-id
              "&article_comment[sid]="
              "&article_comment[response_to]="
              "&article_comment[response_bucket]="
              "&article_comment[txt]=" body
              "&article_comment[aut]=" nick)
        origin (str "http://" hostname)
        referer (str origin "/artykul/" story-id "/")]
    (client/post origin {
                         :body body
                         :body-encoding "UTF-8"
                         :headers {
                                   "Host" hostname
                                   "Connection" "keep-alive"
                                   "Cache-Control" "max-age=0"
                                   "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                                   "Origin" origin
                                   "User-Agent" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"
                                   "DNT" "1"
                                   "Referer" referer
                                   "Accept-Encoding" "gzip, deflate"
                                   "Accept-Language" "en-US,en;q=0.8,pl;q=0.6" }
                         :content-type :x-www-form-urlencoded
                         :length (count body)})))

(defn- split-into-chunks [rdr size]
  "return lazy seq of chunks"
  (prn size

(defn- handle-upload [tempfile filename]
  "File String"
  (with-open [rdr (io/reader tempfile]
    (split-into-chunks rdr chunk-size
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "OK\n"}

;; TODO route not found
(defroutes caniche-routes
  (POST "/upload"
        {{{tempfile :tempfile filename :filename} "file"} :multipart-params}
        (handle-upload tempfile filename

(def app
  (-> 
   caniche-routes
   params/wrap-params
   multipart-params/wrap-multipart-params






















