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

(defn blur [radius]
  (ImageFilter/makeBlur radius radius FilterTileMode/CLAMP))

(def face-bold
  (typeface/make-from-resource "io/github/humbleui/fonts/Inter-Bold.ttf"))

(def *celsius
  (atom {:text "5"
         :from 1
         :to   1}))

(def *fahrenheit
  (atom {:text "41"}))

(def *component
  (atom {:text "2129"}))

(def *answer
  (atom {:text "42"}))

(def ^:dynamic *editing* false)

(add-watch *celsius ::update
  (fn [_ _ old new]
    (when-not *editing*
      (when (not= (:text old) (:text new))
        (binding [*editing* true]
          )))))

(add-watch *fahrenheit ::update
  (fn [_ _ old new]
    (when-not *editing*
      (when (not= (:text old) (:text new))
        (binding [*editing* true]
          )))))

(defonce *clicks (atom 0))

(defn simple [name value unit]
  (ui/grid
    [[(ui/label name)
      (ui/text-field value)
      (ui/label unit)]]))

(def solar {:name "Constante Solar", :value *answer, :unit "MJ/m²"})

(defn RenderParams [params]
  (for [param params] (simple (param :name) (param :value) (param :unit)))
)

(defn parameter [name filter color value unit]
    (ui/clip-rrect 20
      (ui/backdrop filter
        (ui/stack
          (ui/rect (paint/fill color)
            (ui/gap 100 100))
          (ui/center
        (ui/grid [
              [(ui/label name)]
              [;(ui/gap 0 10)
              (ui/text-field value)
              (ui/label unit)]])
              ;(ui/label (format "Fill: #%02X%02X%02X" r g b))
              ;(ui/gap 0 10)
              ;(ui/label (format "Opacity: %d%%" (math/round (/ a 2.55))))
              )))))

(def ui
  (ui/center
  (ui/focus-controller
      (ui/with-context
        {:hui.text-field/padding-top    10
         :hui.text-field/padding-bottom 10
         :hui.text-field/padding-left   5
         :hui.text-field/padding-right  5}
      (ui/column
        ;(parameter "Altura" (blur 20) 0x40CC3333 *component "msnm")
        (simple "Altura" *component "msnm")
        (first (RenderParams [solar]))
      )
     ))))
