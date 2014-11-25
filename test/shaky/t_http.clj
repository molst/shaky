(ns shaky.t-http
  (:require [dev]
            [clojure.test :refer :all]
            [torpo.uri :as uri]
            [shaky.http :as http]
            [shaky.test-site :as site]))

(deftest healthcheck (is (= 'ok (http/block-read! (uri/parse "http://localhost:4499/healthcheck")))))

(deftest get-string-param "The param, which is a string, will also be wrapped in a string to consistently read back into a string."
  (is (= "1" (http/block-read! {:scheme "http" :hostname "localhost" :port 4499 :path ["get-param"] :params {:a "1"}}))))

(deftest get-string-param-as-str "Make sure the raw string-wrapped parameter can be read back untouched."
  (is (= "\"1\"" (http/block-request! {:scheme "http" :hostname "localhost" :port 4499 :path ["get-param"] :params {:a "1"}}))))

(deftest get-symbol-param "A symbol supplied over the API can be read back."
  (is (= '1 (http/block-read! {:scheme "http" :hostname "localhost" :port 4499 :path ["get-param"] :params {:a 1}}))))

(deftest get-keyword-param "A keyword supplied over the API can be read back."
  (is (= :b (http/block-read! {:scheme "http" :hostname "localhost" :port 4499 :path ["get-param"] :params {:a :b}}))))

(deftest get-string-param-as-str "Make sure the raw string-wrapped parameter can be read back untouched."
  (is (= "\"1\"" (http/block-request! {:scheme "http" :hostname "localhost" :port 4499 :path ["get-paramZZZ"] :params {:a "1"}}))))
