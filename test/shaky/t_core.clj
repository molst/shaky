(ns shaky.t-core
  (:use midje.sweet)
  (:require [shaky.core :as core]))

;;Load the forms below to run all tests in the project.
#_(require 'midje.repl)
#_(midje.repl/load-facts)

(defmethod core/pick-part :car [part-req] :audi)
(defmethod core/pick-part :boat [part-req] :fjordling)
(defmethod core/pick-part :animal [part-req] part-req)

(defn body-equals [value result] (= value (read-string (:body result))))
(defn body-equals-in [select-vec value result] (= value (get-in (read-string (:body result)) select-vec)))

(fact "pick by vector"                   (core/pick-parts {:pick-only [:car]    }) => {:car :audi})
(fact "pick by map without filter value" (core/pick-parts {:pick-only {:car nil}}) => {:car :audi})
(fact "pick by vector with dependency order" (core/pick-parts {:pick-only [:car] :part-dependency-order [:kalle]}) => {:car :audi})
(fact "response-body can handle a pick-parts vector" (core/response-body {:params {"pick-only" (str [:car])}}) => (partial body-equals {:car :audi}))
(fact "response-body can handle a pick-parts map" (core/response-body {:params {"pick-only" (str {:car nil :boat nil})}}) => (partial body-equals {:car :audi :boat :fjordling}))
(fact "response-body can pick via optional map" (core/response-body {} {:pick-only [:car]}) => (partial body-equals {:car :audi}))
(fact "pick-only in request overrides pick-only in options map" (core/response-body {:params {"pick-only" (str {:animal "snake"})}}
                                                                                    {:pick-only {:animal "bison"}})
  => (partial body-equals-in [:animal :selector] "snake"))

(fact "response-body can pick via optional map and :request-method set to :get"
    (core/response-body {:request-method :get} {:pick-only {:animal "bison"} :part-dependency-order [:animal :widgets]})
  => (partial body-equals-in [:animal :selector] "bison"))
