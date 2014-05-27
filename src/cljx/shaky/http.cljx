(ns shaky.http
  (:require [org.httpkit.client :as http])
  (:require [ring.util.codec :as rinc])
  (:require [torpo.core :as torpo])
  (:require [torpo.uri :as uri]))

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

(defn prepare-for-transmission [uri] (update-in uri [:params] (fn [old-val] (torpo/doto-vals (case (:request-method uri) :get rinc/url-encode :post str) (:params uri)))))

(defn block-read! "Reads the first clojure object from 'uri'." [uri]
  (let [options (options (prepare-for-transmission uri))
        {:keys [body] :as response} @(http/request options nil)
        ;;first-value (if body (read-string body)) ;;if body was text this would be fine
        first-value (if body (read (java.io.PushbackReader. (clojure.java.io/reader body))))]
    first-value))

