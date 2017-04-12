# strukt

ClojureScript map factory with shape validation using [prost](https://github.com/adamrenklint/prost) and cljs.spec

[![CircleCI](https://circleci.com/gh/adamrenklint/strukt.svg?style=svg)](https://circleci.com/gh/adamrenklint/strukt)

```clojure
[adamrenklint/strukt "1.0.0"] ;; latest release
```

## Usage

```clojure
(ns strukt.example
  (:require [strukt.core :refer-macros [defstrukt] :refer [valid? valid!]]))

; Create a spec to validate the shape
(s/def ::color keyword?)
(s/def ::size (s/and number? pos?))
(s/def ::hat (s/keys :req-un [::color ::radius]))

; Define the strukt factory
(defstrukt hat ::hat ::size {:color :red :size 5})

; Create a strukt with valid values
(hat) ; => {:type :hat :size 5 :color :red}

; The factory function takes many different forms of arguments to create a new strukt map
(hat 12) ; => {:type :hat :size 12 :color :red}
(hat :color :blue) ; => {:type :hat :size 5 :color :blue}
(hat {:color :blue}) ; => {:type :hat :size 5 :color :blue}

; Try creating a strukt with invalid values
(hat :size :big) ; => invalid shape 'hat :size', expected :big to be number? via :strukt.example/hat > :strukt.example/size

; A strukt is just a map with its name and spec attached as meta data
(def my-hat (hat))
(-> my-hat meta :name) ; => "hat"
(-> my-hat meta :spec) ; => ::hat

; Having the spec attached means that we can easily call validation methods, without having to pass the spec all the time
(valid? my-hat) ; => true
(valid? (assoc my-hat :size :big)) ; => false
(valid! my-hat) ; => {:type :hat :size 5 :color :red}
(valid! (assoc my-hat :size :big)) ; => invalid shape 'hat :size', expected :big to be number? via :strukt.example/hat > :strukt.example/size
```

## API

### Macros

- defstrukt [name spec primary-key defaults] => defn => strukt
- defstrukt [name type spec primary-key defaults] => defn => strukt
- strukt* [name type spec primary-key defaults] => fn => strukt

### Functions

- valid? [strukt] => bool
- valid! [strukt] => strukt | throw error

## Develop

- `boot test`
- `boot watch-test`
- `boot fmt`
- `boot release`

## License

Copyright (c) 2017 [Adam Renklint](http://adamrenklint.com)

Distributed under the [MIT license](https://github.com/adamrenklint/strukt/blob/master/LICENSE)
