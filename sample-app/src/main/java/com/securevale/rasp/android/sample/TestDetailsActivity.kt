package com.securevale.rasp.android.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestDetailsActivity : AppCompatActivity() {

    private val args: TestData by lazy { intent.extras?.getSerializable(TEST_ARG) as TestData }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_test_result)
        fillData()
    }

    private fun fillData() {
        val checkResultTv = findViewById<TextView>(R.id.check_result_tv).apply {
            movementMethod = ScrollingMovementMethod()
        }

        for (button in args.buttons) {
            val linearLayout = findViewById<LinearLayout>(R.id.buttonsContainer)

            val buttonView: Button = (LayoutInflater.from(this)
                .inflate(R.layout.test_button, linearLayout, false) as Button)
                .apply {
                    id = View.generateViewId()
                    text = button.buttonText
                    setOnClickListener {
                        val result = button.buttonAction.invoke(context)
                        checkResultTv.text = result
                    }
                }
            linearLayout.addView(buttonView, 0)
        }
    }

    companion object {
        private const val TEST_ARG = "test_arg"

        fun intent(context: Context, arg: TestData) =
            Intent(context, TestDetailsActivity::class.java).apply {
                putExtra(TEST_ARG, arg)
            }
    }
}