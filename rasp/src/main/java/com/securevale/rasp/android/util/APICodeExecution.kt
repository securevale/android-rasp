package com.securevale.rasp.android.util

import android.os.Build

/**
 * Helper function for running code on given [ApiCondition].
 * @param T the type of the returned value.
 * @param apiCondition the apiCondition instance which will be used to choose which code to run.
 * @param run the code which will be run if [apiCondition] has been met.
 * @param otherwise the code which will be run if [apiCondition] was not met.
 * @return [T] function expects both [run] and [otherwise] returning the same type.
 */
fun <T> runOn(apiCondition: ApiCondition, run: () -> T, otherwise: () -> T) =
    if (apiCondition.condition(Build.VERSION.SDK_INT)) {
        run()
    } else {
        otherwise()
    }

/**
 * Api conditions enum
 *
 * This is used for running code based on devices having particular APIs
 */
enum class ApiCondition {
    /**
     * Condition that is used to run some code on devices having [Build.VERSION_CODES.Q] and below.
     */
    QorBelow {
        override fun condition(apiVersion: Int) = apiVersion <= Build.VERSION_CODES.Q
    };

    /**
     * Checks whether the condition is met.
     * @param apiVersion the device's API version to check against.
     */
    abstract fun condition(apiVersion: Int): Boolean
}
