package com.example.bgactivitiestesting

import android.app.Activity
import android.content.Intent

object ActivityCounter {

    fun startActivity(parent: Activity, name: Int) {
        parent.startActivity(Intent(parent, MainActivity::class.java)
            .apply {
                putExtra("counter", name + 1)
            })
    }
}
