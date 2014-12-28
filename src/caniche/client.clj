(ns caniche.client
   (require [ring.util.codec :as c]
            [clojure.string :as string]
            [clojure.set :as set]
            [clj-http.client :as client]
            [hickory.core :as hc]
            [hickory.select :as hs]))

;; TODO connection manager

(def chunk-size 200)

(def max-story-id 70000)

(def hostname "www.pudelek.pl")

(def origin (str "http://" hostname))

;; TODO
(defn random-user-agent []
  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")

(defn standard-headers []
  { "Host" hostname
    "Cache-Control" "max-age=0"
    "Content-Type" "application/x-www-form-urlencoded"
    "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
    "Origin" origin
    "User-Agent" (random-user-agent)
    "DNT" 1
    "Accept-Language" "en-US,en;q=0.8,pl;q=0.6"})

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

(defn post-comment [story-id body nick]
  "Returns :url and :anchor"
  (let [body (create-comment-body story-id body nick)
        referer (str origin  "/artykul/" story-id)
        headers (assoc (standard-headers) "Referer" referer)]
    (-> (client/post (str origin "/komentarz") {:body body
                                                :body-encoding "UTF-8"
                                                :headers headers})
        :headers
        (get "Location")
        (string/split #"#")
        (zipmap [:url :anchor])
        set/map-invert)))

(defn get-comment [url anchor]
  (let [parsed-body (-> (client/get url {:headers (standard-headers)})
                        :body
                        hc/parse
                        hc/as-hickory)]
    (-> (hs/select 
         (hs/child 
          (hs/id anchor) 
          (hs/class "comment-body")
          (hs/class "comment-text")) 
         parsed-body)
        first
        :content
        first
        string/trim)))




