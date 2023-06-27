package com.securevale.rasp.android.native

/**
 * The object for library initalisation.
 */
object SecureApp {

    init {
        System.loadLibrary("native")
    }

    /**
     * Method which needs to be called to correctly initialise the library.
     * It should be called only once par app's lifetime.
     * Best way to call it is app's Application class.
     */
    fun init() {
        initJni()
    }

    private external fun initJni()
}
