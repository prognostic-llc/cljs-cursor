(ns cursor.core
  (:require [cursor.impl :refer [deref* invoke* swap!* reset!* cursor* hash*]]))


(declare ->Cursor)


(deftype Cursor [value swap-fn! all-segments store]
  IDeref
  (-deref [_]
    (deref* value))

  IFn
  (-invoke [_ segments]
    (invoke* ->Cursor value swap-fn! segments all-segments store))
  (-invoke [_ segments not-found]
    (invoke* ->Cursor value swap-fn! segments all-segments store :not-found not-found))
  (-invoke [_ segments not-found invalid?]
    (invoke* ->Cursor value swap-fn! segments all-segments store :not-found not-found :invalid? invalid?))

  ISwap
  (-swap! [_ f]
    (swap!* swap-fn! f))
  (-swap! [_ f x]
    (swap!* swap-fn! f x))
  (-swap! [_ f x y]
    (swap!* swap-fn! f x y))
  (-swap! [_ f x y args]
    (apply swap!* swap-fn! f x y args))

  IReset
  (-reset! [o v]
    (reset!* o v))

  IHash
  (-hash [_]
    (hash* value all-segments store))

  IEquiv
  (-equiv [o o']
    (and (instance? Cursor o') (= (hash o) (hash o')))))


(defn cursor [store] (cursor* ->Cursor store))
