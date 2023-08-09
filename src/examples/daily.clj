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
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 100 (ui/label name)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 100 (ui/text-field value)))))]
    [:stretch 1 (ui/rect (paint/fill 0xFFB2D7FE) (ui/center (ui/padding 10 10 (ui/width 100 (ui/label unit)))))]))

(defn RenderParams [params]
  (for [param params] (parameter (param :name) (param :value) (param :unit)))
)

(def *valHeight
  (atom {:text "2129"}))
(def height {:name "Altura", :value *valHeight, :unit "msnm"})

(def *valSolar
  (atom {:text "42"}))
(def solar {:name "Constante Solar", :value *valSolar, :unit "MJ/m²"})

(def ui
  (ui/center
  (ui/focus-controller
      (ui/with-context
        {:hui.text-field/padding-top    10
         :hui.text-field/padding-bottom 10
         :hui.text-field/padding-left   5
         :hui.text-field/padding-right  5}
      (ui/column
        (RenderParams [height solar]))))))
