package com.securevale.rasp.android.util

import android.annotation.SuppressLint
import android.os.Build
import com.securevale.rasp.android.emulator.checks.*

/**
 * Helper function for easily accessing the device's Build fields.
 */
@Deprecated("Will be rewritten in native in 0.3.0", replaceWith = ReplaceWith(""))
@Suppress("UnusedPrivateMember")
private fun deviceInfo(): String = """
          Bootloader: ${Build.BOOTLOADER}
          Device: ${Build.DEVICE}
          Model: ${Build.MODEL}
          Product: ${Build.PRODUCT}
          Manufacturer: ${Build.MANUFACTURER}
          Brand: ${Build.BRAND}
          Board: ${Build.BOARD}
          Hardware: ${Build.HARDWARE}
          Fingerprint: ${Build.FINGERPRINT}
          Tags: ${Build.TAGS}
          """.trimIndent()

/**
 * Helper function for accessing more detailed device's information's.
 */
@SuppressLint("MissingPermission")
@Suppress("UnusedPrivateMember")
@Deprecated("Will be rewritten in native in 0.3.0", replaceWith = ReplaceWith(""))
private fun extendedDeviceInfo(): String = """
""".trimIndent()

/**
 * Helper function for accessing device's sensors list.
 */
@Suppress("UnusedPrivateMember")
@Deprecated("Will be rewritten in native in 0.3.0", replaceWith = ReplaceWith(""))
private fun sensorsInfo(): String = EMPTY
