package com.securevale.rasp.android.util

import android.util.Log


fun logTime(tag: String, func: () -> Boolean): Boolean {

    val start = System.currentTimeMillis()

    val result = func.invoke()

    val end = System.currentTimeMillis() - start

    Log.d("Rasp-android-time", "$tag: $end")

    return result
}
