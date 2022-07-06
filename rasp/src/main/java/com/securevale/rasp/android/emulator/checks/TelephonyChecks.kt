package com.securevale.rasp.android.emulator.checks

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import com.securevale.rasp.android.util.ApiCondition
import com.securevale.rasp.android.util.SecureAppLogger
import com.securevale.rasp.android.util.getWithSystemService
import com.securevale.rasp.android.util.runOn

/**
 * Phone numbers which indicates emulator
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal val EMULATOR_PHONE_NUMBERS = arrayOf(
    "15555215554", "15555215556", "15555215558", "15555215560", "15555215562", "15555215564",
    "15555215566", "15555215568", "15555215570", "15555215572", "15555215574", "15555215576",
    "15555215578", "15555215580", "15555215582", "15555215584"
)

/**
 * An object that contains all telephony-related check functions.
 */
internal object TelephonyChecks {

    /**
     * Checks if device supports telephony feature.
     * @param context the Context used for checking feature availability.
     * @return whether device supports telephony.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun supportsTelephony(context: Context) =
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)

    /**
     * Gets device's phone number.
     * @param context the Context used to access [TelephonyManager].
     * @return the device's phone number or error message indicating that permission was missing.
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @RequiresPermission(READ_PHONE_STATE)
    fun getPhoneNumber(context: Context): String? =
        context.getWithSystemService<TelephonyManager, String>("Missing permission to check phone number") {
            try {
                return it.line1Number
            } catch (e: SecurityException) {
                SecureAppLogger.logError("Missing permission to check phone number", e)
                return "Missing permission to check phone number"
            }
        }

    /**
     * Checks if device's phone number looks suspicious.
     * This check only works on [Build.VERSION_CODES.Q] or below.
     * @param context the Context used for check.
     * @return whether the device's phone number is on the [EMULATOR_PHONE_NUMBERS] list or not.
     */
    @RequiresPermission(READ_PHONE_STATE)
    fun isTelephonySuspicious(context: Context): Boolean {
        return runOn(ApiCondition.QorBelow, {
            if ((ContextCompat.checkSelfPermission(context, READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) && supportsTelephony(context)
            ) {
                val phoneNumber = getPhoneNumber(context)
                EMULATOR_PHONE_NUMBERS.forEach { number ->
                    if (number == phoneNumber || "+$number" == phoneNumber) {
                        return@runOn true
                    }
                }
                false

            } else {
                false
            }
        }, {
            false
        })
    }
}
