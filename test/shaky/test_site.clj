(ns shaky.test-site
  (:require [net.cgrand.moustache :as m]
            [pisto.core :as pisto]
            [ring.adapter.jetty :as ringj]))

(defonce state nil)

(def routes (-> (m/app
  ["healthcheck"] (m/app :get (fn [req] {:body "ok"})))))

(defmethod pisto/start-part :shaky [[type {:keys [config]}]]
  (let [site-jetty-port (:port (:uri config))]
    (alter-var-root #'state (fn [_] {:site-jetty (when site-jetty-port (ringj/run-jetty #'routes {:port site-jetty-port :join? false}))}))))

(defmethod pisto/stop-part  :shaky [[type part]]
  (let [site-jetty (:site-jetty (:state part))]
    (when site-jetty (.stop site-jetty))))
