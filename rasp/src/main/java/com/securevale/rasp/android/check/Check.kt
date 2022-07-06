package com.securevale.rasp.android.check

/**
 * Base interface for checking.
 */
interface Check<T> {

    fun check(): T
}

/**
 * The default vulnerability check which returns [CheckResult].
 */
interface DefaultCheck : Check<CheckResult>

/**
 * Base class for vulnerability checks.
 */
abstract class ProbabilityCheck : DefaultCheck {
    /**
     * List of checks to be triggered. It contains functions which returns the Int values when successful.
     */
    protected abstract val checks: List<() -> Int>

    /**
     * Threshold on which the check should return that check failed and app is vulnerable.
     */
    protected abstract val threshold: Int

    /**
     * The check function, it triggers all checks from the [checks].
     * @return the result of the check which is [CheckResult.Vulnerable] or [CheckResult.Secure]
     * based on the fact whether threshold was matched or exceeded.
     */
    override fun check(): CheckResult = if (checks.fireChecks(threshold))
        CheckResult.Vulnerable else CheckResult.Secure
}
