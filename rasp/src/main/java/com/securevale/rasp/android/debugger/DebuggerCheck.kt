package com.securevale.rasp.android.debugger

import android.content.Context
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.DebuggerChecks
import com.securevale.rasp.android.api.result.DebuggerChecks.DebugField
import com.securevale.rasp.android.api.result.DebuggerChecks.Debuggable
import com.securevale.rasp.android.api.result.DebuggerChecks.DebuggerConnected
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.WrappedCheckResult
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.hasDebugBuildConfig
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.isDebuggable
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.isDebuggerConnected
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.someoneIsWaitingForDebugger

/**
 * Debugger detection check.
 *
 * @property context: This refers to the app's Context and should be initialized using
 * the context from the main app module.
 *
 * Using Context from other modules may lead to incorrect BuildConfig file checks,
 * resulting in inaccurate results from this check class.
 */
@PublishedApi
internal class DebuggerCheck(private val context: Context) : ProbabilityCheck() {

    /**
     * @see [ProbabilityCheck]
     */
    override val threshold = 3

    override val checksMap: Map<CheckType, () -> WrappedCheckResult> = mapOf(
        Debuggable to ::checkDebuggable,
        DebugField to ::checkDebugField,
        DebuggerConnected to ::checkDebuggerConnected
    )

    /**
     * @see [ProbabilityCheck]
     */
    override val checkType: String = DebuggerChecks::class.java.simpleName

    /**
     * Checks for debuggable flag.
     */
    private fun checkDebuggable() = wrappedCheck(3, Debuggable) {
        isDebuggable(context)
    }

    /**
     * Checks whether DEBUG field is present.
     */
    private fun checkDebugField() = wrappedCheck(3, DebugField) {
        hasDebugBuildConfig(context)
    }

    /**
     * Checks whether debugger is connected.
     */
    private fun checkDebuggerConnected() = wrappedCheck(3, DebuggerConnected) {
        isDebuggerConnected() || someoneIsWaitingForDebugger()
    }
}
