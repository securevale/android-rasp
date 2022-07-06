package com.securevale.rasp.android.debugger.checks

import android.content.Context
import android.os.Debug
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.hasDebugBuildConfig
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.isDebuggerConnected
import com.securevale.rasp.android.debugger.checks.DebuggableChecks.someoneIsWaitingForDebugger
import com.securevale.rasp.android.os.BuildFields
import com.securevale.rasp.android.os.BuildFields.getBuildConfigValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.Test

class DebuggableChecksTest : TestBaseClass(withBaseLoggerMocked = true) {

    private val mockContext = mockk<Context>()

    @Test
    fun `when buildConfig has Debug field set to true check should return debuggable`(){
        mockkObject(BuildFields)
        every { getBuildConfigValue(mockContext, "DEBUG") } returns true
        assertThat(hasDebugBuildConfig(mockContext)).isTrue()
    }

    @Test
    fun `when buildConfig has Debug field set to false check should return not debuggable`(){
        mockkObject(BuildFields)
        every { getBuildConfigValue(mockContext, "DEBUG") } returns false
        assertThat(hasDebugBuildConfig(mockContext)).isFalse()
    }

    @Test
    fun `when debugger is connected check should return true`() {
        mockkStatic(Debug::class)
        every { Debug.isDebuggerConnected() } returns true

        assertThat(isDebuggerConnected()).isTrue()
    }

    @Test
    fun `when debugger is not connected check should return false`() {
        mockkStatic(Debug::class)
        every { Debug.isDebuggerConnected() } returns false

        assertThat(isDebuggerConnected()).isFalse()
    }

    @Test
    fun `when someone is waiting for debugger check should return true`() {
        mockkStatic(Debug::class)
        every { Debug.waitingForDebugger() } returns true

        assertThat(someoneIsWaitingForDebugger()).isTrue()
    }

    @Test
    fun `when no one is waiting for debugger check should return false`() {
        mockkStatic(Debug::class)
        every { Debug.waitingForDebugger() } returns false

        assertThat(someoneIsWaitingForDebugger()).isFalse()
    }
}
