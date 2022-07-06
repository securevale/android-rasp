package com.securevale.rasp.android.debugger.checks

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug
import com.securevale.rasp.android.os.BuildFields.getBuildConfigValue

/**
 * An object that contains all debugger-related check functions.
 */
internal object DebuggableChecks {

    /**
     * Checks whether there is debuggable flag present in the app.
     * @param context the Context used for check.
     * @return whether app is debuggable.
     */
    fun isDebuggable(context: Context) =
        (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    /**
     * Checks whether there is Debug config field set to true.
     * It is CRUCIAL to use the app [context] here in order to receive flag value from the "main" build
     * instead of the module's or library(which may return Debug set to true marking app debuggable incorrectly).
     * @param context the Context used for check.
     * @return whether app is debuggable.
     */
    fun hasDebugBuildConfig(context: Context): Boolean {
        val field = getBuildConfigValue(context, "DEBUG")
        return field == true
    }

    /**
     * Checks whether there is debugger connected to the app.
     * @return whether app is debuggable.
     */
    fun isDebuggerConnected() = Debug.isDebuggerConnected()

    /**
     * Checks whether there is any thread waiting for attaching the debugger.
     * @return whether app is debuggable.
     */
    fun someoneIsWaitingForDebugger() = Debug.waitingForDebugger()
}
