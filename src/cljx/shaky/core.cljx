(ns shaky.core
  (:require [clojure.edn :as edn]
            [torpo.uri :as uri]
            [ring.util.response :as ringres]))

(defn get-param "Gets a parameter by first looking among query params and form params, then in cookies."
  [param-key req]
  (let [param-value (get (:params req) param-key)
        param-value (if (and (nil? param-value) (string? param-key)) ;;In case key is a string it could be found in form/query params if 'wrap-keyword-params' is used.
                      (or (get (:query-params req) param-key) (get (:form-params req) param-key))
                      param-value)]
    param-value
    ;;TODO: search cookies
    ))

(defn read-param "Gets a parameter by first looking among query params and form params, then in cookies."
  [param-name req]
  (when-let [param (get-param param-name req)]
    (edn/read-string param)))

(defn make-validation-error-response [validation-errors]
  (ringres/status {:body (str {:validation-errors validation-errors})} 400)) ;;TODO: Change this to look like error-body (below)

(defn error-body [message & tags]
  {:errors [{:message message :tags (set tags)}]})

(defn error-response "If tags are integers, they will be regarded as http status codes and only the first of the integers will matter."
  [message & tags]
  (let [{integers true tags false} (group-by integer? tags)]
    (ringres/status {:body (str (apply (partial error-body message) tags))} (first integers))))

(defn parse-request-params [params]
  (apply merge (for [[param-key param-val] params :when param-val :let [read-val (edn/read-string param-val)]] ;;we do not decode here, as that part is usually done by ring middleware
                 {(keyword param-key) read-val})))

(defn take-ring-params "Takes all params from 'ring-req', assumes all params are clojure strings, and returns a map with keywords to read values."
  [ring-req & params]
  (reduce (fn [acc param] (if-let [val (read-param (name param) ring-req)] (assoc acc param val) acc)) {} params))
