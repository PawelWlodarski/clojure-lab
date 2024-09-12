(defproject clojure-lab "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 ;;[org.clojure/clojure "1.12.0"]
                 [org.clojure/core.async "1.6.673"]
                 [nubank/matcher-combinators "3.8.3"]
                 [tortue/spy "2.14.0"]
                 [com.taoensso/timbre "5.2.1"]
                 [funcool/cats "2.2.0"]
                 [metosin/malli "0.14.0"]
                 [com.clojure-goes-fast/clj-java-decompiler "0.3.4"]
                 [philoskim/debux "0.9.1"]
                 ]
  :main ^:skip-aot clojure-lab.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
