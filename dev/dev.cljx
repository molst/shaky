(ns dev
  (:require [pisto.core :as pisto]
            [clojure.tools.namespace.repl :as tns]
            [clojure.test :as t]))

(defonce system
  {:start-order [:shaky]
   :parts {:shaky {:config {:uri {:port 4499}}}}})

(defn restart [] (alter-var-root #'system #(pisto/restart-parts %)))
(defn start   [] (alter-var-root #'system #(pisto/start-parts %)))
(defn stop    [] (alter-var-root #'system #(pisto/stop-parts %))
                 (alter-var-root #'system #(pisto/clear-all-stateful-info %)))

(defn start-and-test [] (start)
  #_(t/run-tests 'shaky.t-core))

(defn reload-and-restart [] (stop) (tns/refresh-all :after 'dev/start))
(defn update-and-restart [] (stop) (tns/refresh     :after 'dev/start))

(defn reload-and-retest [] (stop) (tns/refresh-all :after 'dev/start-and-test))

(defn init [] (reload-and-retest))
