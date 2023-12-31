(ns examples.loadcsv
  (:require
    [clojure.string :as str]
    [io.github.humbleui.core :as core]
    [io.github.humbleui.paint :as paint]
    [io.github.humbleui.typeface :as typeface]
    [io.github.humbleui.font :as font]
    [io.github.humbleui.ui :as ui])
  (:import
    [io.github.humbleui.skija Color ColorFilter ColorMatrix FilterTileMode ImageFilter])
  (:use
    [clojure-csv.core]))

; Prepare the thing
(def counter (atom 3))
;(def csv-data [["date" "H" "TA" "HR" "VV" "RS" "PR"] ["12/1/2019" "0:00" "12:00:00" "11.7" "64" "4.32" "0" "5.2"] ["12/1/2019" "0:00" "12:02:00" "11.7" "64" "2.5" "0" "5.2"]]) 
(defn isit [v arg] (.indexOf v arg))
(defn printif [v cond size] (do (swap! counter dec) (when (> cond -1) (println [(- size @counter) cond]))))

(defn find-index [data value]
  ; Run the thing
  (let [size (count data)]
    (def counter (atom size))
    ;(first (map #(printif data % (- size 1)) (map #(isit % value) data)))
    (map #(printif data % (- size 1)) (map #(isit % value) data))
  ))

(defn open-file [file-name]
  ; Attempts to open a file and complains if the file is not present.
  (let [file-data (try 
                 (slurp (clojure.string/replace file-name "\"" ""))
                 (catch Exception e (println (.getMessage e))))]
    file-data))

(defn ret-csv-data [fnam]
; Returns a lazy sequence generated by parse-csv.
; Uses open-file which will return a nil, if
; there is an exception in opening fnam.
;
; parse-csv called on non-nil file, and that
; data is returned.
  (let [csv-file (open-file fnam)
    inter-csv-data (if-not (nil? csv-file)
                    (parse-csv csv-file :delimiter \;)
                    nil)

    csv-data 
      (vec (filter #(and pos? (count %) 
          (not (nil? (rest %)))) inter-csv-data))]

    (if-not (empty? csv-data)
      (pop csv-data)
       nil)))

(defn fetch-csv-data [csv-file]
  ; This function accepts a csv file name, and returns parsed csv data,
  ; or returns nil if file is not present.
  (let [csv-data (ret-csv-data csv-file)]
    csv-data))

(defn parameter [name value unit]
  (ui/row 
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/label name)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/text-field value)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/label unit)))))]))

(defn RenderParams [params]
  (for [param params] (parameter (param :name) (param :value) (param :unit)))
)

(def *fileName 
  (atom {:text "\"C:\\Users\\lross\\Documents\\test-data.csv\""}))

(def color-digit 0xFF797979)

(defn handle-csv [file-name]
  (let [csv-data (fetch-csv-data file-name)]
    ;(println csv-data)
    ;(println (find-index csv-data "HR"))
    ;(println (find-index csv-data "12/3/2019"))
    (println (find-index csv-data "12/1/2019"))
    (println (find-index csv-data "11.7"))
    ))

(defn button [text color]
  (ui/clickable
    {:on-click (fn [_] (handle-csv (@*fileName :text)))}
    (ui/dynamic ctx [{:keys [hui/active? font-btn]} ctx]
      (let [color' (if active?
                     (bit-or 0x80000000 (bit-and 0xFFFFFF color))
                     color)]
        (ui/rect (paint/fill color')
          (ui/center
            (ui/label {:font font-btn :features ["tnum"]} text)))))))

(def ui
  (ui/center
  (ui/focus-controller
      (ui/with-context
        {:hui.text-field/padding-top    10
         :hui.text-field/padding-bottom 10
         :hui.text-field/padding-left   5
         :hui.text-field/padding-right  5}
      (ui/column
        (ui/label "Archivo")
        [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/text-field *fileName)))))]
        (button "Print" color-digit)
        )))))
