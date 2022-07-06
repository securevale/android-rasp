package com.securevale.rasp.android.emulator.checks

import android.content.Context
import android.telephony.TelephonyManager
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isOperatorNameAndroid
import com.securevale.rasp.android.emulator.checks.DeviceChecks.isRadioVersionSuspicious
import com.securevale.rasp.android.emulator.checks.DeviceChecks.operatorName
import com.securevale.rasp.android.emulator.checks.DeviceChecks.radioVersion
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

class DeviceChecksTest : TestBaseClass() {

    private val mockContext = mockk<Context>()

    override fun setupBefore() {
        mockkObject(DeviceChecks)
    }

    // operatorName
    @Test
    fun `operatorName returns android lowercase should return true`() {
        every { mockContext.getSystemService(any()) } returns mockk<TelephonyManager>()
        every { operatorName(mockContext) } returns "android"

        assertThat(isOperatorNameAndroid(mockContext)).isTrue()
    }

    @Test
    fun `operatorName returns android uppercase first letter should return true`() {
        every { mockContext.getSystemService(any()) } returns mockk<TelephonyManager>()
        every { operatorName(mockContext) } returns "Android"

        assertThat(isOperatorNameAndroid(mockContext)).isTrue()
    }

    @Test
    fun `operatorName returns android all uppercase should return true`() {
        every { mockContext.getSystemService(any()) } returns mockk<TelephonyManager>()
        every { operatorName(mockContext) } returns "ANDROID"

        assertThat(isOperatorNameAndroid(mockContext)).isTrue()
    }

    @Test
    fun `operatorName returns not android should return false`() {
        every { mockContext.getSystemService(any()) } returns mockk<TelephonyManager>()
        every { operatorName(mockContext) } returns "Orange"

        assertThat(isOperatorNameAndroid(mockContext)).isFalse()
    }

    // radioVersion
    @Test
    fun `radioVersion returns null should be suspicious`() {
        every { radioVersion() } returns null

        assertThat(isRadioVersionSuspicious()).isTrue()
    }

    @Test
    fun `radioVersion returns empty string should be suspicious`() {
        every { radioVersion() } returns ""

        assertThat(isRadioVersionSuspicious()).isTrue()
    }

    @Test
    fun `radioVersion returns emulator default version should be suspicious`() {
        every { radioVersion() } returns "1.0.0.0"

        assertThat(isRadioVersionSuspicious()).isTrue()
    }

    @Test
    fun `radioVersion returns real version should not be suspicious`() {
        every { radioVersion() } returns "G9300ZCU2API3"

        assertThat(isRadioVersionSuspicious()).isFalse()
    }
}
