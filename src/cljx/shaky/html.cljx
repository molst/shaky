(ns shaky.html)

(defn unescape "Change special characters into HTML character entities."
  [string] (.. string
               (replace "&amp;" "&")
               (replace "&lt;" "<")
               (replace "&gt;" ">" )
               (replace "&quot;" "\"")))
