package com.securevale.rasp.android.root.checks

import android.content.Context

/**
 * An object that contains all app-related check functions.
 */
object AppsChecks {

    @JvmName("r")
    external fun hasRootAppPackages(context: Context): Boolean

    @JvmName("k")
    external fun hasRootCloakingAppPackages(context: Context): Boolean
}
