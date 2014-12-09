(defproject shaky "0.2-SNAPSHOT"
  :description "A web development helper library."
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :url "https://github.com/molst/shaky"
  :scm {:name "git" :url "https://github.com/molst/shaky"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [net.cgrand/moustache "1.1.0" :exclusions [org.clojure/clojure ring/ring-core]]
                 [torpo "0.2-SNAPSHOT" :exclusions [org.clojure/clojure]]
                 [pisto "0.2-SNAPSHOT"]
                 [ring "1.3.1"]
                 [http-kit "2.1.16"]]
  :source-paths ["src/clj"]
  :plugins [[com.keminglabs/cljx "0.5.0"]]
  :cljx {:builds [{:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :clj}
                  {:source-paths ["src/cljx"]
                   :output-path "target/classes"
                   :rules :cljs}
                  {:source-paths ["dev/cljx"]
                   :output-path "target/dev-classes"
                   :rules :clj}
                  {:source-paths ["dev/cljx"]
                   :output-path "target/dev-classes"
                   :rules :cljs}]}
  :prep-tasks [["cljx" "once"] "javac" "compile"]
  :profiles {:dev {:source-paths ["target/dev-classes"] ;;for repl to recognize the dev ns after cljx
                   }})
