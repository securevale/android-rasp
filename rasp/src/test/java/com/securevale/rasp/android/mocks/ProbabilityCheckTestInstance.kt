package com.securevale.rasp.android.mocks

import com.securevale.rasp.android.check.ProbabilityCheck

class ProbabilityCheckTestInstance(
    private val checkShouldPass: Boolean,
    override val threshold: Int = 1,
    private val vulnerabilitiesFoundReturnValue: Int = threshold + 1
) : ProbabilityCheck() {

    override val checks: List<() -> Int>
        get() = listOf { if (checkShouldPass) 0 else vulnerabilitiesFoundReturnValue }

}
