(ns examples.ross
  (:require
    [clojure.string :as str]
    [io.github.humbleui.core :as core]
    [io.github.humbleui.typeface :as typeface]
    [io.github.humbleui.font :as font]
    [io.github.humbleui.ui :as ui]))

(def face-bold
  (typeface/make-from-resource "io/github/humbleui/fonts/Inter-Bold.ttf"))

;(defn text-field [text & {:keys [from to placeholder cursor-blink-interval cursor-width padding-h padding-v padding-top padding-bottom border-radius]
;                          :or {cursor-blink-interval 500, cursor-width 1, padding-h 0, padding-v 3, border-radius 4}
;                          :as opts}]
;  (ui/with-context
;    {:hui.text-field/cursor-blink-interval cursor-blink-interval
;     :hui.text-field/cursor-width          cursor-width
;     :hui.text-field/border-radius         border-radius
;     :hui.text-field/padding-top           (float (or padding-top padding-v))
;     :hui.text-field/padding-bottom        (float (or padding-bottom padding-v)) :hui.text-field/padding-left          (float padding-h)
;     :hui.text-field/padding-right         (float padding-h)}
;    (ui/text-field opts
;      (atom
;        {:text        text
;         :placeholder placeholder
;         :from        from
;         :to          to}))))

(def *celsius
  (atom {:text "5"
         :from 1
         :to   1}))

(def *fahrenheit
  (atom {:text "41"}))

(def *answer
  (atom {:text "42"}))

(def ^:dynamic *editing* false)

(add-watch *celsius ::update
  (fn [_ _ old new]
    (when-not *editing*
      (when (not= (:text old) (:text new))
        (binding [*editing* true]
          )))))

;(add-watch *fahrenheit ::update
;  (fn [_ _ old new]
;    (when-not *editing*
;      (when (not= (:text old) (:text new))
;        (binding [*editing* true]
;          (if-some [f (parse-long (str/trim (:text new)))]
;            (let [c (-> f (- 32) (* 5) (quot 9) str)]
;              (swap! *celsius assoc
;                :text c
;                :from (count c)
;                :to   (count c)))
;            (swap! *celsius assoc
;              :text ""
;              :from 0
;              :to   0)))))))

(add-watch *fahrenheit ::update
  (fn [_ _ old new]
    (when-not *editing*
      (when (not= (:text old) (:text new))
        (binding [*editing* true]
          )))))

(defonce *clicks (atom 0))

(def ui
  (ui/focus-controller
      (ui/with-context
        {:hui.text-field/padding-top    10
         :hui.text-field/padding-bottom 10
         :hui.text-field/padding-left   5
         :hui.text-field/padding-right  5}
  (ui/grid 
    [[
      (ui/label "1")
      (ui/label "2")
      (ui/label "3")
      ]
      [
      (ui/width 100
        (ui/text-field *fahrenheit))
      (ui/width 100
        (ui/text-field *celsius))
      (ui/width 100
        (ui/text-field *answer ))
      ]
      [
      (ui/width 100
        (ui/text-field *fahrenheit))
      (ui/width 100
        (ui/text-field *celsius))
      (ui/width 100
        (ui/text-field *answer ))
      ]
      [
      (ui/width 100
        (ui/text-field *fahrenheit))
      (ui/width 100
        (ui/text-field *celsius))
      (ui/width 100
        (ui/text-field *answer ))
      ]
      [
      (ui/button #(swap! *clicks inc)
        (ui/label "Increment"))
      (ui/button #(swap! *clicks dec)
        (ui/label "Decrement"))
      (ui/dynamic _ [clicks @*clicks]
        (ui/label (str clicks)))
      ]
      [
      (ui/button #(swap! *celsius assoc
        :text "56")
        (ui/label "castTo56"))
      (ui/button #(swap! *celsius assoc
        :text (str (@*fahrenheit :text)) )
        (ui/label "fahrah"))
      ]
     ])
     )))

        ;(ui/text-field {:focused (core/now)} *celsius))