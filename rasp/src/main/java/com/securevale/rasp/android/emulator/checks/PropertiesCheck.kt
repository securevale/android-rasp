package com.securevale.rasp.android.emulator.checks

import androidx.annotation.VisibleForTesting
import com.securevale.rasp.android.util.SecureAppLogger
import com.securevale.rasp.android.util.SystemProperties

/**
 * Container for property and it's expected value.
 * @property name the name of the property.
 * @property wantedValue the value we are seeking for(and if found indicates emulator).
 */
internal data class Property(val name: String, val wantedValue: String? = null)

/**
 * Helper function for checking whether property is suspicious.
 * @param foundValue the value which if will match the found property value indicates emulator.
 * @return whether both property and its value looks suspicious.
 */
internal fun Property.indicatesEmulator(foundValue: String?) = when {
    wantedValue == null && foundValue != null -> true
    wantedValue == foundValue -> true
    else -> false
}

/**
 * The qemu properties found threshold which indicates whether device is suspicious or not.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
const val EMULATOR_PROPERTIES_THRESHOLD = 10

/**
 * Known qemu properties.
 */
private val KNOWN_PROPS = arrayOf(
    Property("init.svc.qemud", null),
    Property("init.svc.qemu-props", null),
    Property("qemu.hw.mainkeys", null),
    Property("qemu.sf.fake_camera", null),
    Property("qemu.sf.lcd_density", null),
    Property("ro.bootloader", "unknown"),
    Property("ro.bootmode", "unknown"),
    Property("ro.hardware", "goldfish"),
    Property("ro.kernel.android.qemud", null),
    Property("ro.kernel.qemu.gles", null),
    Property("ro.kernel.qemu", "1"),
    Property("ro.product.device", "generic"),
    Property("ro.product.model", "sdk"),
    Property(
        "ro.product.name",
        "sdk"
    ),
    Property("ro.serialno", null)
)

/**
 * An object that contains all properties-related check functions.
 */
internal object PropertiesCheck {

    /**
     * Checks for qemu properties.
     * @return whether qemu properties that has been found are equals or exceeds the [EMULATOR_PROPERTIES_THRESHOLD].
     */
    fun hasQemuProperties(): Boolean = qemuProperties() >= EMULATOR_PROPERTIES_THRESHOLD

    /**
     * Gets the found qemu properties count.
     * @return the number of the qemu properties found on the device.
     */
    fun qemuProperties(): Int {
        var outerCount = 0
        KNOWN_PROPS
            .asSequence()
            .runningFold(0) { foundProperties, property ->
                val foundValue = SystemProperties.getProp(property.name)
                val result = if (property.indicatesEmulator(foundValue)) {
                    SecureAppLogger.logDebug("Suspicious Quemu property found: $property")
                    outerCount++
                    foundProperties + 1
                } else {
                    foundProperties
                }
                result
            }
            .forEach {
                if (it >= EMULATOR_PROPERTIES_THRESHOLD) {
                    return it
                }
            }
        return outerCount
    }
}
