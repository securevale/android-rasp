package com.securevale.rasp.android.check

import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import org.junit.Test

class ProbabilityTest : TestBaseClass(withBaseLoggerMocked = true) {

    @Test
    fun `when successful checks wages exceeds threshold check should return so`() {
        val checkFunc: () -> Int = { 5 }
        val checksList = listOf(checkFunc, checkFunc, checkFunc)

        val result = checksList.fireChecks(9)
        assertThat(result).isTrue()
    }

    @Test
    fun `when successful checks wages are equal to threshold check should return so`() {
        val checkFunc: () -> Int = { 5 }
        val checksList = listOf(checkFunc, checkFunc, checkFunc)

        val result = checksList.fireChecks(15)
        assertThat(result).isTrue()
    }

    @Test
    fun `when successful checks wages are lower than threshold check should return so`() {
        val checkFunc: () -> Int = { 5 }
        val checksList = listOf(checkFunc, checkFunc, checkFunc)

        val result = checksList.fireChecks(16)
        assertThat(result).isFalse()
    }
}
