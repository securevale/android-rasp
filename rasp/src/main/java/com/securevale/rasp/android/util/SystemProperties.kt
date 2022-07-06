package com.securevale.rasp.android.util

import android.annotation.SuppressLint
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Method

/**
 * Helper object for getting the device's system properties.
 */
@SuppressLint("PrivateApi")
object SystemProperties {

    /**
     * Method for accessing the property.
     */
    private val getPropMethod: Method? by lazy {
        val clazz = Class.forName(SYSTEM_PROPERTIES)
        clazz.getMethod(METHOD_GET, String::class.java, String::class.java)
    }

    /**
     * Map of the properties names and its accessing status(whether they were attempted to access already or not)
     */
    private val failedAttemptsMap = mutableMapOf<String, PropertiesStatus>()

    /**
     * Gets the property using first reflection and if it fails trying to access it via reading the
     * getprop process output.
     * @param propName the property name.
     * @param defaultResult the default result to be returned if property was not found.
     * @return found property value or [defaultResult] if not found.
     */
    fun getProp(propName: String, defaultResult: String? = null): String? {
        // reflection
        var result = tryWithReflection(propName, defaultResult)
        if (result == defaultResult) {
            // If reflection failed try reading the getprop process output
            result = tryWithSystemOut(propName, defaultResult)
        }
        return result
    }

    /**
     * Gets the property using reflection.
     * @param propName the property name.
     * @param defaultResult the default result to be returned if property was not found.
     * @return found property value or [defaultResult] if not found.
     */
    @Suppress("TooGenericExceptionCaught")
    private fun tryWithReflection(propName: String, defaultResult: String?): String? {
        if (failedAttemptsMap[propName]?.failedViaReflection != true) try {
            return getPropMethod!!.invoke(null, propName, defaultResult) as String? ?: defaultResult
        } catch (e: Exception) {
            SecureAppLogger.logError("SystemAPI read failed from reflection:", e)
            failedAttemptsMap[propName] = PropertiesStatus(
                true, failedAttemptsMap[propName]?.failedViaProcessOut ?: false
            )
        }
        return defaultResult
    }

    /**
     * Gets the property reading the getprop process output.
     * @param propName the property name.
     * @param defaultResult the default result to be returned if property was not found.
     * @return found property value or [defaultResult] if not found.
     */
    private fun tryWithSystemOut(propName: String, defaultResult: String?): String? {
        if (failedAttemptsMap[propName]?.failedViaProcessOut != true) {
            var process: Process? = null
            try {
                process =
                    Runtime.getRuntime().exec("$GET_PROP_COMMAND \"$propName\" \"$defaultResult\"")
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                return reader.readLine()
            } catch (e: IOException) {
                SecureAppLogger.logError("SystemAPI read failed from process:", e)
                failedAttemptsMap[propName] = PropertiesStatus(
                    failedAttemptsMap[propName]?.failedViaReflection ?: false, true
                )
            } finally {
                process?.destroy()
            }
        }
        return defaultResult
    }

    /**
     * Android SystemProperties package.
     */
    private const val SYSTEM_PROPERTIES = "android.os.SystemProperties"

    /**
     * Name of the method from SystemProperties what we want to access via reflection.
     */
    private const val METHOD_GET = "get"

    /**
     * Name of the getprop command that is used if accessing property via reflection fails.
     */
    private const val GET_PROP_COMMAND = "getprop"

    /**
     * Helper class for mapping already accessed properties alongside with its access status.
     * @property failedViaReflection whether accessing the property via reflections failed.
     * @property failedViaProcessOut whether accessing the property via reading getprop process
     * output failed.
     */
    private data class PropertiesStatus(
        val failedViaReflection: Boolean, val failedViaProcessOut: Boolean
    )
}
