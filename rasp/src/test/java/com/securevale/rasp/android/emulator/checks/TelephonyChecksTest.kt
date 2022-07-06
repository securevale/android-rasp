package com.securevale.rasp.android.emulator.checks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.TelephonyChecks.getPhoneNumber
import com.securevale.rasp.android.emulator.checks.TelephonyChecks.isTelephonySuspicious
import com.securevale.rasp.android.util.ApiCondition
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Test

class TelephonyChecksTest : TestBaseClass() {

    private val mockPackageManager = mockk<PackageManager> {
        every { hasSystemFeature(PackageManager.FEATURE_TELEPHONY) } returns true
    }

    private val mockContext = mockk<Context> {
        every { packageManager } returns mockPackageManager
    }

    override fun setupBefore() {
        mockkObject(TelephonyChecks)
    }

    // sdk more than Q
    @Test
    fun `when sdk is more than Q check should return false`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns false

        val result = isTelephonySuspicious(mockContext)

        assertThat(result).isFalse()
    }

    // sdk less than Q
    @Test
    fun `when has no read phone state permission check should return false`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns true
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.READ_PHONE_STATE
            )
        } returns PackageManager.PERMISSION_DENIED
        every { getPhoneNumber(mockContext) } returns EMULATOR_PHONE_NUMBERS[0]

        val result = isTelephonySuspicious(mockContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `when don't support telephony check should return false`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns true
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.READ_PHONE_STATE
            )
        } returns PackageManager.PERMISSION_GRANTED
        every { mockPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) } returns false
        every { getPhoneNumber(mockContext) } returns EMULATOR_PHONE_NUMBERS[0]

        val result = isTelephonySuspicious(mockContext)

        assertThat(result).isFalse()
    }

    @Test
    fun `when suspicious phone number found check should return true`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns true
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.READ_PHONE_STATE
            )
        } returns PackageManager.PERMISSION_GRANTED
        every { getPhoneNumber(mockContext) } returns EMULATOR_PHONE_NUMBERS[0]

        val result = isTelephonySuspicious(mockContext)

        assertThat(result).isTrue()
    }

    @Test
    fun `when suspicious phone number not found check should return false`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns true
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                mockContext,
                Manifest.permission.READ_PHONE_STATE
            )
        } returns PackageManager.PERMISSION_GRANTED
        every { getPhoneNumber(mockContext) } returns "509876545"

        val result = isTelephonySuspicious(mockContext)

        assertThat(result).isFalse()
    }
}
