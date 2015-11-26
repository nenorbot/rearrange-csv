(ns rearrange-csv.core
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [semantic-csv.core :refer :all]))

(defn read-csv [f encoding]
  (with-open [r (io/reader f :encoding encoding)]
    (->>
     (csv/read-csv r)
     mappify
     doall)))

(defn fill-new-order [header order]
  (concat order (filter (comp not (set order))
                        header)))

(defn rearrange [f encoding order]
  (let [data (read-csv f encoding)
        header (keys (first data))
        order (fill-new-order header (map keyword order))]
    (with-open [out (io/writer (str f ".fixed.csv"))]
      (csv/write-csv out (vectorize {:header order} data)))))

(defn -main [f encoding & order]
  (rearrange f encoding order))






