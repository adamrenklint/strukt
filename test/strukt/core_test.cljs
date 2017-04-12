(ns strukt.core-test
  (:require [cljs.test :refer-macros [is are testing deftest]]
            [strukt.core :as strukt :refer-macros [defstrukt]]
            [cljs.spec :as s]))

(defn should
  [desc & assertions]
  (testing (str "-> should " desc)
    (doseq [[actual expected] (partition 2 assertions)]
      (is (= expected actual)))))

(s/def ::one (s/and integer? pos?))
(s/def ::two keyword?)
(s/def ::three string?)
(s/def ::fooish (s/keys :opt-un [::one ::two ::three]))
(defstrukt foo ::fooish :two {:one 42 :two :fourty-two})

(deftest defstrukt

  (testing "constructed with no arguments"
    (should "return default values + type"
      (foo)
      {:type :strukt.core-test/foo :one 42 :two :fourty-two}))

  (testing "constructed with a key and a value"
    (should "return values + type"
      (foo :one 89)
      {:type :strukt.core-test/foo :one 89 :two :fourty-two})
    (testing "with invalid value"
      (is (thrown-with-msg? js/TypeError
                            #"invalid shape \'foo \:one\', expected \-89 to be pos\? via \:strukt\.core\-test/fooish > \:strukt\.core\-test/one"
                            (foo :one -89)))))

  (testing "constructed with multiple keys and values"
    (should "return values + type"
      (foo :one 89 :two :foobar)
      {:type :strukt.core-test/foo :one 89 :two :foobar})
    (testing "with invalid value"
      (is (thrown-with-msg? js/TypeError
                            #"invalid shape \'foo \:two\', expected \"foobar\" to be keyword\? via \:strukt\.core\-test/fooish > \:strukt\.core\-test/two"
                            (foo :one 89 :two "foobar")))))

  (testing "constructed with primary argument"
    (should "return default values + type + primary value"
      (foo :bleep)
      {:type :strukt.core-test/foo :one 42 :two :bleep})
    (testing "with invalid value"
      (is (thrown-with-msg? js/TypeError
                            #"invalid shape \'foo \:two\', expected 12 to be keyword\? via \:strukt\.core\-test/fooish > \:strukt\.core\-test/two"
                            (foo 12)))))

  (testing "constructed with a map of keys and values"
    (should "return values + type"
      (foo {:one 64 :three "drei"})
      {:type :strukt.core-test/foo :one 64 :two :fourty-two :three "drei"})
    (testing "with invalid value"
      (is (thrown-with-msg? js/TypeError
                            #"invalid shape \'foo \:three\', expected \-89 to be string\? via \:strukt\.core\-test/fooish > \:strukt\.core\-test/three"
                            (foo {:one 64 :three -89}))))))

(deftest valid?
  (let [m (foo)]
    (testing "with valid values"
      (should "return true" true (strukt/valid? m)))
    (testing "with invalid value"
      (should "return false" false (strukt/valid? (assoc m :one -64))))))

(deftest valid!
  (let [m (foo)]
    (testing "with valid values"
      (should "return the strukt" m (strukt/valid! m)))
    (testing "with invalid value"
      (is (thrown-with-msg? js/TypeError
                            #"invalid shape \'foo \:one\', expected \-64 to be pos\? via \:strukt\.core\-test/fooish > \:strukt\.core\-test/one"
                            (strukt/valid! (assoc m :one -64)))))))

(deftest ->strukt
  (let [m (foo)]
    (testing "attach meta data"
      (should "attach :name"
        "foo" (-> m meta :name))
      (should "attach :type"
        ::foo (-> m meta :type))
      (should "attach :spec"
        ::fooish (-> m meta :spec)))))
