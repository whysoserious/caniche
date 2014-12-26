(ns caniche.server
  (require [compojure.core :refer :all]
           [compojure.core :as route]
           [ring.adapter.jetty :as ring]
           [ring.util.codec :as c]
           [clojure.java.io :as io]
           [clojure.string :as string]
           [ring.middleware.params :as params]
           [ring.middleware.multipart-params :as multipart-params]
           [clj-http.client :as client]))

(def chunk-size 200)

(def max-story-id 70000)

(def hostname "www.pudelek.pl")

(defn create-comment-body [story-id body nick]
  (string/join "&"
               (map
                (fn [[k v]] (str (c/url-encode k) "=" (c/url-encode v)))
                { "article_comment[aid]" story-id
                  "article_comment[sid]" ""
                  "article_comment[response_to]" ""
                  "article_comment[response_bucket]" ""
                  "article_comment[txt]" body
                  "article_comment[aut]" nick})))

;; TODO connection-manager,
;; monadify
(defn create-comment [story-id body nick]
  (let [body (create-comment-body story-id body nick)
        origin (str "http://" hostname)
        referer (str origin "/artykul/" story-id "/")]
    (prn (str ">>>" body))
    (client/post origin {
                         :body body
                         :body-encoding "UTF-8"
                         :content-length (count body)
                         :headers {
                         "Host" hostname
                         "Cache-Control" "max-age=0"
                         "Content-Type" "application/x-www-form-urlencoded"
                         "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                         "Origin" origin
                         "User-Agent" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"
                         "DNT" "1"
                         "Referer" "http://www.pudelek.pl/artykul/74619/mariah_carey_jest_zdruzgotana_po_rozstaniu_z_nickiem_cannonem/5/"
                         "Accept-Encoding" "gzip, deflate"
                         "Accept-Language" "en-US,en;q=0.8,pl;q=0.6"
                         "Cookie" "SID=pudelp1; __gfp_64b=AlDH1o19ARZLMt5hDBLOashSpKdifniBjewIBUrYXnL.n7; cm-cookies=true; o2fbox_d_=true; __utmt=1; __utmt_b=1; _urac_v2_=74619; _bfp=1015254217; __utma=21588644.1189142059.1419055197.1419055197.1419595110.2; __utmb=21588644.62.9.1419601164914; __utmc=21588644; __utmz=21588644.1419055197.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)"
                         :debug true :debug-body true})))


(defn- split-into-chunks [rdr size]
  "return lazy seq of chunks"
  (prn size))

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
