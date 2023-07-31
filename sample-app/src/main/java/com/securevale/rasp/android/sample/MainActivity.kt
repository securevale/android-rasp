package com.securevale.rasp.android.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.rasp.android.api.SecureAppChecker.Builder
import com.securevale.rasp.android.api.result.Result
import com.securevale.rasp.android.util.extendedDeviceInfo
import com.securevale.rasp.android.util.sensorInfo

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
                    "Emulator Test" action { context ->
                        val result = Builder(context, checkEmulator = true)
                            .build()
                            .check()

                        "${result is Result.EmulatorFound}"
                    }
                )
            )),
        ChecksAdapter.TestCheck(
            "Debugger",
            TestData(
                listOf("Test" action { context ->
                    val result = Builder(context, checkDebugger = true)
                        .build()
                        .check()

                    "${result is Result.DebuggerEnabled}"
                }
                ))
        )
    )
}
