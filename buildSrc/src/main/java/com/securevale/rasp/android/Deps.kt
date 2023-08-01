package com.securevale.rasp.android

object Deps {
    const val versionCode = 3
    const val versionName = "0.3.0"

    const val minSDKVersion = 23
    const val targetSDKVersion = 33
    const val compileSDKVersion = 33

    object Android {
        private const val coreCtxVersion = "1.8.0"
        private const val appcompatVersion = "1.4.2"
        private const val materialVersion = "1.6.1"
        private const val constraintLayoutVersion = "2.1.4"

        const val coreKtx = "androidx.core:core-ktx:$coreCtxVersion"
        const val appcompat = "androidx.appcompat:appcompat:$appcompatVersion"
        const val material = "com.google.android.material:material:$materialVersion"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    }

    object Debug {
        private const val leakCanaryVersion = "2.9.1"

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
    }
}
