package com.towhid.mlkit.presentation.textrecognition

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TextRecognitionScreen(
    viewModel: TextRecognitionViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                viewModel.onEvent(TextRecognitionEvent.OnImageSelected(bitmap))
            }
        }
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Text Recognition", style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(12.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Pick Image")
        }

        Spacer(Modifier.height(12.dp))

        state.imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = { viewModel.onEvent(TextRecognitionEvent.OnRecognizeText) }) {
                Text("Recognize Text")
            }

            Spacer(Modifier.height(8.dp))
        }

        when {
            state.isLoading -> CircularProgressIndicator()
            state.recognizedText.isNotBlank() -> {
                Text("Result:", fontWeight = FontWeight.Bold)
                Text(state.recognizedText)
            }
            state.error != null -> {
                Text("Error: ${state.error}", color = Color.Red)
            }
        }
    }
}
