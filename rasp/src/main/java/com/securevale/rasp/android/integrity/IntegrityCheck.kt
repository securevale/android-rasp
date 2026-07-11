package com.securevale.rasp.android.integrity

import android.content.Context
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.IntegrityChecks
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.WrappedCheckResult
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.integrity.checks.SignatureChecks

@PublishedApi
internal class IntegrityCheck(private val context: Context) : ProbabilityCheck() {
    override val threshold = 1
    override val checkType: String = IntegrityChecks::class.java.simpleName
    override val checksMap: Map<CheckType, () -> WrappedCheckResult> = mapOf(
        IntegrityChecks.AppSignature to ::checkAppSignature
    )

    private fun checkAppSignature() = wrappedCheck(2, IntegrityChecks.AppSignature) {
        SignatureChecks.a(context)
    }
}
