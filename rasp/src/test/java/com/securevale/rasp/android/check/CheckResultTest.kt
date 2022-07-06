package com.securevale.rasp.android.check

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CheckResultTest {

    @Test
    fun `when result is vulnerable check should return so`() {
        val result = CheckResult.Vulnerable.vulnerabilityFound()
        assertThat(result).isTrue()
    }

    @Test
    fun `when result is safe check should return so`() {
        val result = CheckResult.Secure.vulnerabilityFound()
        assertThat(result).isFalse()
    }
}
