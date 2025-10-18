package com.example.bgactivitiestesting.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bgactivitiestesting.ui.theme.BgActivitiesTestingTheme

class PostCounterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BgActivitiesTestingTheme {
                Scaffold { paddingValues ->
                    Text(
                        text = "You made it, now go back and check the counter!",
                        modifier = Modifier.Companion
                            .padding(paddingValues)
                            .padding(16.dp)
                    )
                }
            }
        }
    }

}