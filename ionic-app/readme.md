
## 1. Build the apk

- Run : `ionic cordova build --release android`

## 2. Build a keystore

- Run : `keytool -genkey -v -keystore gallery.keystore -alias gallery_app -keyalg RSA -keysize 2048 -validity 10000` 
- The password of the generated one is `password`.

## 3. Sign apk

- Run : `jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore gallery.keystore platforms/android/app/build/outputs/apk/release/app-release-unsigned.apk gallery_app`

For all the rest, it's an Android Application... :)
