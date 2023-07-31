package com.securevale.rasp.android.util

import android.annotation.SuppressLint
import android.content.Context
import com.securevale.rasp.android.emulator.checks.*

/**
 * Helper function for easy access to device's Build fields.
 */
external fun deviceInfo(): String

/**
 * Helper function for accessing more detailed device's information's.
 */
@SuppressLint("MissingPermission")
//@Suppress("UnusedPrivateMember")
//@Deprecated("Will be rewritten in native in 0.3.0", replaceWith = ReplaceWith(""))
external fun extendedDeviceInfo(): String

/**
 * Helper function for accessing device's sensors list.
 */
external fun sensorInfo(context: Context): String
