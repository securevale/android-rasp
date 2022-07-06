package com.securevale.rasp.android.emulator.checks

import android.content.Context
import android.content.pm.PackageManager
import com.securevale.rasp.android.util.EMPTY

/**
 * Suspicious emulator packages.
 */
internal val EMULATOR_PACKAGES = arrayOf(
    "com.google.android.launcher.layouts.genymotion",
    "com.bluestacks",
    "com.bignox.app",
    "com.vphone.launcher",
    "com.bluestacks",
    "com.bluestacks.appmart",
    "com.bignox",
    "com.bignox.app",
    "com.google.android.launcher.layouts.genymotion",
    "com.vphone.launcher",
)

/**
 * Suspicious memu packages.
 */
internal val MEMU_PACKAGES = arrayOf(
    "com.microvirt.tools",
    "com.microvirt.download",
    "com.mumu.store"
)

/**
 * An object that contains all package-related check functions.
 */
internal object PackageChecks {

    /**
     * Checks whether there are any suspicious packages found.
     * @param context the app Context used for check.
     * @param packages the suspicious packages that will be searched for.
     * @return whether any suspicious packages from the [packages] were found.
     */
    fun hasSuspiciousPackages(context: Context, packages: Array<String>): Boolean {
        val suspiciousPackage = suspiciousPackages(context, packages)
        return suspiciousPackage.isNotBlank()
    }

    /**
     * Search device for provided [packages]
     * @param context the app Context used for check.
     * @param packages the suspicious packages that will be searched for.
     * @return found suspicious package or [EMPTY] string if nothing found.
     */
    @Suppress("SwallowedException")
    fun suspiciousPackages(context: Context, packages: Array<String>): String {
        val packageManager = context.packageManager

        for (packageName in packages) {
            try {
                packageManager.getPackageInfo(packageName, 0)
                return packageName
            } catch (e: PackageManager.NameNotFoundException) {
                // ignore, this indicates that one of the packages was not found, so keep looking
            }
        }
        return EMPTY
    }
}
