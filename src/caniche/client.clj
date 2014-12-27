(ns caniche.client
  (require [ring.util.codec :as c]
           [clojure.string :as string]
           [clj-http.client :as client]))

(def chunk-size 200)

(def max-story-id 70000)

(def hostname "www.pudelek.pl")

(def origin (str "http://" hostname))

(def cm (clj-http.conn-mgr/make-reusable-conn-manager {:timeout 10 :threads 1}))

(def cs (clj-http.cookies/cookie-store))

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

(defn get-story [story-id]
  (prn ">>>>>>>>>>>>>>>>")
  (client/get (str origin "/artykul/" story-id) {
                                                 :headers (standard-headers)
                                                 :connection-manager cm
                                                 :cookie-store cs}))

(defn post-comment [story-id body nick]
  (let [body (create-comment-body story-id body nick)
        referer (-> (get-story story-id) :trace-redirects last)
        headers (assoc (standard-headers) "Referer" referer)]
    (client/post (str origin "/komentarz") { :body body
                         :body-encoding "UTF-8"
                         :content-length (count body)
                         :headers headers
                         :connection-manager cm
                         :cookies {"SID" {:discard true, :path "/", :secure false, :value "pudelg2", :version 0}}})))


;; ;; TODO connection-manager,
;; ;; Cookie manager
;; ;; monadify

;;                                    "Cookie" "SID=pudelp1; __gfp_64b=AlDH1o19ARZLMt5hDBLOashSpKdifniBjewIBUrYXnL.n7; cm-cookies=true; o2fbox_d_=true; __utmt=1; __utmt_b=1; _urac_v2_=74619; _bfp=1015254217; __utma=21588644.1189142059.1419055197.1419055197.1419595110.2; __utmb=21588644.62.9.1419601164914; __utmc=21588644; __utmz=21588644.1419055197.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)"
;;                                    :debug true :debug-body true}}))))


















