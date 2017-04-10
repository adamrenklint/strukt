(ns strukt.core
  (:require prost.core))

(defmacro strukt*
  [name type spec primary defaults]
  `(fn ~name
     ([]
      (prost.core/shape! ~(str name) ~spec
                                   (assoc ~defaults :type ~type)))
     ([m#]
      (prost.core/shape! ~(str name) ~spec
                                   (if (map? m#)
                                     (assoc (merge ~defaults m#) :type ~type)
                                     (assoc ~defaults ~primary m# :type ~type))))
     ([k# v#]
      (prost.core/shape! ~(str name) ~spec
                                   (assoc ~defaults k# v# :type ~type)))
     ([k# v# & kvs#]
      (prost.core/shape! ~(str name) ~spec
                                   (~name (apply (partial hash-map k# v#) kvs#))))))

(defmacro defstrukt
  "Define a strukt map factory as fn *name* in current namespace. *type* is optionally a keyword, or the *name* will be keywordized and used. *spec* is a keyword pointing to a spec definiton in the spec registry. *primary* is a keyword that represents the main field on the strukt, and will be used for the shorthand primary value factory. *defaults* is a map of default values."
  ([name spec primary defaults]
   `(strukt.core/defstrukt ~name ~(keyword (str *ns*) (str name)) ~spec ~primary ~defaults))
  ([name type spec primary defaults]
   `(def ~name (strukt.core/strukt* ~name ~type ~spec ~primary ~defaults))))
