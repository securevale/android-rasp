package com.securevale.rasp.android.emulator.checks

import android.content.Context

/**
 * An object that contains all device-related check functions.
 */
internal object DeviceChecks {
    /**
     * Checks whether operator name looks suspicious.
     * @param context the Context used for check.
     * @return result of the check.
     */
    @JvmName("c")
    external fun isOperatorNameAndroid(context: Context) : Boolean

    /**
     * Checks whether device's radioVersion looks suspicious.
     * @return result of the check.
     */
    @JvmName("u")
    external fun isRadioVersionSuspicious() : Boolean
}



