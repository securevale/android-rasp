package com.securevale.rasp.android.api

import com.securevale.rasp.android.api.result.ExtendedResult

interface CheckSubscriber {
    fun onCheck(result: ExtendedResult)
}
