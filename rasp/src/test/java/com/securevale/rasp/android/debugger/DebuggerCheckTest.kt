package com.securevale.rasp.android.debugger

import android.content.Context
import android.content.pm.ApplicationInfo
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.check.CheckResult
import com.securevale.rasp.android.debugger.checks.DebuggableChecks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

class DebuggerCheckTest : TestBaseClass(withBaseLoggerMocked = true) {

    private val mockApplicationInfo = mockk<ApplicationInfo>()
    private val mockContext = mockk<Context> {
        every { applicationInfo } returns mockApplicationInfo
    }
    private val debuggerCheck = DebuggerCheck(mockContext)

    @Test
    fun `when check vulnerability is bigger than threshold check should return vulnerable`() {
        mockChecks(debuggerConnected = true, waitingForDebugger = true)
        assertThat(debuggerCheck.check()).isEqualTo(CheckResult.Vulnerable)
    }

    @Test
    fun `when check vulnerability is equal threshold check should return vulnerable`() {
        mockChecks(debuggerConnected = true)
        assertThat(debuggerCheck.check()).isEqualTo(CheckResult.Vulnerable)
    }

    @Test
    fun `when check have not found vulnerabilities check should return safe`() {
        mockChecks()
        assertThat(debuggerCheck.check()).isEqualTo(CheckResult.Secure)
    }

    private fun mockChecks(
        debuggerConnected: Boolean = false,
        waitingForDebugger: Boolean = false,
        debugBuildConfig: Boolean = false
    ) {
        mockkObject(DebuggableChecks)
        every {
            DebuggableChecks.hasDebugBuildConfig(mockContext)
        } returns debugBuildConfig
        every { DebuggableChecks.isDebuggerConnected() } returns debuggerConnected
        every { DebuggableChecks.someoneIsWaitingForDebugger() } returns waitingForDebugger
    }
}
