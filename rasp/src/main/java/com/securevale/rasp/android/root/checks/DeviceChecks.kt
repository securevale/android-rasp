package com.securevale.rasp.android.root.checks

import android.content.Context

/**
 * An object that contains all device-related check functions.
 */
internal object DeviceChecks {

    @JvmName("p")
    external fun isSuperUser(context: Context): Boolean

    @JvmName("c")
    external fun hasTestTags(): Boolean
}
