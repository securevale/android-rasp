package com.securevale.rasp.android.emulator.checks

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.securevale.rasp.android.util.getWithSystemService

/**
 * An object that contains all sensor-related check functions.
 */
internal object SensorsCheck {

    /**
     * Gets device's sensors.
     * @param context the Context used to access [SensorManager].
     * @return the device sensors list formatted as a string(each sensor in separated row).
     */
    fun sensorsList(context: Context) =
        context.getWithSystemService<SensorManager, String>("") {
            val sensors = StringBuilder()
            it.getSensorList(Sensor.TYPE_ALL).forEach { sensor ->
                sensors.append(sensor.name)
                sensors.append("\n")
            }
            return sensors.toString()
        }

    /**
     * Checks if sensors are from emulator.
     * @param context the Context used for check.
     * @return whether any of the sensors contains "goldfish" thus indicating emulator.
     */
    fun areSensorsFromEmulator(context: Context): Boolean = sensorsList(context)
        ?.lowercase()
        ?.contains("goldfish")
        ?: true
}
