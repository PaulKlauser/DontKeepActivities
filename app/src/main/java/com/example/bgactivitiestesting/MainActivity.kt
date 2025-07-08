package com.example.bgactivitiestesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.bgactivitiestesting.ui.theme.BgActivitiesTestingTheme
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val map = mutableMapOf<UUID, ByteArray>()
    private val name = "Activity #${ActivityCounter.counter}"

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.init(ActivityCounter.counter)

        enableEdgeToEdge()
        setContent {
            BgActivitiesTestingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(
                            onClick = { ActivityCounter.startActivity(this@MainActivity) },
                            modifier = Modifier.padding(innerPadding)
                        ) { Text("Start another activity") }
                        Button(
                            onClick = { map[UUID.randomUUID()] = ByteArray(10 * 1024 * 1024) },
                            modifier = Modifier.padding(innerPadding)
                        ) { Text("Allocate 10 MB") }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        vm.logSaveStateActivity(name)

        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        vm.logDestroyedActivity(name, isFinishing)
    }
}
