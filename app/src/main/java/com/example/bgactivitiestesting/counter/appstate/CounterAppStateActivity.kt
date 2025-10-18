package com.example.bgactivitiestesting.counter.appstate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bgactivitiestesting.counter.PostCounterActivity
import com.example.bgactivitiestesting.ui.theme.BgActivitiesTestingTheme
import com.example.bgactivitiestesting.ui.theme.Typography

class CounterAppStateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BgActivitiesTestingTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "CounterAppStateActivity",
                            style = Typography.titleLarge
                        )
                        val count by CounterRepository.count.collectAsStateWithLifecycle()
                        Text(
                            text = "Count $count",
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Button(
                            onClick = { CounterRepository.increment() },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Increment")
                        }

                        Button(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@CounterAppStateActivity,
                                        PostCounterActivity::class.java
                                    )
                                )
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Navigate to Post Counter Activity")
                        }
                    }
                }
            }
        }
    }
}