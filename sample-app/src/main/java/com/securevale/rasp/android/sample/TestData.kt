package com.securevale.rasp.android.sample

import android.content.Context
import java.io.Serializable

data class TestData(
    val buttons: List<TestButtonData> = emptyList()
) : Serializable

data class TestButtonData(
    val buttonText: String,
    val buttonAction: ((Context) -> String?)
) : Serializable

infix fun String.action(action: ((Context) -> String?)) = TestButtonData(this, action)