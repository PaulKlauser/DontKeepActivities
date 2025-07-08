package com.example.bgactivitiestesting

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private lateinit var name: String

    fun init(counter: String) {
        name = "VM #$counter"
    }

    fun logSaveStateActivity(name: String) {
        Log.d("Debug", "$name is saving its state")
    }

    fun logDestroyedActivity(name: String, isFinishing: Boolean) {
        Log.d("Debug", "$name has been destroyed, $isFinishing")
    }

    override fun onCleared() {
        super.onCleared()

        Log.d("Debug", "$name has been cleared")
    }
}
