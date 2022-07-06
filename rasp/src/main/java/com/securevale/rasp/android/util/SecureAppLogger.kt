package com.securevale.rasp.android.util

import android.util.Log

/**
 * Flag indicates whether library was initialised in debug mode.
 */
internal var isDebug = false

/**
 * An object used for logging.
 */
object SecureAppLogger {

    /**
     * Logs debug.
     * @param message message to be logged.
     */
    fun logDebug(message: String) {
        if (isDebug) {
            Log.d(TAG, message)
        }
    }

    /**
     * Logs error.
     * @param error error message to be logged.
     * @param throwable the throwable that might be logged alongside with error message.
     */
    fun logError(error: String, throwable: Throwable? = null) {
        if (isDebug) {
            Log.e(TAG, error, throwable)
        }
    }

    /**
     * Logging tag.
     */
    private const val TAG = "safe-app"
}
