package com.securevale.rasp.android.sample.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.securevale.rasp.android.api.SecureAppChecker
import com.securevale.rasp.android.api.result.ExtendedResult
import com.securevale.rasp.android.sample.R

class EmulatorCheckFragment : Fragment() {

    private lateinit var checkResultsAdapter: CheckResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkResultsAdapter = CheckResultAdapter()
        view.findViewById<RecyclerView>(R.id.checks_results).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = checkResultsAdapter
        }

        view.findViewById<Button>(R.id.checkButton).apply {
            text = "Emulator check"

            setOnClickListener {
                val emulatorCheck = SecureAppChecker.Builder(requireContext(), checkEmulator = true)
                    .build()

                emulatorCheck.subscribe(granular = true) {
                    checkResultsAdapter.updateItems(it)
                }
            }
        }
    }
}