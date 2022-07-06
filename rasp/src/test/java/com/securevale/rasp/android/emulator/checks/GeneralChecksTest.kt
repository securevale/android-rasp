package com.securevale.rasp.android.emulator.checks

import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.GeneralChecks.hasSuspiciousFiles
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdDevice
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isAvdHardware
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isFingerprintFromEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGenymotion
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isGoogleEmulator
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isMemu
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isNox
import com.securevale.rasp.android.emulator.checks.GeneralChecks.isVBox
import com.securevale.rasp.android.mocks.mockDeviceData
import com.securevale.rasp.android.util.SystemProperties
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Test

class GeneralChecksTest : TestBaseClass() {

    // avdDevice
    @Test
    fun `when device is avd device check should return true`() {
        mockDeviceData(device = "generic_x86")
        assertThat(isAvdDevice()).isTrue()

        mockDeviceData(bootloader = "unknown")
        assertThat(isAvdDevice()).isTrue()

        mockDeviceData(board = "goldfish_x86")
        assertThat(isAvdDevice()).isTrue()

        mockDeviceData(board = "unknown")
        assertThat(isAvdDevice()).isTrue()
    }

    @Test
    fun `when device is not avd device check should return false`() {
        mockDeviceData()
        assertThat(isAvdDevice()).isFalse()
    }

    // avdHardware
    @Test
    fun `when device has avd hardware check should return true`() {
        mockDeviceData(hardware = "goldfish")
        assertThat(isAvdHardware()).isTrue()

        mockDeviceData(hardware = "ranchu")
        assertThat(isAvdHardware()).isTrue()

        mockDeviceData(hardware = "unknown")
        assertThat(isAvdHardware()).isTrue()

        mockDeviceData(hardware = "x86")
        assertThat(isAvdHardware()).isTrue()
    }

    @Test
    fun `when device has no avd hardware check should return false`() {
        mockDeviceData()
        assertThat(isAvdHardware()).isFalse()
    }

    // memu
    @Test
    fun `when device has memu hardware check should return true`() {
        mockDeviceData(hardware = "intel")
        assertThat(isMemu()).isTrue()
    }

    @Test
    fun `when device has not memu hardware check should return false`() {
        mockDeviceData()
        assertThat(isMemu()).isFalse()
    }

    // genymotion
    @Test
    fun `when device is genymotion check should return true`() {
        mockDeviceData(manufacturer = "Genymotion")
        assertThat(isGenymotion()).isTrue()

        mockDeviceData()
        mockkStatic(::hasGenymotionFiles)
        every { hasGenymotionFiles() } returns true
        assertThat(isGenymotion()).isTrue()

        unmockkStatic(::hasGenymotionFiles)
        mockkObject(GeneralChecks)
        every { isGenymotion() } returns true
        assertThat(isGenymotion()).isTrue()
    }

    @Test
    fun `when device is not genymotion check should return false`() {
        mockDeviceData()
        assertThat(isGenymotion()).isFalse()
    }

    // vbox
    @Test
    fun `when device is vBox check should return true`() {
        mockDeviceData(hardware = "vbox86")
        assertThat(isVBox()).isTrue()

        mockDeviceData(product = "vbox86p")
        assertThat(isVBox()).isTrue()
    }

    @Test
    fun `when device is not vBox check should return false`() {
        mockDeviceData()
        assertThat(isVBox()).isFalse()
    }

    // nox
    @Test
    fun `when device is nox check should return true`() {
        mockDeviceData(hardware = "Nox")
        assertThat(isNox()).isTrue()

        mockDeviceData(product = "nox_product")
        assertThat(isNox()).isTrue()

        mockDeviceData(board = "nox_board")
        assertThat(isNox()).isTrue()

        mockDeviceData()
        mockkStatic(::hasNoxFiles)
        every { hasNoxFiles() } returns true
        assertThat(isNox()).isTrue()
    }

    @Test
    fun `when device is not nox check should return false`() {
        mockDeviceData()
        assertThat(isNox()).isFalse()
    }

    // suspicious files
    @Test
    fun `when device has andy suspicious files check should return true`() {
        mockkStatic(::hasAndyFiles)
        every { hasAndyFiles() } returns true

        assertThat(hasSuspiciousFiles()).isTrue()
    }

    @Test
    fun `when device has blue suspicious files check should return true`() {
        mockkStatic(::hasBlueFiles)
        every { hasBlueFiles() } returns true

        assertThat(hasSuspiciousFiles()).isTrue()
    }

    @Test
    fun `when device has x86 suspicious files check should return true`() {
        mockkStatic(::hasX86Files)
        every { hasX86Files() } returns true

        assertThat(hasSuspiciousFiles()).isTrue()
    }

    @Test
    fun `when device has emulator suspicious files check should return true`() {
        mockkStatic(::hasEmulatorFiles)
        every { hasEmulatorFiles() } returns true

        assertThat(hasSuspiciousFiles()).isTrue()
    }

    @Test
    fun `when device has no suspicious files check should return false`() {
        assertThat(hasSuspiciousFiles()).isFalse()
    }

    // fingerprint
    @Test
    fun `when fingerprint starts with generic check should return true`() {
        mockDeviceData(fingerprint = "generic_fingerprint")

        assertThat(isFingerprintFromEmulator()).isTrue()
    }

    @Test
    fun `when fingerprint starts with unknown check should return true`() {
        mockDeviceData(fingerprint = "unknown")

        assertThat(isFingerprintFromEmulator()).isTrue()
    }

    @Test
    fun `when fingerprint starts with google sdk phone check should return true`() {
        mockDeviceData(fingerprint = "google/sdk_gphone_")

        assertThat(isFingerprintFromEmulator()).isTrue()
    }

    @Test
    fun `when fingerprint contains x86 sdk phone check should return true`() {
        mockDeviceData(fingerprint = "fingerprint_x86")

        assertThat(isFingerprintFromEmulator()).isTrue()
    }

    @Test
    fun `when fingerprint contains debug sdk phone check should return true`() {
        mockDeviceData(fingerprint = "google/sdk_debug")

        assertThat(isFingerprintFromEmulator()).isTrue()
    }

    // google emulator
    @Test
    fun `when device is google emulator check should return true`() {
        mockDeviceData(model = "Emulator")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(model = "Android SDK built for x86")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(model = "google_sdk")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(model = "sdk_gphone_")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(model = "droid4x")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(model = "DROID4X")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_google")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "google_sdk")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_x86")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_gphone64_arm64")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_gphone_x86")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_emulator")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(product = "sdk_simulator")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(brand = "generic", device = "generic")
        assertThat(isGoogleEmulator()).isTrue()

        mockDeviceData(tags = "dev-keys")
        assertThat(isGoogleEmulator()).isTrue()

        mockkStatic(::hasEmulatorPipes)
        every { hasEmulatorPipes() } returns true
        assertThat(isGoogleEmulator()).isTrue()

        mockkObject(SystemProperties)
        every { SystemProperties.getProp("ro.kernel.qemu") } returns "1"
        assertThat(isGoogleEmulator()).isTrue()
    }

    @Test
    fun `when device is not google emulator check should return false`() {
        mockDeviceData()
        mockkObject(SystemProperties)
        every { SystemProperties.getProp("ro.kernel.qemu") } returns "0"
        assertThat(isGoogleEmulator()).isFalse()

        mockDeviceData(brand = "generic")
        assertThat(isGoogleEmulator()).isFalse()

        mockDeviceData(device = "generic")
        assertThat(isGoogleEmulator()).isFalse()
    }
}
