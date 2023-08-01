package com.securevale.rasp.android.util

import android.content.Context

/**
 * Helper function for easy access to device's Build fields.
 */
external fun deviceInfo(): String

/**
 * Helper function for accessing more detailed device's information's.
 */
external fun extendedDeviceInfo(): String

/**
 * Helper function for accessing device's sensors list.
 */
external fun sensorInfo(context: Context): String
