@file:Suppress("WildcardImport")

package com.securevale.rasp.android.emulator

import android.content.Context
import com.securevale.rasp.android.api.result.CheckType
import com.securevale.rasp.android.api.result.EmulatorChecks
import com.securevale.rasp.android.api.result.EmulatorChecks.*
import com.securevale.rasp.android.check.ProbabilityCheck
import com.securevale.rasp.android.check.WrappedCheckResult
import com.securevale.rasp.android.check.wrappedCheck
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isOperatorNameAndroid
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isRadioVersionSuspicious
import com.securevale.rasp.android.emulator.checks.GeneralChecks.cpuSuspicious
import com.securevale.rasp.android.emulator.checks.GeneralChecks.hasSuspiciousFiles
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdDevice
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdHardware
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isFingerprintFromEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGenymotion
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGoogleEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isMemu
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isNox
import com.securevale.rasp.android.emulator.checks.GeneralChecks.modulesSuspicious
import com.securevale.rasp.android.emulator.checks.GeneralChecks.mountsSuspicious
import com.securevale.rasp.android.emulator.checks.PackageChecks.hasSuspiciousPackages
import com.securevale.rasp.android.emulator.checks.PropertyChecks.hasQemuProperties
import com.securevale.rasp.android.emulator.checks.SensorChecks.areSensorsFromEmulator

/**
 * Emulator detection check.
 *
 * @property context the app's Context.
 */
@PublishedApi
internal class EmulatorCheck(
    private val context: Context
) : ProbabilityCheck() {

    /**
     * @see [ProbabilityCheck]
     */
    override val threshold = 10

    override val checksMap: Map<CheckType, () -> WrappedCheckResult> = mapOf(
        AvdDevice to ::checkAvdDevice,
        AvdHardware to ::checkAvdHardware,
        Genymotion to ::checkGenymotion,
        Nox to ::checkNox,
        Memu to ::checkMemu,
        Fingerprint to ::checkFingerprint,
        SuspiciousFiles to ::checkFiles,
        GoogleEmulator to ::checkEmulatorGoogle,
        Sensors to ::checkSensors,
        OperatorName to ::checkOperatorName,
        RadioVersion to ::checkRadioVersion,
        SuspiciousPackages to ::checkPackages,
        SuspiciousProperties to ::checkProperties,
        Mounts to ::checkMounts,
        CPU to ::checkCPU,
        Modules to ::checkModules
    )

    /**
     * @see [ProbabilityCheck]
     */
    override val checkType: String = EmulatorChecks::class.java.simpleName

    /**
     * Checks whether AVD device indicators were found.
     */
    private fun checkAvdDevice() = wrappedCheck(2, AvdDevice) { isAvdDevice() }

    /**
     * Checks whether AVD hardware indicators were found.
     */
    private fun checkAvdHardware() = wrappedCheck(2, AvdHardware) { isAvdHardware() }

    /**
     * Checks whether device's fingerprint looks suspicious.
     */
    private fun checkFingerprint() = wrappedCheck(2, Fingerprint) { isFingerprintFromEmulator() }

    /**
     * Checks whether Google emulator indicators were found.
     */
    private fun checkEmulatorGoogle() = wrappedCheck(3, GoogleEmulator) { isGoogleEmulator() }

    /**
     * Checks whether Nox emulator indicators were found.
     */
    private fun checkNox() = wrappedCheck(3, Nox) { isNox() }

    /**
     * Checks whether Genymotion emulator indicators were found.
     */
    private fun checkGenymotion() = wrappedCheck(3, Genymotion) {
        isGenymotion()
    }

    /**
     * Checks whether mounts seems suspicious.
     */
    private fun checkMounts() = wrappedCheck(3, Mounts) {
        mountsSuspicious()
    }

    /**
     * Checks whether CPU seems suspicious.
     */
    private fun checkCPU() = wrappedCheck(3, CPU) {
        cpuSuspicious()
    }

    /**
     * Checks whether modules seems suspicious.
     */
    private fun checkModules() = wrappedCheck(3, Modules) {
        modulesSuspicious()
    }

    /**
     * Checks whether Memu indicators were found.
     */
    private fun checkMemu() = wrappedCheck(3, Memu) { isMemu() }

    /**
     * Checks whether there are any suspicious files were found.
     */
    private fun checkFiles() = wrappedCheck(5, SuspiciousFiles) { hasSuspiciousFiles() }

    /**
     * Checks whether any of the sensors looks suspicious.
     */
    private fun checkSensors() = wrappedCheck(10, Sensors) { areSensorsFromEmulator(context) }

    /**
     * Checks whether radio version looks suspicious.
     */
    private fun checkRadioVersion() = wrappedCheck(10, RadioVersion) { isRadioVersionSuspicious() }

    /**
     * Checks whether there are any suspicious emulator packages on the device.
     */
    private fun checkPackages() =
        wrappedCheck(10, SuspiciousPackages) { hasSuspiciousPackages(context) }

    /**
     *  Section for checks which might return false positives so they cannot indicate whether emulator
     *  is detected on their own (need to be matched with at least second positive match).
     */

    /**
     * Checks whether operator name is Android.
     */
    private fun checkOperatorName() =
        wrappedCheck(5, OperatorName) { isOperatorNameAndroid(context) }

    /**
     * Checks whether there are any suspicious qemu properties found on a device.
     */
    private fun checkProperties() = wrappedCheck(3, SuspiciousProperties) { hasQemuProperties() }
}
