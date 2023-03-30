package com.securevale.rasp.android.emulator.checks

import android.content.Context

/**
 * An object that contains all package-related check functions.
 */
internal object PackageChecks {

    /**
     * Checks whether there are any suspicious packages found.
     * @param context the app Context used for check.
     * @return whether any suspicious packages were found.
     */
    @JvmName("y")
    external fun hasSuspiciousPackages(context: Context): Boolean
}
