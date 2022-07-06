package com.securevale.rasp.android.emulator.checks

import android.content.Context
import android.telephony.TelephonyManager
import androidx.annotation.VisibleForTesting
import com.securevale.rasp.android.util.getWithSystemService

/**
 * An object that contains all device-related check functions.
 */
internal object DeviceChecks {

    /**
     * Checks whether operator name looks suspicious.
     * @param context the Context used for check.
     * @return whether operator name lowercase is equal to "android".
     */
    fun isOperatorNameAndroid(context: Context): Boolean {
        val operatorName = operatorName(context)
        return operatorName?.lowercase() == "android"
    }

    /**
     * Gets the operator name from [TelephonyManager].
     * @param context the Context used access [TelephonyManager].
     * @return the networkOperatorName from the [TelephonyManager] or null if no permissions.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun operatorName(context: Context): String? = context
        .getWithSystemService<TelephonyManager, String>("") { it.networkOperatorName }

    /**
     * Checks whether device's radioVersion looks suspicious.
     * @return true if radioVersion was not found, is empty or equals to '1.0.0.0'
     */
    fun isRadioVersionSuspicious(): Boolean {
        val radioVersion = radioVersion()
        return radioVersion?.isEmpty() ?: true || radioVersion == "1.0.0.0"
    }

    /**
     * Gets the device's radioVersion.
     * @return the device's radioVersion or null if not found.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun radioVersion(): String? = android.os.Build.getRadioVersion()
}



