package com.towhid.mlkit.presentation.languagedetection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LanguageIdScreen(viewModel: LanguageIdViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            Text(
                "Language Identification",
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    ) { paddingValues ->

        Column(Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)) {

            Spacer(Modifier.height(8.dp))

            TextField(
                value = state.inputText,
                onValueChange = { viewModel.onEvent(LanguageIdEvent.OnTextChange(it)) },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = { viewModel.onEvent(LanguageIdEvent.OnIdentifyLanguage) }) {
                Text("Identify Language")
            }

            Spacer(Modifier.height(8.dp))

            when {
                state.isLoading -> CircularProgressIndicator()
                state.languageCode.isNotBlank() -> Text("Detected Language: ${state.languageCode}")
                state.error != null -> Text("Error: ${state.error}", color = Color.Red)
            }
        }
    }
}
