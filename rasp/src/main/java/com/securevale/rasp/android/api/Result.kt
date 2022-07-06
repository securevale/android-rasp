package com.securevale.rasp.android.api

/**
 * The result of the vulnerability check.
 */
sealed class Result {
    object EmulatorFound : Result()
    object DebuggerEnabled : Result()
    object Secure : Result()
}
