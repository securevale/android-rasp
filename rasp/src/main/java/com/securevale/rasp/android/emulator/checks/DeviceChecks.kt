package com.securevale.rasp.android.emulator.checks

import android.content.Context

/**
 * An object that contains all device-related check functions.
 */
internal object DeviceChecks {
    /**
     * Checks whether operator name looks suspicious.
     * @param context the Context used for check.
     * @return whether operator name lowercase is equal to "android".
     */
    @JvmName("c")
    external fun isOperatorNameAndroid(context: Context) : Boolean

    /**
     * Checks whether device's radioVersion looks suspicious.
     * @return true if radioVersion was not found, is empty or equals to '1.0.0.0'
     */
    @JvmName("u")
    external fun isRadioVersionSuspicious() : Boolean
}



