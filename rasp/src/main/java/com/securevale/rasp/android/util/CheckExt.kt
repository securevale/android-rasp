package com.securevale.rasp.android.util

import android.content.Context
import androidx.core.content.getSystemService

/**
 * Helper function for accessing system services.
 * @param T the system service classes that we want to use.
 * @param R the [checkFun] return value.
 * @param errorMsg the error message to be printed if any exception will occur.
 * @param checkFun the logic which will be run with the system service class as an argument.
 * @return [R] or null if [checkFun] will fail.
 */
@Suppress("TooGenericExceptionCaught")
inline fun <reified T : Any, R : Any> Context.getWithSystemService(
    errorMsg: String,
    checkFun: (T) -> R
): R? {
    var result: R? = null
    getSystemService<T>()?.let {
        try {
            result = checkFun(it)
        } catch (e: Exception) {
            SecureAppLogger.logError(errorMsg, e)
        }
    }
    return result
}
