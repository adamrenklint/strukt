(def project 'adamrenklint/strukt)
(def version "1.1.0")

(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojurescript   "1.9.542"]
                 [adamrenklint/prost          "1.1.0"]
                 [adzerk/bootlaces            "0.1.13" :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.0"  :scope "test"]
                 [adamrenklint/boot-fmt       "1.1.0"  :scope "test"]])

(require '[adzerk.bootlaces :refer :all]
         '[adamrenklint.boot-fmt :refer [fmt]]
         '[boot.git :as git]
         '[crisptrutski.boot-cljs-test :refer [test-cljs]])

(bootlaces! version)

(ns-unmap 'boot.user 'test)

(deftask test
  [e exit?     bool  "Exit after running"]
  (merge-env! :source-paths #{"test"})
  (test-cljs :exit? exit?))

(deftask watch-test []
  (comp (watch)
        (test)))

(deftask release []
  (comp (build-jar)
        (push-release)
        (dosh "git" "push" "--tags")))

(task-options!
  pom {:project     project
       :version     version
       :description "ClojureScript map factory with shape validation using prost and cljs.spec"
       :url         "https://github.com/adamrenklint/strukt"
       :scm         {:url "https://github.com/adamrenklint/strukt"}
       :license     {"MIT" "https://github.com/adamrenklint/strukt/blob/master/LICENSE"}})
