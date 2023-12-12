package com.securevale.rasp.android.api

import android.content.Context
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.Result
import com.securevale.rasp.android.check.CHECK_ALL
import com.securevale.rasp.android.check.ChecksMediator
import com.securevale.rasp.android.util.isDebug

/**
 * Class for checking whether app is secure.
 */
class SecureAppChecker private constructor() {

    @PublishedApi
    internal lateinit var mediator: ChecksMediator

    /**
     * The function for checking whether device is secure.
     * It will trigger all checks which were configured using the [Builder].
     * @return the [Result] of the check.
     */
    @Suppress("NOTHING_TO_INLINE")
    inline fun check(): Result = mediator.check()

    /**
     * The function for checking whether device is secure.
     * It will trigger all checks which were passed via [checkOnlyFor] param and returns result(s)
     * to the [subscriber] only if the vulnerability will be found.
     * @param granular whether it should return result separately for every sub-check or just result of whole check.
     * @param checkOnlyFor checks that should be triggered.
     * @param subscriber subscriber where the results will be passed on.
     */
    @Suppress("NOTHING_TO_INLINE")
    inline fun subscribeVulnerabilitiesOnly(
        granular: Boolean = false,
        checkOnlyFor: Array<CheckType> = CHECK_ALL,
        subscriber: CheckSubscriber
    ) {
        if (granular) {
            mediator.checkGranular(checkOnlyFor, subscriber, true)
        } else {
            mediator.checkMerged(checkOnlyFor, subscriber, true)
        }
    }

    /**
     * The function for checking whether device is secure.
     * It will trigger all checks which were passed via [checkOnlyFor] param and returns result(s)
     * to the [subscriber].
     * @param granular whether it should return result separately for every sub-check or just result of whole check.
     * @param checkOnlyFor checks that should be triggered.
     * @param subscriber the subscriber where the results will be passed on.
     */
    @Suppress("NOTHING_TO_INLINE")
    inline fun subscribe(
        granular: Boolean = false,
        checkOnlyFor: Array<CheckType> = CHECK_ALL,
        subscriber: CheckSubscriber
    ) {
        if (granular) {
            mediator.checkGranular(checkOnlyFor, subscriber, false)
        } else {
            mediator.checkMerged(checkOnlyFor, subscriber, false)
        }
    }

    /**
     * Builder for [SecureAppChecker].
     * @property context the Context used for configuring checks.
     * @property checkEmulator whether emulator checks should be triggered.
     * @property checkDebugger whether checks for debug should be triggered.
     */
    class Builder(
        private val context: Context,
        private val checkEmulator: Boolean = false,
        private val checkDebugger: Boolean = false
    ) {

        /**
         * Marks whether library will run in debug mode (it shows debug logs from the lib).
         */
        fun debug(): Builder {
            isDebug = true
            return this
        }

        /**
         * Returns [SecureAppChecker] instance.
         * @return the configured [SecureAppChecker] instance.
         */
        fun build() = SecureAppChecker().apply {
            mediator = ChecksMediator(context, checkEmulator, checkDebugger)
        }
    }
}
