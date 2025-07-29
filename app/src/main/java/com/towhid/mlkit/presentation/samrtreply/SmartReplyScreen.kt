package com.towhid.mlkit.presentation.samrtreply

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SmartReplyScreen(viewModel: SmartReplyViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var message by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Smart Reply", style = MaterialTheme.typography.headlineLarge)

        Row {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Enter message") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                viewModel.onEvent(SmartReplyEvent.OnAddMessage(message, isLocalUser = false))
                message = ""
            }) {
                Text("Send")
            }
        }

        Button(
            onClick = { viewModel.onEvent(SmartReplyEvent.OnGenerateReplies) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Generate Replies")
        }

        if (state.isLoading) CircularProgressIndicator()

        if (state.suggestions.isNotEmpty()) {
            Text("Suggestions:")
            state.suggestions.forEach {
                Text("- $it")
            }
        }

        if (state.error != null) {
            Text("Error: ${state.error}", color = Color.Red)
        }
    }
}
