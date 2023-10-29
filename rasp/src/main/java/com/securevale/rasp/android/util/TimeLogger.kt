package com.securevale.rasp.android.util

import android.util.Log

/**
 * Helper function for debugging the elapsed time of function execution.
 * @param tag the tag for logging.
 * @param func the function to trigger.
 * @return the result of [func]
 */
fun logTime(tag: String, func: () -> Boolean): Boolean {

    val start = System.currentTimeMillis()

    val result = func.invoke()

    val end = System.currentTimeMillis() - start

    Log.d("Rasp-android-time", "$tag: $end")

    return result
}
