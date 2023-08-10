(ns examples.daily
  (:require
    [clojure.string :as str]
    [io.github.humbleui.core :as core]
    [io.github.humbleui.paint :as paint]
    [io.github.humbleui.typeface :as typeface]
    [io.github.humbleui.font :as font]
    [io.github.humbleui.ui :as ui])
  (:import
    [io.github.humbleui.skija Color ColorFilter ColorMatrix FilterTileMode ImageFilter]))

(defn parameter [name value unit]
  (ui/row 
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/label name)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/text-field value)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 125 (ui/label unit)))))]))

(defn RenderParams [params]
  (for [param params] (parameter (param :name) (param :value) (param :unit)))
)

(def *valHeight
  (atom {:text "2129"}))
(def height {:name "Altura", :value *valHeight, :unit "msnm"})

(def *valSolar
  (atom {:text "0,082"}))
(def solar {:name "Constante Solar", :value *valSolar, :unit "MJ/m²"})

(def *valAlbedo
  (atom {:text "0,23"}))
(def albedo {:name "Albedo", :value *valAlbedo, :unit "-"})

(def *valAtm
  (atom {:text "78,54"}))
(def atm {:name "Presión Atmosférica", :value *valAtm, :unit "kPa"})

(def *valMeassureDist
  (atom {:text "6,50"}))
(def meassureDist {:name "Altura de medición", :value *valMeassureDist, :unit "m"})

(def *valCapCaloric
  (atom {:text "2,10"}))
(def capCaloric {:name "Capacidad calorífica", :value *valCapCaloric, :unit "MJ m-3 °C-1"})

(def *valSoilDepth
  (atom {:text "0,10"}))
(def soilDepth {:name "Δz profundida suelo", :value *valSoilDepth, :unit "m"})

(def *valInitialDay
  (atom {:text "0"}))
(def initDay {:name "Día inicial", :value *valInitialDay, :unit "-"})

;(def *val
;  (atom {:text ""}))
;(def solar {:name "", :value *val, :unit ""})

(def ui
  (ui/center
  (ui/focus-controller
      (ui/with-context
        {:hui.text-field/padding-top    10
         :hui.text-field/padding-bottom 10
         :hui.text-field/padding-left   5
         :hui.text-field/padding-right  5}
      (ui/column
        (ui/label "Estación")
        (RenderParams [height albedo solar atm meassureDist])
        (ui/label "Localización")
        (RenderParams [capCaloric soilDepth initDay]))))))
