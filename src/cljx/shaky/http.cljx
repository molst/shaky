(ns shaky.http
  (:require [torpo.core :as torpo]
            [torpo.uri :as uri]
            [torpo.platform :as pl]
            #+clj  [ring.util.codec :as rinc]
            #+clj  [org.httpkit.client :as http]
            #+cljs [goog.net.XhrIo :as http]))

(defn options
  "Transforms a 'base-request' on the form {:hostname \"svt.se\" :port 1234 :scheme \"http\" :path [\"member\" \"steven\"] :params {:token 1234}} to a httpkit request map."
  [{:keys [request-method] :as base-uri}]
  (merge
   #_{:as :text} ;;this would require read-string in block-read! (below)
   (when request-method {:method request-method})
   (if (= :post request-method)
     {:form-params (:params base-uri)
      :url (uri/make-uri-str (dissoc base-uri :params))}
     {:url (uri/make-uri-str base-uri)})))

(defn read-body [{body :body}]
  (when body #+clj  (read (java.io.PushbackReader. (clojure.java.io/reader body)))
             #+cljs (pl/debuglog (str "read-body not supported for clojurescript, see shaky.http"))))

(defn httpkit-to-goog-xhrio-callback "Returns a function that takes a http-kit style request result argument and produces a goog xhrio callback function."
  [httpkit-cb]
  (fn [event]
    (let [t (-> event .-target)]
      (httpkit-cb {:status (.getStatus t)
                   :body (.getResponseText t)}))))

(defn request! [uri callback]
  (println "prp: " (options (uri/prepare-for-transmission uri)))
  #+clj (http/request (options (uri/prepare-for-transmission uri)) callback)
  #+cljs (http/send (uri/make-uri-str (uri/prepare-for-transmission uri))
                    (httpkit-to-goog-xhrio-callback callback)
                    (case (:request-method uri) :post "POST" "GET")))

#+clj ;;not tested in js engine
(defn block-read! "Reads the first clojure object from 'uri'."
  [uri] (read-body @(request! uri nil)))
