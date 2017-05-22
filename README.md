# strukt

ClojureScript map factory with shape validation using [prost](https://github.com/adamrenklint/prost) and cljs.spec

[![CircleCI](https://circleci.com/gh/adamrenklint/strukt.svg?style=svg)](https://circleci.com/gh/adamrenklint/strukt)

```clojure
[adamrenklint/strukt "1.1.0"] ;; latest release
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

#### `defstrukt [name spec primary-key defaults] => defn => m`<br><br>`defstrukt [name type spec primary-key defaults] => defn => m`

Define a strukt map factory as fn *name* in current namespace. *type* is optionally a keyword, or the *name* will be keywordized and used. *spec* is a keyword pointing to a spec definiton in the spec registry. *primary-key* is a keyword that represents the main field on the strukt, and will be used for the shorthand primary-key value factory. *defaults* is a map of default values.

#### `strukt* [name type spec primary-key defaults] => fn => m`

Create a strukt map factory with *name*. *type* is a keyword and will be used to identify the type of strukt. *spec* is a keyword pointing to a spec definiton in the spec registry. *primary-key* is a keyword that represents the main field on the strukt, and will be used for the shorthand primary-key value factory. *defaults* is a map of default values.

Returns a strukt map factory fn that can be called with no arguments, a single value to associated with *primary-key*, a map of keys and values, or variadic keyword arguments.

### Functions

#### `valid? [m] => bool`

Check if map *m* conforms to its spec, which is attached as meta data. Returns true or false.

#### `valid! [m] => m | nil`

Check if map *m* conforms to its spec, which is attached as meta data. Returns the map if true, and throws an error if false.

#### `->strukt [name spec m] => m`

Attach *name* and *spec* to map strukt *m* as meta data.

## Develop

- `boot test` - run tests
- `boot watch-test` - watch and run tests
- `boot fmt` - format source with cljfmt
- `boot release` - build and push release to Clojars

## License

Copyright (c) 2017 [Adam Renklint](http://adamrenklint.com)

Distributed under the [MIT license](https://github.com/adamrenklint/strukt/blob/master/LICENSE)
