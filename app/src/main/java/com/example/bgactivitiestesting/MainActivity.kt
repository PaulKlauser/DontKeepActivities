package com.example.bgactivitiestesting

import android.R.attr.data
import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    private var count = 0
    private lateinit var name: String

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        count = intent.getIntExtra("counter", 0)
        name = "Activity #${count}"
        Log.d("Debug", "$name is created")

        vm.init(name)

        enableEdgeToEdge()
        setContent {
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    Log.d(
                        "Debug",
                        "$name received activity result: ${it.resultCode}, ${it.data?.getStringExtra("result")}"
                    )
                }
            BgActivitiesTestingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, MainActivity::class.java)
                                    .apply {
                                        putExtra("counter", count + 1)
                                    }
//                                this@MainActivity.startActivityForResult(
//                                    Intent(this@MainActivity, MainActivity::class.java)
//                                        .apply {
//                                            putExtra("counter", count + 1)
//                                        },
//                                    1)
                                launcher.launch(intent)
                            },
                            modifier = Modifier.padding(innerPadding)
                        ) { Text("Start another activity") }
                        Button(
                            onClick = { map[UUID.randomUUID()] = ByteArray(10 * 1024 * 1024) },
                            modifier = Modifier.padding(innerPadding)
                        ) { Text("Allocate 10 MB") }
                        Button(
                            onClick = {
                                setResult(RESULT_OK, Intent().apply {
                                    putExtra("result", "Result from $name")
                                })
                                finish()
                            }
                        ) {
                            Text("Navigate back with result")
                        }
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

    override fun onResume() {
        super.onResume()

        Log.d("Debug", "$name is resuming")
    }

//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?,
//        caller: ComponentCaller
//    ) {
//        super.onActivityResult(requestCode, resultCode, data, caller)
//        Log.d(
//            "Debug",
//            "$name received activity result: $requestCode, $resultCode, ${data?.getStringExtra("result")}"
//        )
//    }

}
