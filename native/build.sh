#!/usr/bin/env bash

# This script needs to be called from the native folder cuz all paths are assumed to be relative

set -Eeuo pipefail

readonly OPTION_DEBUG="-d"
DEBUG=false
BUILD_FOLDER=""

function create_folders() {
  BUILD_FOLDER="$(pwd)/target"

  cd ../sample-app/src/main/jniLibs || exit

  mkdir -p arm64-v8a
  mkdir -p armeabi-v7a
  mkdir -p x86
  mkdir -p x86_64
}

function create_lib_folders() {

    cd ../rasp/src/main/jniLibs || exit

    mkdir -p arm64-v8a
    mkdir -p armeabi-v7a
    mkdir -p x86
    mkdir -p x86_64
}

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    "$OPTION_DEBUG")
        DEBUG=true
        shift 1
        ;;
    *)
        shift
        ;;
esac
done

if [[ "$DEBUG" == true ]]
then
    cargo build  --target aarch64-linux-android
    cargo build  --target armv7-linux-androideabi
    cargo build  --target i686-linux-android
    cargo build  --target x86_64-linux-android
    create_folders

    cp -fr "$BUILD_FOLDER/aarch64-linux-android/debug/libnative.so" "arm64-v8a/libnative.so"
    cp -fr "$BUILD_FOLDER/armv7-linux-androideabi/debug/libnative.so" "armeabi-v7a/libnative.so"
    cp -fr "$BUILD_FOLDER/i686-linux-android/debug/libnative.so" "x86/libnative.so"
    cp -fr "$BUILD_FOLDER/x86_64-linux-android/debug/libnative.so" "x86_64/libnative.so"

else
  cargo build  --target aarch64-linux-android --release
  cargo build  --target armv7-linux-androideabi --release
  cargo build  --target i686-linux-android --release
  cargo build  --target x86_64-linux-android --release

  create_folders

  cp -fr "$BUILD_FOLDER/aarch64-linux-android/release/libnative.so" "arm64-v8a/libnative.so"
  cp -fr "$BUILD_FOLDER/armv7-linux-androideabi/release/libnative.so" "armeabi-v7a/libnative.so"
  cp -fr "$BUILD_FOLDER/i686-linux-android/release/libnative.so" "x86/libnative.so"
  cp -fr "$BUILD_FOLDER/x86_64-linux-android/release/libnative.so" "x86_64/libnative.so"

  cd -
  create_lib_folders

  cp -fr "$BUILD_FOLDER/aarch64-linux-android/release/libnative.so" "arm64-v8a/libnative.so"
  cp -fr "$BUILD_FOLDER/armv7-linux-androideabi/release/libnative.so" "armeabi-v7a/libnative.so"
  cp -fr "$BUILD_FOLDER/i686-linux-android/release/libnative.so" "x86/libnative.so"
  cp -fr "$BUILD_FOLDER/x86_64-linux-android/release/libnative.so" "x86_64/libnative.so"

fi
