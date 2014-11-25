(ns shaky.t-core
  (:require [clojure.test :refer :all]
            [shaky.core :as core]))

(deftest parse-request-params (is (= {:a [:vk "string"]} (core/parse-request-params {:a "[:vk \"string\"]"}))))

