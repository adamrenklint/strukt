(ns strukt.core
  (:require [prost.core :as prost :refer-macros [shape!]]))

(defn valid?
  "Check if map *m* conforms to its spec, which is attached as meta data. Returns true or false."
  [m]
  (let [{:keys [name spec]} (meta m)]
    (prost/valid? spec m)))

(defn valid!
  "Check if map *m* conforms to its spec, which is attached as meta data. Returns the map if true, and throws an error if false."
  [m]
  (let [{:keys [name spec]} (meta m)]
    (shape! name spec m)))

(defn ->strukt
  "Attach *name* and *spec* to map strukt *m* as meta data."
  [name spec m]
  (let [s (with-meta m {:name name :type (:type m) :spec spec})]
    (valid! s)))
