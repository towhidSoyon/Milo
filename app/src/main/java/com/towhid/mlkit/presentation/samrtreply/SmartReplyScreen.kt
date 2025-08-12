package com.towhid.mlkit.presentation.samrtreply

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.towhid.mlkit.presentation.languagedetection.LanguageIdEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SmartReplyScreen(
    navController: NavController,
    viewModel: SmartReplyViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Smart Reply",
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF036C61)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(Modifier.background(Color.White.copy(0.05f)))
            ) {
                BasicTextField(
                    value = message,
                    onValueChange = {
                        message = it
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onEvent(SmartReplyEvent.OnAddMessage(message, isLocalUser = false))
                    message = ""
                }) {
                Text("Send")
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(SmartReplyEvent.OnGenerateReplies) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Replies")
            }

            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> CircularProgressIndicator()
                state.suggestions.isNotEmpty() -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text("Result:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))

                            when {
                                state.suggestions.isNotEmpty() -> {
                                    Text("Suggestions:")
                                    state.suggestions.forEach {
                                        Text("- $it")
                                    }
                                }

                                state.error != null -> Text(
                                    "Error: ${state.error}",
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
