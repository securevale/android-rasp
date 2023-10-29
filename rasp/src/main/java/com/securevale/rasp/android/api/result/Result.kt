package com.securevale.rasp.android.api.result

/**
 * The result of the vulnerability check.
 */
sealed class Result {
    data object EmulatorFound : Result()
    data object DebuggerEnabled : Result()
    data object Secure : Result()
}

/**
 * The extended result class which contains [CheckType] alongside with its result.
 */
data class ExtendedResult(val checkType: CheckType, val vulnerable: Boolean)
