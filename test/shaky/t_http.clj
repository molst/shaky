(ns shaky.t-http
  (:require [dev]
            [clojure.test :refer :all]
            [torpo.uri :as uri]
            [shaky.http :as http]
            [shaky.test-site :as site]))

(deftest healthcheck (is (= "ok" (http/block-read! (uri/parse "http://healthcheck")))))
