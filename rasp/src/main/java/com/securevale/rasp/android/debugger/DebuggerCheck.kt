package com.securevale.rasp.android.debugger

import android.content.Context
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.hasDebugBuildConfig
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.isDebuggable
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.isDebuggerConnected
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.someoneIsWaitingForDebugger

/**
 * Debugger detection check.
 *
 * @property context the app's Context, you need to use context from the main app module for initialisation here.
 * DO NOT use context from any other modules as it will result with checking incorrect BuildConfig file(not the main app one),
 * it may result in returning incorrect results from this check class.
 */
@PublishedApi
internal class DebuggerCheck(private val context: Context) : ProbabilityCheck() {

    /**
     * @see [ProbabilityCheck]
     */
    override val threshold = 3

    /**
     * @see [ProbabilityCheck]
     */
    override val checks: List<() -> Int>
        get() = listOf(
            ::checkDebuggable,
            ::checkDebugField,
            ::checkDebuggerConnected,
        )

    /**
     * Checks for debuggable flag
     */
    private fun checkDebuggable() = wrappedCheck(3) { isDebuggable(context) }

    /**
     * Checks whether DEBUG field is present
     */
    private fun checkDebugField() = wrappedCheck(3) {
        hasDebugBuildConfig(context)
    }

    /**
     * Checks whether debugger is connected
     */
    private fun checkDebuggerConnected() = wrappedCheck(3) {
        isDebuggerConnected() || someoneIsWaitingForDebugger()
    }
}
