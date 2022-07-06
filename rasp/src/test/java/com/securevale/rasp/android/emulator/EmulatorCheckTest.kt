package com.securevale.rasp.android.emulator

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.check.CheckResult
import com.securevale.rasp.android.emulator.checks.*
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isOperatorNameAndroid
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isRadioVersionSuspicious
import com.securevale.rasp.android.emulator.checks.GeneralChecks.hasSuspiciousFiles
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdDevice
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdHardware
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isFingerprintFromEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGenymotion
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGoogleEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isMemu
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isNox
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isVBox
import com.securevale.rasp.android.emulator.checks.PackageChecks.hasSuspiciousPackages
import com.securevale.rasp.android.emulator.checks.PropertiesCheck.hasQemuProperties
import com.securevale.rasp.android.emulator.checks.SensorsCheck.areSensorsFromEmulator
import com.securevale.rasp.android.emulator.checks.TelephonyChecks.isTelephonySuspicious
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

class EmulatorCheckTest : TestBaseClass(withBaseLoggerMocked = true) {

    private val mockContext = mockk<Context>()

    // section basic
    @Test
    fun `when basic check is safe should return safe`() {
        mockBasicChecks()
        assertThat(getEmulatorCheck(CheckLevel.BASIC).check()).isEqualTo(CheckResult.Secure)
    }

    @Test
    fun `when basic check is vulnerable should return vulnerable`() {
        mockBasicChecks(avdDevice = true, emulatorGoogle = true)
        assertThat(getEmulatorCheck(CheckLevel.BASIC).check()).isEqualTo(CheckResult.Vulnerable)
    }

    // section advanced
    @Test
    fun `when advanced check is safe should return safe`() {
        mockAdvancedChecks()
        assertThat(getEmulatorCheck(CheckLevel.ADVANCED).check()).isEqualTo(CheckResult.Secure)
    }

    @Test
    fun `when advanced check is vulnerable but with only one false positive should return safe`() {
        mockAdvancedChecks(telephonySuspicious = true)
        assertThat(getEmulatorCheck(CheckLevel.ADVANCED).check()).isEqualTo(CheckResult.Secure)
    }

    @Test
    fun `when advanced check is vulnerable should return vulnerable`() {
        mockAdvancedChecks(radioVersionSuspicious = true)
        assertThat(getEmulatorCheck(CheckLevel.ADVANCED).check()).isEqualTo(CheckResult.Vulnerable)
    }

    @Test
    fun `when advanced check is vulnerable with at least two false positives should return vulnerable`() {
        mockAdvancedChecks(telephonySuspicious = true, operatorNameSuspicious = true)
        assertThat(getEmulatorCheck(CheckLevel.ADVANCED).check()).isEqualTo(CheckResult.Vulnerable)
    }

    private fun mockBasicChecks(
        avdDevice: Boolean = false,
        avdHardware: Boolean = false,
        fingerprintSuspicious: Boolean = false,
        emulatorGoogle: Boolean = false,
        vBox: Boolean = false,
        nox: Boolean = false,
        genymotion: Boolean = false,
        memu: Boolean = false,
        filesSuspicious: Boolean = false,
        sensorsFromEmulator: Boolean = false
    ) {
        mockkObject(GeneralChecks)
        mockkObject(SensorsCheck)
        every { isAvdDevice() } returns avdDevice
        every { isAvdHardware() } returns avdHardware
        every { isFingerprintFromEmulator() } returns fingerprintSuspicious
        every { isGoogleEmulator() } returns emulatorGoogle
        every { isVBox() } returns vBox
        every { isNox() } returns nox
        every { isGenymotion() } returns genymotion
        every { isMemu() } returns memu
        every { hasSuspiciousFiles() } returns filesSuspicious
        every { areSensorsFromEmulator(mockContext) } returns sensorsFromEmulator
    }

    private fun mockAdvancedChecks(
        radioVersionSuspicious: Boolean = false,
        packagesSuspicious: Boolean = false,
        telephonySuspicious: Boolean = false,
        operatorNameSuspicious: Boolean = false,
        propertiesSuspicious: Boolean = false
    ) {
        mockkObject(DeviceChecks)
        mockkObject(TelephonyChecks)
        mockkObject(PackageChecks)
        mockkObject(PropertiesCheck)

        every { isRadioVersionSuspicious() } returns radioVersionSuspicious
        every { hasSuspiciousPackages(mockContext, EMULATOR_PACKAGES) } returns packagesSuspicious
        every { hasSuspiciousPackages(mockContext, MEMU_PACKAGES) } returns packagesSuspicious
        every { isTelephonySuspicious(mockContext) } returns telephonySuspicious
        every { isOperatorNameAndroid(mockContext) } returns operatorNameSuspicious
        every { hasQemuProperties() } returns propertiesSuspicious
    }

    private fun getEmulatorCheck(checkLevel: CheckLevel) =
        EmulatorCheck(mockContext, checkLevel)
}
