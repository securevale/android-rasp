package com.securevale.rasp.android.emulator.checks

import android.content.Context

/**
 * An object that contains all sensor-related check functions.
 */
internal object SensorChecks {
    /**
     * Checks if sensors are from emulator.
     * @param context the Context used for check.
     * @return whether any of the sensors contains "goldfish" thus indicating emulator.
     */
    @JvmName("g")
    external fun areSensorsFromEmulator(context: Context): Boolean
}
