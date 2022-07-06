package com.securevale.rasp.android.emulator

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import androidx.annotation.RequiresPermission
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isOperatorNameAndroid
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isRadioVersionSuspicious
import com.securevale.rasp.android.emulator.checks.EMULATOR_PACKAGES
import com.securevale.rasp.android.emulator.checks.GeneralChecks.hasSuspiciousFiles
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdDevice
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdHardware
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isFingerprintFromEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGenymotion
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGoogleEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isMemu
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isNox
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isVBox
import com.securevale.rasp.android.emulator.checks.MEMU_PACKAGES
import com.securevale.rasp.android.emulator.checks.PackageChecks.hasSuspiciousPackages
import com.securevale.rasp.android.emulator.checks.PropertiesCheck.hasQemuProperties
import com.securevale.rasp.android.emulator.checks.SensorsCheck.areSensorsFromEmulator
import com.securevale.rasp.android.emulator.checks.TelephonyChecks.isTelephonySuspicious

/**
 * Emulator detection check.
 *
 * @property context the app's Context.
 * @property checkLevel how detailed check should be.
 */
@PublishedApi
internal class EmulatorCheck(
    private val context: Context,
    private val checkLevel: CheckLevel
) : ProbabilityCheck() {

    /**
     * @see [ProbabilityCheck]
     */
    override val threshold = when (checkLevel) {
        CheckLevel.BASIC -> 3
        CheckLevel.ADVANCED -> 7
    }

    /**
     * @see [ProbabilityCheck]
     */
    override val checks: List<() -> Int>
        get() = when (checkLevel) {
            CheckLevel.BASIC -> checkBasic()
            CheckLevel.ADVANCED -> checkAdvanced()
        }

    /**
     * Check for basic emulator indicators.
     */
    private fun checkBasic() = listOf<() -> Int>(
        ::checkAvdDevice,
        ::checkAvdHardware,
        ::checkGenymotion,
        ::checkVbox,
        ::checkNox,
        ::checkMemu,
        ::checkEmulatorGoogle,
        ::checkFingerprint,
        ::checkFiles,
        ::checkSensors
    )

    /**
     * Check advanced emulator indicators.
     */
    private fun checkAdvanced() = listOf<() -> Int>(
        ::checkOperatorName,
        ::checkRadioVersion,
        ::checkPackages,
        ::checkTelephony,
        ::checkProperties,
        ::checkMemuPackages
    )

    /**
     * Checks whether AVD device indicators were found.
     */
    private fun checkAvdDevice() = wrappedCheck(2) { isAvdDevice() }

    /**
     * Checks whether AVD hardware indicators were found.
     */
    private fun checkAvdHardware() = wrappedCheck(2) { isAvdHardware() }

    /**
     * Checks whether device's fingerprint looks suspicious.
     */
    private fun checkFingerprint() = wrappedCheck(2) { isFingerprintFromEmulator() }

    /**
     * Checks whether Google emulator indicators were found.
     */
    private fun checkEmulatorGoogle() = wrappedCheck(3) { isGoogleEmulator() }

    /**
     * Checks whether Vbox emulator indicators were found.
     */
    private fun checkVbox() = wrappedCheck(3) { isVBox() }

    /**
     * Checks whether Nox emulator indicators were found.
     */
    private fun checkNox() = wrappedCheck(3) { isNox() }

    /**
     * Checks whether Genymotion emulator indicators were found.
     */
    private fun checkGenymotion() = wrappedCheck(3) { isGenymotion() }

    /**
     * Checks whether Memu indicators were found.
     */
    private fun checkMemu() = wrappedCheck(3) { isMemu() }

    /**
     * Checks whether there are any suspicious files were found.
     */
    private fun checkFiles() = wrappedCheck(3) { hasSuspiciousFiles() }

    /**
     * Checks whether any of the sensors looks suspicious.
     */
    private fun checkSensors() = wrappedCheck(10) { areSensorsFromEmulator(context) }

    // Advanced

    /**
     * Checks whether radio version looks suspicious.
     */
    private fun checkRadioVersion() = wrappedCheck(10) { isRadioVersionSuspicious() }

    /**
     * Checks whether there are any suspicious emulator packages on the device.
     */
    private fun checkPackages() =
        wrappedCheck(10) { hasSuspiciousPackages(context, EMULATOR_PACKAGES) }

    /**
     * Checks whether there are any suspicious memu packages on the device.
     */
    private fun checkMemuPackages() =
        wrappedCheck(10) { hasSuspiciousPackages(context, MEMU_PACKAGES) }

    /**
     *  Section for checks which might return false positives so they cannot indicate emulator
     *  is detected on their own (need to be matched with at least second positive match).
     */

    /**
     * Checks whether telephony looks suspicious.
     */
    @RequiresPermission(READ_PHONE_STATE)
    private fun checkTelephony() = wrappedCheck(5) { isTelephonySuspicious(context) }

    /**
     * Checks whether operator name is Android.
     */
    private fun checkOperatorName() = wrappedCheck(5) { isOperatorNameAndroid(context) }

    /**
     * Checks whether there are any suspicious qemu properties found on a device.
     */
    private fun checkProperties() = wrappedCheck(3) { hasQemuProperties() }
}

/**
 * Modes of operations which [EmulatorCheck] could use.
 */
enum class CheckLevel {
    /**
     * Only check for basic emulator indicators. It is faster then the [ADVANCED] one's but can miss some emulators.
     * It might be also prone to return false positives in some cases.
     */
    BASIC,

    /**
     * Advanced check, it it more detailed than the [BASIC] one and have bigger chance to report most of emulators correctly.
     */
    ADVANCED
}
