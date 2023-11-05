package com.example.newtrackmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.newtrackmed.ui.feature.addmedication.AddMedicationScreen
import com.example.newtrackmed.ui.feature.home.HomeScreen
import com.example.newtrackmed.ui.feature.mymedications.MyMedicationsScreen
import com.example.newtrackmed.ui.feature.report.NewReportsScreen

import com.example.newtrackmed.ui.feature.report.TestBarChat

import com.example.newtrackmed.ui.feature.report.testDonut
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewTrackMedTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
                TrackMedNavController()
//                HomeScreen()

//                MyMedicationsScreen()
                //testDonut()
//                TestDonut()
//                AddMedicationScreen()
                //ReportsScreen()

//                NewReportsScreen()

//                TestBarChat()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewTrackMedTheme {
        Greeting("Android")
    }
}