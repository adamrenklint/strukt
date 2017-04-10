# strukt

ClojureScript map factory with shape validation using [prost](https://github.com/adamrenklint/prost) and cljs.spec

[![CircleCI](https://circleci.com/gh/adamrenklint/strukt.svg?style=svg)](https://circleci.com/gh/adamrenklint/strukt)

```clojure
[adamrenklint/strukt "1.0.0"] ;; latest release
```

## Usage

```clojure
; Import the 'defstrukt' macro
(ns strukt.example
  (:require [strukt.core :refer-macros [defstrukt]]))

; Create a spec to validate the shape
(s/def ::color keyword?)
(s/def ::size (s/and number? pos?))
(s/def ::hat (s/keys :req-un [::color ::radius]))

; Define the strukt factory
(defstrukt hat ::hat ::size {:color :red :size 5})

; Create a strukt with valid values
(hat) ; => {:type :hat :size 5 :color :red}

; Try creating a strukt with invalid values
(hat :size :big) ; => invalid shape 'hat :size', expected :big to be number? via :strukt.example/hat > :strukt.example/size
```

## API

- defstrukt [name spec primary-key default-attrs] => fn => strukt
- defstrukt [name type spec primary-key default-attrs] => fn => strukt

## Develop

- `boot test`
- `boot watch-test`
- `boot fmt`
- `boot release`

## License

Copyright (c) 2017 [Adam Renklint](http://adamrenklint.com)

Distributed under the [MIT license](https://github.com/adamrenklint/strukt/blob/master/LICENSE)
