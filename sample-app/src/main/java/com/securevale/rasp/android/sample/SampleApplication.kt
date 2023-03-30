package com.securevale.rasp.android.sample

import android.app.Application
import com.securevale.rasp.android.native.SecureApp

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SecureApp.init()
    }
}