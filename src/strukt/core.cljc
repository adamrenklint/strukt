(ns strukt.core)

(defmacro strukt*
  [name type spec primary-key defaults]
  `(fn ~name
     ([]
      (->strukt ~(str name) ~spec (assoc ~defaults :type ~type)))
     ([m#]
      (->strukt ~(str name) ~spec
                (if (map? m#)
                  (assoc (merge ~defaults m#) :type ~type)
                  (assoc ~defaults ~primary-key m# :type ~type))))
     ([k# v#]
      (->strukt ~(str name) ~spec (assoc ~defaults k# v# :type ~type)))
     ([k# v# & kvs#]
      (->strukt ~(str name) ~spec
                (~name (apply (partial hash-map k# v#) kvs#))))))

(defmacro defstrukt
  "Define a strukt map factory as fn *name* in current namespace. *type* is optionally a keyword, or the *name* will be keywordized and used. *spec* is a keyword pointing to a spec definiton in the spec registry. *primary-key* is a keyword that represents the main field on the strukt, and will be used for the shorthand primary-key value factory. *defaults* is a map of default values."
  ([name spec primary-key defaults]
   `(strukt.core/defstrukt ~name ~(keyword (str *ns*) (str name)) ~spec ~primary-key ~defaults))
  ([name type spec primary-key defaults]
   `(def ~name (strukt.core/strukt* ~name ~type ~spec ~primary-key ~defaults))))
