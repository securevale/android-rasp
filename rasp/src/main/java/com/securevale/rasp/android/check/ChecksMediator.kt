package com.securevale.rasp.android.check

import android.content.Context
import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.DebuggerChecks
import com.securevale.rasp.android.api.result.EmulatorChecks
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.api.result.Result
import com.securevale.rasp.android.debugger.DebuggerCheck
import com.securevale.rasp.android.emulator.EmulatorCheck

class ChecksMediator(
    context: Context,
    emulator: Boolean,
    debugger: Boolean
) {

    private var emulatorCheck: EmulatorCheck? = null
    private var debugCheck: DebuggerCheck? = null

    init {
        if (emulator) emulatorCheck = EmulatorCheck(context)
        if (debugger) debugCheck = DebuggerCheck(context)
    }

    fun checkGranular(
        checkOnlyFor: Array<CheckType> = CHECK_ALL,
        subscriber: CheckSubscriber,
        vulnerabilitiesOnly: Boolean
    ) {
        val internalSubscriber = getSubscriber(vulnerabilitiesOnly, subscriber)
        emulatorCheck?.check(checkOnlyFor, true, internalSubscriber)
        debugCheck?.check(checkOnlyFor, true, internalSubscriber)
    }

    fun checkMerged(
        checkOnlyFor: Array<CheckType> = CHECK_ALL,
        subscriber: CheckSubscriber,
        vulnerabilitiesOnly: Boolean
    ) {
        val internalSubscriber = getSubscriber(vulnerabilitiesOnly, subscriber)

        val emulator = emulatorCheck?.check(checkOnlyFor)
        internalSubscriber.onCheck(
            ExtendedResult(
                EmulatorChecks.EmulatorCheck,
                emulator?.vulnerabilityFound() ?: false
            )
        )

        val debugger = debugCheck?.check(checkOnlyFor)
        internalSubscriber.onCheck(
            ExtendedResult(
                DebuggerChecks.DebuggerCheck,
                debugger?.vulnerabilityFound() ?: false
            )
        )
    }

    fun check(): Result = when {
        emulatorCheck?.check()?.vulnerabilityFound()
            ?: false -> Result.EmulatorFound

        debugCheck?.check()?.vulnerabilityFound()
            ?: false -> Result.DebuggerEnabled

        else -> Result.Secure
    }

    private fun getSubscriber(vulnerabilitiesOnly: Boolean, externalSubscriber: CheckSubscriber) =
        object : CheckSubscriber {
            override fun onCheck(result: ExtendedResult) {
                if (vulnerabilitiesOnly) {
                    if (result.vulnerable) externalSubscriber.onCheck(result)
                } else {
                    externalSubscriber.onCheck(result)
                }
            }
        }
}
