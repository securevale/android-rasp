package com.securevale.rasp.android.emulator.checks

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.securevale.rasp.TestBaseClass
import com.securevale.rasp.android.emulator.checks.SensorsCheck.areSensorsFromEmulator
import com.securevale.rasp.android.emulator.checks.SensorsCheck.sensorsList
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Test

class SensorsCheckTest : TestBaseClass() {

    private val mockContext = mockk<Context>()

    override fun setupBefore() {
        mockkObject(SensorsCheck)
    }

    @Test
    fun `when sensor list contains goldfish check should return true`() {
        every { sensorsList(mockContext) } returns """
        Goldfish 3-axis Accelerometer
        Goldfish 3-axis Gyroscope
        Goldfish 3-axis Magnetic field sensor
        Goldfish Orientation sensor
        Goldfish Ambient Temperature sensor
        Goldfish Proximity sensor
        Goldfish Light sensor
        Goldfish Pressure sensor
        Goldfish Humidity sensor
        """.trimIndent()

        assertThat(areSensorsFromEmulator(mockContext)).isTrue()
    }

    @Test
    fun `when sensor list doesn't contain goldfish check should return false`() {
        every { sensorsList(mockContext) } returns """
        LSM330 Accelerometer
        Linear Acceleration
        Magnetometer
        Orientation
        Gravity
        Gyroscope
        Proximity sensor
        Light sensor
        Game Rotation Vector Sensor
        GeoMag Rotation Vector Sensor
        Rotation Vector Sensor
        Orientation Sensor
        """.trimIndent()

        assertThat(areSensorsFromEmulator(mockContext)).isFalse()
    }
}
