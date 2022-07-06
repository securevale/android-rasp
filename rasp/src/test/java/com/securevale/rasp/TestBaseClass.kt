package com.securevale.rasp

import com.securevale.rasp.android.util.SecureAppLogger
import io.mockk.*
import org.junit.After
import org.junit.Before

open class TestBaseClass(private val withBaseLoggerMocked: Boolean = false) {

    open fun setupBefore() = run { }

    @Before
    fun setup() {
        if (withBaseLoggerMocked) {
            mockkObject(SecureAppLogger)
            every { SecureAppLogger.logDebug(any()) } just Runs
        }
        setupBefore()
    }

    open fun setupAfter() = run {}

    @After
    fun teardown() {
        unmockkAll()
        setupAfter()
    }
}
