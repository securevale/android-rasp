package com.securevale.rasp.android.debugger.checks

import android.content.Context

/**
 * An object that contains all debugger-related check functions.
 */
internal object DebuggableChecks {

    /**
     * Checks whether there is debuggable flag present in the app.
     * @param context the Context used for check.
     * @return whether app is debuggable.
     */
    @JvmName("a")
    external fun isDebuggable(context: Context): Boolean

    /**
     * Checks whether there is Debug config field set to true.
     * It is CRUCIAL to use the app [context] here in order to receive flag value from the "main" build
     * instead of the module's or library(which may return Debug set to true marking app debuggable incorrectly).
     * @param context the Context used for check.
     * @return whether app is debuggable.
     */
    @JvmName("b")
    external fun hasDebugBuildConfig(context: Context): Boolean

    /**
     * Checks whether there is debugger connected to the app.
     * @return whether app has debugger attached to it thus indicates whether app is debuggable.
     */
    @JvmName("k")
    external fun isDebuggerConnected(): Boolean

    /**
     * Checks whether there is any thread waiting for attaching the debugger.
     * @return whether app is debuggable.
     */
    @JvmName("d")
    external fun someoneIsWaitingForDebugger(): Boolean
}
