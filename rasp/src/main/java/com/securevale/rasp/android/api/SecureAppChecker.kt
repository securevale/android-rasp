package com.securevale.rasp.android.api

import android.content.Context
import com.securevale.rasp.android.check.vulnerabilityFound
import com.securevale.rasp.android.debugger.DebuggerCheck
import com.securevale.rasp.android.emulator.CheckLevel
import com.securevale.rasp.android.emulator.EmulatorCheck
import com.securevale.rasp.android.util.isDebug

/**
 * Class for checking whether app is secure.
 */
class SecureAppChecker private constructor() {

    /**
     * Check for emulator
     */
    @PublishedApi
    internal var emulatorCheck: EmulatorCheck? = null

    /**
     * Check for debug.
     */
    @PublishedApi
    internal var debugCheck: DebuggerCheck? = null

    /**
     * The function for checking whether device is secure.
     * It will trigger all checks which were configured using the [Builder].
     * @return the [Result] of the check.
     */
    @Suppress("NOTHING_TO_INLINE")
    inline fun check(): Result = when {
        emulatorCheck?.check()?.vulnerabilityFound() ?: false -> Result.EmulatorFound
        debugCheck?.check()?.vulnerabilityFound() ?: false -> Result.DebuggerEnabled
        else -> Result.Secure
    }

    /**
     * Builder for [SecureAppChecker].
     * @property context the Context used for configuring checks.
     * @param emulatorCheckLevel which check level should be used for emulator checks. If none provided then emulator check will be not triggered.
     * @param debuggerCheck whether checks for debug should be triggered.
     *
     */
    class Builder(
        private val context: Context,
        emulatorCheckLevel: CheckLevel? = null,
        debuggerCheck: Boolean = false
    ) {
        /**
         * The [SecureAppChecker] instance.
         */
        private val instance = SecureAppChecker()

        init {
            emulatorCheckLevel?.let {
                instance.emulatorCheck = EmulatorCheck(context, it)
            }

            if (debuggerCheck) {
                instance.debugCheck = DebuggerCheck(context)
            }
        }

        /**
         * Marks whether library will run in debug mode(it shows debug logs from the lib).
         */
        fun debug(): Builder {
            isDebug = true
            return this
        }

        /**
         * Returns [SecureAppChecker] instance.
         * @return the configured [SecureAppChecker] instance.
         */
        fun build() = instance
    }
}
