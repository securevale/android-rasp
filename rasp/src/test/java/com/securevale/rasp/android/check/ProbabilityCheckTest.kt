package com.securevale.rasp.android.check

import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.mocks.ProbabilityCheckTestInstance
import org.junit.Test

class ProbabilityCheckTest : TestBaseClass(withBaseLoggerMocked = true) {

    @Test
    fun `when check is safe result should be also safe`() {
        val testClass = ProbabilityCheckTestInstance(checkShouldPass = true)

        assertThat(testClass.check()).isEqualTo(CheckResult.Secure)
    }

    @Test
    fun `when checks found vulnerabilities but below threshold result should be safe`() {
        val testClass = ProbabilityCheckTestInstance(
            checkShouldPass = false,
            threshold = 5,
            vulnerabilitiesFoundReturnValue = 4
        )

        assertThat(testClass.check()).isEqualTo(CheckResult.Secure)
    }

    @Test
    fun `when checks found vulnerabilities and above threshold result should be vulnerable`() {
        val testClass = ProbabilityCheckTestInstance(
            checkShouldPass = false,
            threshold = 5,
            vulnerabilitiesFoundReturnValue = 6
        )

        assertThat(testClass.check()).isEqualTo(CheckResult.Vulnerable)
    }

    @Test
    fun `when checks found vulnerabilities and same as threshold result should be vulnerable`() {
        val testClass = ProbabilityCheckTestInstance(
            checkShouldPass = false,
            threshold = 6,
            vulnerabilitiesFoundReturnValue = 6
        )

        assertThat(testClass.check()).isEqualTo(CheckResult.Vulnerable)
    }
}
