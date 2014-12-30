(ns caniche.crypto
  (:require [crypto.random]))

;; TODO difference between require and import
(import (javax.crypto Cipher KeyGenerator SecretKey)
        (javax.crypto.spec SecretKeySpec)
        (java.security SecureRandom)
        (org.apache.commons.codec.binary Base64))

;; Encryption functions taken from http://stackoverflow.com/questions/10221257/is-there-an-aes-library-for-clojure

(defn- secret-key [size]
  (crypto.random/base32 size))

(defn- string-to-bytes [s]
  (.getBytes s "UTF-8"))

(defn- vec-to-bytes [arr]
  (byte-array (map byte arr)))

(defn- base64 [b]
  (Base64/encodeBase64String b))

(defn- debase64 [s]
  (Base64/decodeBase64 (string-to-bytes s)))

(defn- get-raw-key [seed]
  (let [keygen (KeyGenerator/getInstance "AES")
        sr (SecureRandom/getInstance "SHA1PRNG")]
    (.setSeed sr (string-to-bytes seed))
    (.init keygen 128 sr)
    (.. keygen generateKey getEncoded)))

(defn- get-cipher [mode seed]
  (let [key-spec (SecretKeySpec. (get-raw-key seed) "AES")
        cipher (Cipher/getInstance "AES")]
    (.init cipher mode key-spec)
    cipher))

(defn encrypt [byte-vec key]
  (let [cipher (get-cipher Cipher/ENCRYPT_MODE key)
        arr (vec-to-bytes byte-vec)]
    (base64 (.doFinal cipher arr))))

(defn decrypt [text key]
  (let [cipher (get-cipher Cipher/DECRYPT_MODE key)]
    (.doFinal cipher (debase64 text))))
