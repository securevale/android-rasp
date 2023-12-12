package com.securevale.rasp.android.api

import com.securevale.rasp.android.api.result.ExtendedResult

/**
 * The interface for doing more granular checks, it wraps the
 * [com.securevale.rasp.android.api.result.CheckType] with the result of the check.
 */
fun interface CheckSubscriber {
    /**
     * Callback function which will be called with the result of the check.
     */
    fun onCheck(result: ExtendedResult)
}
