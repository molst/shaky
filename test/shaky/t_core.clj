(ns shaky.t-core
  (:require [clojure.test :refer :all]
            [shaky.core :as core]
            [torpo.uri :as uri]))

(deftest parse-request-params (is (= {:a [:vk "string"]} (core/parse-request-params {:a "[:vk \"string\"]"}))))

(deftest parse-prepared-params "should lead to same result as for parse-request-params"
  (is (= {:a [:vk "string"]} (core/parse-request-params (:params (uri/prepare-for-transmission {:params {:a [:vk "string"]}}))))))

(deftest prepare-get-params-for-transmission
  (is (= {:a "%5B%3Avk%20%22string%22%5D"}
         (:params                            (uri/prepare-for-transmission {:params {:a [:vk "string"]}})))))

(deftest prepare-get-params-for-transmission-only-does-read-string
  "parse-request-params is used to undo prepare-for-transmission on the receiving side, but the url decoding is usually done by ring, so it should be avoided"
  (is (= {:a '%5B%3Avk%20%22string%22%5D}
         (core/parse-request-params (:params (uri/prepare-for-transmission {:params {:a [:vk "string"]}}))))))

(deftest prepare-post-params-for-transmission "params should be prn-str:ed, but not url-encoded"
  (is (= {:params {:a "[:vk \"string\"]"} :request-method :post}
         (uri/prepare-for-transmission {:params {:a [:vk "string"]} :request-method :post}))))