(ns dev.main
  (:require
   [clojure.core.server :as server]
   [clojure.java.io :as io]
   [examples.ross]
   [examples.daily]
   [examples.align]
   [examples.backdrop]
   [examples.calculator]
   [examples.canvas]
   [examples.canvas-shapes]
   [examples.checkbox]
   [examples.container]
   [examples.effects]
   [examples.errors]
   [examples.event-bubbling]
   [examples.image-snapshot]
   [examples.label]
   [examples.paragraph]
   [examples.scroll]
   [examples.settings]
   [examples.state :as state]
   [examples.stack]
   [examples.text-field-debug]
   [examples.todomvc]
   [examples.toggle]
   [examples.tooltip]
   [examples.wordle]
   [io.github.humbleui.app :as app]
   [io.github.humbleui.debug :as debug]
   [io.github.humbleui.paint :as paint]
   [io.github.humbleui.window :as window]
   [io.github.humbleui.ui :as ui])
   (:gen-class))

(defn set-floating! [window floating]
  (when window
    (if floating
      (window/set-z-order window :floating)
      (window/set-z-order window :normal))))

(add-watch state/*floating ::window
  (fn [_ _ _ floating]
    (set-floating! @state/*window floating)))

(def examples
  (sorted-map
    "ARoss" examples.ross/ui
    "Diario" examples.daily/ui
    ;"Align" examples.align/ui
    ;"Backdrop" examples.backdrop/ui
    ;"Calculator" examples.calculator/ui
    ;"Canvas" examples.canvas/ui
    ;"Canvas Shapes" examples.canvas-shapes/ui
    ;"Checkbox" examples.checkbox/ui
    ;"Container" examples.container/ui
    ;"Effects" examples.effects/ui
    ;"Errors" examples.errors/ui
    ;"Event Bubbling" examples.event-bubbling/ui
    ;"Image Snapshot" examples.image-snapshot/ui
    ;"Label" examples.label/ui
    ;"Paragraph" examples.paragraph/ui
    ;"Scroll" examples.scroll/ui
    ;"Settings" examples.settings/ui
    ;"Stack" examples.stack/ui
    ;"Text Field Debug" examples.text-field-debug/ui
    ;"Todo MVC" examples.todomvc/ui
    ;"Toggle" examples.toggle/ui
    ;"Tooltip" examples.tooltip/ui
    ;"Wordle" examples.wordle/ui
    ))

(def light-grey
  0xffeeeeee)   

(def border-line
  (ui/rect (paint/fill light-grey)
    (ui/gap 1 0)))

(def app
  (ui/default-theme {}; :font-size 13
    ; :cap-height 10
    ; :leading 100
    ; :fill-text (paint/fill 0xFFCC3333)
    ; :hui.text-field/fill-text (paint/fill 0xFFCC3333)
    (ui/row
      (ui/vscrollbar
        (ui/column
          ;(for [[name _] (sort-by first examples)]
          (for [[name _] examples]
            (ui/clickable
              {:on-click (fn [_] (reset! state/*example name))}
              (ui/dynamic ctx [selected? (= name @state/*example)
                               hovered?  (:hui/hovered? ctx)]
                (let [label (ui/padding 20 10
                              (ui/label name))]
                  (cond
                    selected? (ui/rect (paint/fill 0xFFB2D7FE) label)
                    hovered?  (ui/rect (paint/fill 0xFFE1EFFA) label)
                    :else     label)))))))
      border-line
      [:stretch 1
       (ui/clip
         (ui/dynamic _ [name @state/*example]
           (examples name)))])))

(reset! state/*app app)

(defn -main [& args]
  (ui/start-app!
    (let [screen (app/primary-screen)]
      (reset! state/*window 
        (ui/window
          {:title    "Evapotranspiración"
           :mac-icon (if (io/resource "resources/images/icon.icns")
                       (slurp (io/resource "resources/images/icon.icns"))
                       "resources/images/icon.icns"
                     )
           :screen   (:id screen)
           :width    600
           :height   600
           :x        :center
           :y        :center}
          state/*app))))
  (set-floating! @state/*window @state/*floating)
  #_(reset! protocols/*debug? true)
  (let [{port "--port"
         :or {port "5555"}} (apply array-map args)
        port (parse-long port)]
    (println "Started Server Socket REPL on port" port)
    (server/start-server
      {:name          "repl"
       :port          port
       :accept        'clojure.core.server/repl
       :server-daemon false})))
