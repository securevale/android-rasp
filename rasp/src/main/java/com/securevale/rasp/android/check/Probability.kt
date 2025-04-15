package com.securevale.rasp.android.check

import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.util.SecureAppLogger

/**
 * Helper function for calling vulnerability checks with given threshold.
 * @param threshold the threshold which triggers finalization of function when reached.
 * @return whether the threshold was reached.
 */
fun List<() -> WrappedCheckResult>.fireChecks(threshold: Int): Boolean {
    var sum = 0
    return !asSequence()
        .map { it() }
        .filter {
            sum += it.first
            SecureAppLogger.logDebug("Current threshold is $sum")
            sum >= threshold
        }
        .none()
}

fun List<() -> WrappedCheckResult>.fireGranular(subscriber: CheckSubscriber, threshold: Int) {
    var sum = 0
    asSequence()
        .map { it() }
        .forEach {
            sum += it.first
            subscriber.onCheck(ExtendedResult(it.second, it.first > 0, sum > threshold))
            SecureAppLogger.logDebug("Current threshold is $sum")
        }
}

/**
 * Helper function for calling check and returning provided [wage] based on result.
 * @param wage the wage which will be returned when check is successful.
 * @param checkType the checks that should be triggered.
 * @param check the check function which will be called.
 * @return [wage] if the [check] is successful or 0 otherwise.
 */
fun wrappedCheck(wage: Int, checkType: CheckType, check: () -> Boolean): WrappedCheckResult =
    if (check()) wage to checkType else 0 to checkType

typealias WrappedCheckResult = Pair<Int, CheckType>
