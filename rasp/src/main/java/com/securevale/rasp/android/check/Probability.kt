package com.securevale.rasp.android.check

import com.securevale.rasp.android.util.SecureAppLogger

/**
 * Helper function for calling vulnerability checks against given threshold.
 * @param threshold the threshold which triggers finalization of function when reached.
 * @return whether the threshold was reached.
 */
fun List<() -> Int>.fireChecks(threshold: Int): Boolean {
    var sum = 0
    return !asSequence()
        .map { it() }
        .filter {
            sum += it
            SecureAppLogger.logDebug("Current threshold is $sum")
            sum >= threshold
        }
        .none()
}

/**
 * Helper function for calling check and returning provided [wage] based on result.
 * @param wage the wage which will be returned when check is successful.
 * @param check the check function which will be called.
 * @return [wage] if the [check] is successful or 0 otherwise.
 */
fun wrappedCheck(wage: Int, check: () -> Boolean): Int = if (check()) wage else 0
