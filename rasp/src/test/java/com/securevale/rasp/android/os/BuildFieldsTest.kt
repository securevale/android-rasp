package com.securevale.rasp.android.os

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.os.BuildFields.getBuildConfigValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Test

class BuildFieldsTest : TestBaseClass(withBaseLoggerMocked = true) {

    private val mockContext = mockk<Context>()

    @Test
    fun `when package name is correct buildConfig return value`() {
        every { mockContext.packageName } returns "com.securevale.rasp.android"
        assertThat(getBuildConfigValue(mockContext, "DEBUG")).isNotNull()
    }

    @Test
    fun `when build config class not found should return null`() {
        every { mockContext.packageName } throws ClassNotFoundException()
        assertThat(getBuildConfigValue(mockContext, "value")).isNull()
    }

    @Test
    fun `when requested field not found should return null`() {
        every { mockContext.packageName } throws NoSuchFieldException()
        assertThat(getBuildConfigValue(mockContext, "value")).isNull()
    }

    @Test
    fun `when illegal access exception thrown should return null`() {
        every { mockContext.packageName } throws IllegalAccessException()
        assertThat(getBuildConfigValue(mockContext, "value")).isNull()
    }

    @Test
    fun `when any other exception thrown should rethrow`() {
        every { mockContext.packageName } throws Exception()
        assertThrows(Exception::class.java) { getBuildConfigValue(mockContext, "value") }
    }
}
