package com.securevale.rasp.android.native

/**
 * Library initalisation object.
 */
object SecureApp {

    init {
        System.loadLibrary("native")
    }

    /**
     * Method that needs to be called for library initalisation.
     * It should be called only once par app's lifetime.
     * Recommended place to call it is app's Application class.
     */
    fun init() {
        initJni()
    }

    private external fun initJni()
}
