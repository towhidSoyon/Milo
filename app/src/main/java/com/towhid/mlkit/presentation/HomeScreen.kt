package com.towhid.mlkit.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        topBar = {
            Text("MiLo",modifier= Modifier.statusBarsPadding().padding(16.dp), style = MaterialTheme.typography.headlineLarge)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()/*.background(MaterialTheme.colorScheme.background)*/.padding(paddingValues)
            .padding(horizontal = 16.dp)) {

            val buttons = listOf(
                "Text Recognition" to "textRecognition",
                "Face Detection" to "faceDetection",
                "Object Detection" to "objectDetection",
                "Smart Reply" to "smartReply",
                "Language Identification" to "languageId"
            )

            buttons.forEach { (label, route) ->
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(label)
                }
            }
        }
    }

}
