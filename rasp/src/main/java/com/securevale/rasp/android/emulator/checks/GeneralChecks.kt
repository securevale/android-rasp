package com.securevale.rasp.android.emulator.checks

import com.securevale.rasp.android.os.BuildFields
import com.securevale.rasp.android.util.SystemProperties

/**
 * An object that contains all general check functions.
 */
internal object GeneralChecks {

    /**
     * Types of devices for AVD emulator.
     */
    private val avdDevices = listOf("generic_x86_arm", "generic_x86", "generic", "x86")

    /**
     * Checks if device is avd one.
     * @param device the device to be checked.
     * @return whether given device is among the [avdDevices] list.
     */
    private fun isAvd(device: String) = avdDevices.contains(device)

    /**
     * Checks if there are any indicators that device might be avd one.
     * @return whether device [isAvd] or there is any build field that indicates so.
     */
    fun isAvdDevice() = isAvd(BuildFields.device)
            || BuildFields.bootLoader == "unknown"
            || BuildFields.board == "goldfish_x86"
            || BuildFields.board == "unknown"

    /**
     * Checks if hardware looks like from avd.
     * @return whether hardware build field looks suspicious.
     */
    fun isAvdHardware() = BuildFields.hardware == "goldfish"
            || BuildFields.hardware == "ranchu"
            || BuildFields.hardware == "unknown"
            || BuildFields.hardware.contains("x86")

    /**
     * Checks if it is memu device.
     * @return whether hardware build field is from memu emulator.
     */
    fun isMemu() = BuildFields.hardware == "intel"

    /**
     * Checks if it is Genymotion device.
     * @return whether there are any indicators that it is Genymotion device.
     */
    fun isGenymotion() = BuildFields.manufacturer.contains("Genymotion")
            || hasGenymotionFiles()
            || isVBox()

    /**
     * Checks if there are any vbox fields
     * @return whether any vbox fields were found.
     */
    fun isVBox() = BuildFields.hardware == "vbox86"
            || BuildFields.product == "vbox86p"

    /**
     * Checks if it is Nox device.
     * @return whether there are any indicators that it is Nox device.
     */
    fun isNox() = BuildFields.hardware.lowercase().contains("nox")
            || BuildFields.product.lowercase().contains("nox")
            || BuildFields.board.lowercase().contains("nox")
            || hasNoxFiles()

    /**
     * Checks for suspicious files.
     * @return whether there are any suspicious files found.
     */
    fun hasSuspiciousFiles() =
        hasAndyFiles() || hasBlueFiles() || hasX86Files() || hasEmulatorFiles()

    /**
     * Checks for suspicious fingerprint build field.
     * @return whether fingerprint build field is suspicious.
     */
    fun isFingerprintFromEmulator() = BuildFields.fingerprint.startsWith("generic")
            || BuildFields.fingerprint.startsWith("unknown")
            || BuildFields.fingerprint.startsWith("google/sdk_gphone_")
            || BuildFields.fingerprint.contains("x86")
            || BuildFields.fingerprint.contains("debug")

    /**
     * Checks for device being Google's emulator.
     * @return whether there are any indicators that this device might be a Google emulator.
     */
    fun isGoogleEmulator() = BuildFields.model.contains("Emulator") // ****
            || BuildFields.model.contains("Android SDK built for x86")
            || BuildFields.model.contains("google_sdk")
            || BuildFields.model.startsWith("sdk_gphone_")
            || BuildFields.model.lowercase().contains("droid4x")
            || BuildFields.product == "sdk"
            || BuildFields.product == "sdk_google"
            || BuildFields.product == "google_sdk"
            || BuildFields.product == "sdk_x86"
            || BuildFields.product == "sdk_gphone64_arm64"
            || BuildFields.product == "sdk_gphone_x86"
            || BuildFields.product.contains("emulator")
            || BuildFields.product.contains("simulator")
            || BuildFields.brand.startsWith("generic") && BuildFields.device.startsWith("generic")
            || BuildFields.tags == "dev-keys"
            || hasEmulatorPipes()
            || SystemProperties.getProp("ro.kernel.qemu") == "1"
}
