package com.securevale.rasp.android.check

import android.content.Context
import com.securevale.rasp.android.api.CheckSubscriber
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.DebuggerChecks
import com.securevale.rasp.android.api.result.EmulatorChecks
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.api.result.Result
import com.securevale.rasp.android.api.result.RootChecks
import com.securevale.rasp.android.debugger.DebuggerCheck
import com.securevale.rasp.android.emulator.EmulatorCheck
import com.securevale.rasp.android.root.RootCheck

/**
 * Class that acts as a mediator between the public's API of the library and its internals.
 * @param context the Context used for checks configuration.
 * @param emulator whether emulator checks should be triggered.
 * @param debugger whether checks for debug should be triggered.
 * @param root whether checks for root should be triggered.
 */
class ChecksMediator(
    context: Context,
    emulator: Boolean,
    debugger: Boolean,
    root: Boolean
) {

    /**
     * The emulator check instance.
     */
    private var emulatorCheck: EmulatorCheck? = null

    /**
     * The debugger check instance.
     */
    private var debugCheck: DebuggerCheck? = null

    /**
     * The root check instance.
     */
    private var rootCheck: RootCheck? = null

    init {
        if (emulator) emulatorCheck = EmulatorCheck(context)
        if (debugger) debugCheck = DebuggerCheck(context)
        if (root) rootCheck = RootCheck(context)
    }

    /**
     * Function that will trigger granular checks and notify [subscriber] about the result(s).
     * @param checkOnlyFor checks that should be triggered.
     * @param subscriber subscriber where the results will be passed on.
     * @param vulnerabilitiesOnly whether it should notify [subscriber] only if vulnerability is found.
     */
    fun checkGranular(
        checkOnlyFor: Array<CheckType> = CHECK_ALL,
        subscriber: CheckSubscriber,
        vulnerabilitiesOnly: Boolean
    ) {
        val internalSubscriber = getSubscriber(vulnerabilitiesOnly, subscriber)
        emulatorCheck?.check(checkOnlyFor, true, internalSubscriber)
        debugCheck?.check(checkOnlyFor, true, internalSubscriber)
        rootCheck?.check(checkOnlyFor, true, internalSubscriber)
    }

    /**
     * Function that will trigger all checks and notify [subscriber] about the result(s).
     * @param checkOnlyFor checks that should be triggered.
     * @param subscriber subscriber where the results will be passed on.
     * @param vulnerabilitiesOnly whether it should notify [subscriber] only if vulnerability is found.
     */
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

        val root = rootCheck?.check(checkOnlyFor)
        internalSubscriber.onCheck(
            ExtendedResult(
                RootChecks.RootCheck,
                root?.vulnerabilityFound() ?: false
            )
        )
    }

    /**
     * Function that will trigger all checks and return the result.
     * @return the check's [Result].
     */
    fun check(): Result = when {
        emulatorCheck?.check()?.vulnerabilityFound()
            ?: false -> Result.EmulatorFound

        debugCheck?.check()?.vulnerabilityFound()
            ?: false -> Result.DebuggerEnabled

        rootCheck?.check()?.vulnerabilityFound()
            ?: false -> Result.Rooted

        else -> Result.Secure
    }

    /**
     * Helper function for wrapping [externalSubscriber] based on configuration provided.
     * @param vulnerabilitiesOnly whether it should notify subscriber only if vulnerability is found.
     * @param externalSubscriber the [CheckSubscriber] to be wrapped.
     * @return the wrapped subscriber.
     */
    private fun getSubscriber(vulnerabilitiesOnly: Boolean, externalSubscriber: CheckSubscriber) =
        CheckSubscriber { result ->
            if (vulnerabilitiesOnly) {
                if (result.vulnerable) externalSubscriber.onCheck(result)
            } else {
                externalSubscriber.onCheck(result)
            }
        }
}
