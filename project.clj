(defproject shaky "0.2-SNAPSHOT"
  :description "A web development helper library."
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :url "https://github.com/molst/shaky"
  :scm {:name "git" :url "https://github.com/molst/shaky"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [torpo "0.2-SNAPSHOT"]
                 [ring/ring-core "1.1.7"]
                 [http-kit "2.1.12"]]
  :source-paths ["src/clj"]
  :plugins [[com.keminglabs/cljx "0.3.2"]]
  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :cljs}]}
  :hooks [cljx.hooks])