package com.securevale.rasp.android.native

object SecureApp {

    init {
        System.loadLibrary("native")
    }

    fun init() {
        initJni()
    }

    // This method should be called only once per app's lifetime
    private external fun initJni()
}
