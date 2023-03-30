package com.securevale.rasp.android.api.result

/**
 * The result of the vulnerability check.
 */
sealed class Result {
    object EmulatorFound : Result()
    object DebuggerEnabled : Result()
    object Secure : Result()
}

data class ExtendedResult(val checkType: CheckType,val  vulnerable: Boolean)
