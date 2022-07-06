package com.securevale.rasp.android.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.rasp.android.api.Result
import com.securevale.rasp.android.api.SecureAppChecker.Builder
import com.securevale.rasp.android.emulator.CheckLevel
import com.securevale.rasp.android.util.SecureAppLogger
import com.securevale.rasp.android.util.deviceInfo
import com.securevale.rasp.android.util.extendedDeviceInfo
import com.securevale.rasp.android.util.sensorsInfo

class MainActivity : AppCompatActivity() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checksAdapter = ChecksAdapter()

        findViewById<RecyclerView>(R.id.checks_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = checksAdapter
        }

        checksAdapter.items = items
        checksAdapter.notifyDataSetChanged()
    }

    private val items = listOf(
        ChecksAdapter.TestCheck(
            "Emulator",
            TestData(
                listOf(
                    "Basic Test" action { context ->
                        val result = Builder(context, emulatorCheckLevel = CheckLevel.BASIC)
                            .build()
                            .check()

                        "${result is Result.EmulatorFound}"
                    },
                    "Advanced Test" action { context ->
                        val result = Builder(context, emulatorCheckLevel = CheckLevel.ADVANCED)
                            .build()
                            .check()

                        "${result is Result.EmulatorFound}"
                    },
                    "Basic Info" action { deviceInfo() },
                    "Advanced Info" action { context -> extendedDeviceInfo(context) },
                    "Show Sensors" action
                            { context ->
                                val info = sensorsInfo(context)
                                SecureAppLogger.logDebug(info ?: "")
                                info
                            }
                )
            )),
        ChecksAdapter.TestCheck(
            "Debugger",
            TestData(
                listOf("Test" action { context ->
                    val result = Builder(context, debuggerCheck = true)
                        .build()
                        .check()

                    "${result is Result.DebuggerEnabled}"
                }
                ))
        )
    )
}
