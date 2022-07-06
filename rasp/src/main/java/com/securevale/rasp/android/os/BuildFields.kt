package com.securevale.rasp.android.os

import android.content.Context
import android.os.Build
import com.securevale.rasp.android.util.SecureAppLogger
import java.lang.reflect.Field

/**
 * A helper object to easily access fields from the [Build] class.
 */
object BuildFields {
    val bootLoader: String by lazy { Build.BOOTLOADER }
    val device: String by lazy { Build.DEVICE }
    val model: String by lazy { Build.MODEL }
    val brand: String by lazy { Build.BRAND }
    val board: String by lazy { Build.BOARD }
    val manufacturer: String by lazy { Build.MANUFACTURER }
    val hardware: String by lazy { Build.HARDWARE }
    val fingerprint: String by lazy { Build.FINGERPRINT }
    val product: String by lazy { Build.PRODUCT }
    val tags: String by lazy { Build.TAGS }
    val host: String by lazy { Build.HOST }


    /**
     * Gets the build config value.
     * @param context the Context used for getting the package name for BuildConfig class.
     * @param fieldName the BuildConfig field name.
     * @return [fieldName] name or null if were not found or error occurs.
     */
    fun getBuildConfigValue(context: Context, fieldName: String?): Any? {
        try {
            val clazz = Class.forName(context.packageName + ".BuildConfig")
            val field: Field = clazz.getField(fieldName ?: "")
            return field.get(null)
        } catch (e: ClassNotFoundException) {
            SecureAppLogger.logDebug(e.message ?: "")
        } catch (e: NoSuchFieldException) {
            SecureAppLogger.logDebug(e.message ?: "")
        } catch (e: IllegalAccessException) {
            SecureAppLogger.logDebug(e.message ?: "")
        }
        return null
    }
}
