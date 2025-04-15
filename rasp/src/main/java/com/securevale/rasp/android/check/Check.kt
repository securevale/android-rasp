package com.securevale.rasp.android.check

import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.CheckType

/**
 * Base interface for checking.
 */
internal interface Check<T> {
    fun check(
        checksToTrigger: Array<CheckType> = CHECK_ALL,
        granular: Boolean = false,
        subscriber: CheckSubscriber? = null
    ): T
}

/**
 * The default vulnerability check which returns [CheckResult].
 */
internal interface DefaultCheck : Check<CheckResult>

/**
 * Base class for vulnerability checks.
 */
internal abstract class ProbabilityCheck : DefaultCheck {

    /**
     * Map of checks that were chosen to be triggered.
     */
    protected abstract val checksMap: Map<CheckType, () -> WrappedCheckResult>

    /**
     * List of checks to be triggered. It contains functions which returns the Int values (check wages)
     * when successful.
     */
    protected var checks: List<() -> WrappedCheckResult> = emptyList()

    /**
     * Type this check is assigned to.
     */
    protected abstract val checkType: String

    /**
     * Threshold, if this limit is exceeded, the check will indicate a failure,
     * signaling that the app is vulnerable.
     */
    protected abstract val threshold: Int

    /**
     * Function for collecting checks that needs to be called.
     * @param checksToTrigger array of checks that needs to be called or [CHECK_ALL] if all checks
     * should be called.
     */
    private fun collectChecks(checksToTrigger: Array<CheckType>): List<() -> WrappedCheckResult> {
        val checksToBeCalled = mutableListOf<() -> WrappedCheckResult>()
        if (checksToTrigger.contentEquals(CHECK_ALL)) {
            checksToBeCalled.addAll(checksMap.values)
            return checksToBeCalled
        }

        for (check in checksToTrigger) {
            checksMap[check]?.let {
                checksToBeCalled.add(it)
            }
        }
        return checksToBeCalled
    }

    /**
     * The check function, it triggers all checks from the [checks].
     * @param checksToTrigger the checks that should be triggered.
     * @param granular whether it should return results from every [CheckType] as a granular one.
     * @param subscriber the subscriber that should be additionally notified about the result(s)
     * if [granular] option was chosen.
     * @return the result of the check which is [CheckResult.Vulnerable] or [CheckResult.Secure]
     * based on the fact whether threshold was matched or exceeded.
     */
    override fun check(
        checksToTrigger: Array<CheckType>,
        granular: Boolean,
        subscriber: CheckSubscriber?
    ): CheckResult {
        checks = collectChecks(checksToTrigger)
        return if (granular) {
            checkNotNull(subscriber) { "Subscriber must be set when want to use granular checks" }
            checks.fireGranular(subscriber, threshold)
            CheckResult.Ignored
        } else {
            if (checks.fireChecks(threshold)) CheckResult.Vulnerable else CheckResult.Secure
        }
    }
}

/**
 * Constant for triggering all checks.
 */
val CHECK_ALL = emptyArray<CheckType>()
