package com.securevale.rasp.android.emulator.checks

import android.content.Context
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.PackageChecks.hasSuspiciousPackages
import com.securevale.rasp.android.emulator.checks.PackageChecks.suspiciousPackages
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

class PackageChecksTest : TestBaseClass() {

    private val mockPackageManager = mockk<PackageManager>()

    private val mockContext = mockk<Context> {
        every { packageManager } returns mockPackageManager
    }

    @Test
    fun `when suspicious packages found check should return true`() {
        mockkObject(PackageChecks)
        every { suspiciousPackages(any(), any()) } returns "found"

        assertThat(hasSuspiciousPackages(mockContext, emptyArray())).isTrue()
    }

    @Test
    fun `when suspicious packages found suspicious packages should return not empty list`() {
        val suspiciousPackage = "suspicious"
        every { mockPackageManager.getPackageInfo(suspiciousPackage, 0) } returns mockk()

        assertThat(suspiciousPackages(mockContext, arrayOf(suspiciousPackage))).isNotEmpty()
    }

    @Test
    fun `when suspicious packages empty suspicious packages should return empty list`() {
        val suspiciousPackage = "suspicious"
        every { mockPackageManager.getPackageInfo(suspiciousPackage, 0) } returns mockk()

        assertThat(suspiciousPackages(mockContext, emptyArray())).isEmpty()
    }

    @Test
    fun `when suspicious packages not found suspicious packages should return empty list`() {
        val suspiciousPackage = "suspicious"
        every {
            mockPackageManager.getPackageInfo(
                suspiciousPackage,
                0
            )
        } throws PackageManager.NameNotFoundException()

        assertThat(
            suspiciousPackages(
                mockContext,
                arrayOf(suspiciousPackage, suspiciousPackage, suspiciousPackage)
            )
        ).isEmpty()
    }
}
