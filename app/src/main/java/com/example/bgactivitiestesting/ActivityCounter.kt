package com.example.bgactivitiestesting

import android.app.Activity
import android.content.Intent

object ActivityCounter {

    private var _counter = 1
    val counter: String
        get() = _counter.toString()

    fun startActivity(parent: Activity) {
        _counter++

        parent.startActivity(Intent(parent, MainActivity::class.java))
    }
}
