package com.securevale.rasp.android.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.rasp.android.sample.AvailableChecksListAdapter.Check


class MainActivity : AppCompatActivity() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checksAdapter = AvailableChecksListAdapter()

        findViewById<RecyclerView>(R.id.checks_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = checksAdapter
        }

        checksAdapter.items = items
        checksAdapter.notifyDataSetChanged()
    }

    private val items =
        listOf(Check("Emulator", TestType.EMULATOR), Check("Debugger", TestType.DEBUGGER))
}
