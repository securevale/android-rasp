package com.securevale.rasp.android.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.securevale.rasp.android.sample.check.DebuggerCheckFragment
import com.securevale.rasp.android.sample.check.EmulatorCheckFragment

class CheckDetailsActivity : AppCompatActivity() {

    private val args: TestType by lazy { intent.extras?.getSerializable(TEST_ARG) as TestType }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        showCheck(getCheckFragment(args))
    }

    private fun showCheck(fragment: Fragment) {
        findViewById<FrameLayout>(R.id.container).apply {
            visibility = View.VISIBLE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("check")
                .commit()
        }
    }

    private fun getCheckFragment(type: TestType): Fragment = when (type) {
        TestType.EMULATOR -> EmulatorCheckFragment()
        TestType.DEBUGGER -> DebuggerCheckFragment()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TEST_ARG = "check_arg"

        fun intent(context: Context, arg: TestType) =
            Intent(context, CheckDetailsActivity::class.java).apply {
                putExtra(TEST_ARG, arg)
            }
    }
}

enum class TestType { EMULATOR, DEBUGGER }