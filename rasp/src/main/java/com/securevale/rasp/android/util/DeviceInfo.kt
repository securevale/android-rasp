package com.securevale.rasp.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.securevale.rasp.android.emulator.checks.*
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isOperatorNameAndroid
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isRadioVersionSuspicious
import com.securevale.rasp.android.emulator.checks.PackageChecks.suspiciousPackages
import com.securevale.rasp.android.emulator.checks.PropertiesCheck.qemuProperties
import com.securevale.rasp.android.emulator.checks.SensorsCheck.sensorsList
import com.securevale.rasp.android.emulator.checks.TelephonyChecks.isTelephonySuspicious

/**
 * Helper function for easily accessing the device's Build fields.
 */
fun deviceInfo(): String = """
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
fun extendedDeviceInfo(context: Context): String = """
    Nox files: ${hasNoxFiles()}
    Andy files: ${hasAndyFiles()}
    Blue files: ${hasBlueFiles()}
    X86 files: ${hasX86Files()}
    Emulator files: ${hasEmulatorFiles()}
    Genymotion files: ${hasGenymotionFiles()}
    Emulator Pipes: ${hasEmulatorPipes()}
    Qemu Property ro.kernel.qemu: ${SystemProperties.getProp("ro.kernel.qemu") == "1"}
    Qemu Properties count: ${qemuProperties()}
    Radio Version: ${isRadioVersionSuspicious()}
    Operator Name: ${isOperatorNameAndroid(context)}
    Telephony: ${isTelephonySuspicious(context)}
    Packages: ${suspiciousPackages(context, EMULATOR_PACKAGES)}
    Memu Packages: ${suspiciousPackages(context, MEMU_PACKAGES)}
""".trimIndent()

fun sensorsInfo(context: Context): String? = sensorsList(context)
