package com.securevale.rasp.android.check

/**
 * Result used by [DefaultCheck] (which is implemented by all internal check classes).
 */
sealed interface CheckResult {
    object Vulnerable : CheckResult
    object Secure : CheckResult
    object Ignored : CheckResult
}

/**
 * Helper function for checking whether vulnerability was found.
 */
fun CheckResult.vulnerabilityFound() = this is CheckResult.Vulnerable
