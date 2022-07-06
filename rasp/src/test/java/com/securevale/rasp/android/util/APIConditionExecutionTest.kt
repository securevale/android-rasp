package com.securevale.rasp.android.util

import com.google.common.truth.Truth
import com.securevale.rasp.TestBaseClass
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test

class APIConditionExecutionTest : TestBaseClass() {

    @Test
    fun `when apiCondition is met then run the logic`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns true

        var logicRan = false

        runOn(
            ApiCondition.QorBelow,
            run = {
                logicRan = true
            },
            otherwise = {
                logicRan = false
            }
        )
        Truth.assertThat(logicRan).isTrue()
    }

    @Test
    fun `when apiCondition is not met then run the fallback`() {
        mockkObject(ApiCondition.QorBelow)
        every { ApiCondition.QorBelow.condition(any()) } returns false

        var logicRan = false

        runOn(
            ApiCondition.QorBelow,
            run = {
                logicRan = true
            },
            otherwise = {
                logicRan = false
            }
        )
        Truth.assertThat(logicRan).isFalse()
    }
}
