package com.securevale.rasp.android.emulator.checks

import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.PropertiesCheck.hasQemuProperties
import com.securevale.rasp.android.emulator.checks.PropertiesCheck.qemuProperties
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test

class PropertiesCheckTest : TestBaseClass() {

    // indicatesEmulator
    @Test
    fun `when wanted value is null and found value is not check should return true`() {
        val testProperty = Property("name", null)

        val result = testProperty.indicatesEmulator("found")
        assertThat(result).isTrue()
    }

    @Test
    fun `when wanted value is not null and found value is check should return false`() {
        val testProperty = Property("name", "wanted")

        val result = testProperty.indicatesEmulator(null)
        assertThat(result).isFalse()
    }

    @Test
    fun `when wanted value is not null and found value is same check should return true`() {
        val testProperty = Property("name", "wanted")

        val result = testProperty.indicatesEmulator("wanted")
        assertThat(result).isTrue()
    }

    @Test
    fun `when wanted value is not null and found value is different check should return false`() {
        val testProperty = Property("name", "wanted")

        val result = testProperty.indicatesEmulator("different")
        assertThat(result).isFalse()
    }

    // qemuProperties
    @Test
    fun `when qemu properties result exceeded threshold check should return true`() {
        mockkObject(PropertiesCheck)
        every { qemuProperties() } returns EMULATOR_PROPERTIES_THRESHOLD + 1

        assertThat(hasQemuProperties()).isTrue()
    }

    @Test
    fun `when qemu properties result is equal to threshold check should return true`() {
        mockkObject(PropertiesCheck)
        every { qemuProperties() } returns EMULATOR_PROPERTIES_THRESHOLD

        assertThat(hasQemuProperties()).isTrue()
    }

    @Test
    fun `when qemu properties result is less than threshold check should return false`() {
        mockkObject(PropertiesCheck)
        every { qemuProperties() } returns EMULATOR_PROPERTIES_THRESHOLD - 1

        assertThat(hasQemuProperties()).isFalse()
    }
}
