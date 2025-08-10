package com.towhid.mlkit.presentation.objectdetection

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ObjectDetectionScreen(viewModel: ObjectDetectionViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                viewModel.onEvent(ObjectDetectionEvent.OnImageSelected(bitmap))
            }
        }
    )

    Scaffold(
        topBar = {
            Text(
                "Object Detection",
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Pick Image")
            }

            Spacer(Modifier.height(8.dp))

            state.bitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(8.dp))

                Button(onClick = { viewModel.onEvent(ObjectDetectionEvent.OnDetectObjects) }) {
                    Text("Detect Objects")
                }

                if (state.isLoading) CircularProgressIndicator()

                if (state.detectedObjects.isNotEmpty()) {
                    Text("Detected Objects: ${state.detectedObjects.size}")
                    state.detectedObjects.forEachIndexed { index, obj ->
                        val label = obj.labels.firstOrNull()?.text ?: "Unknown"
                        Text("Object $index: $label")
                    }
                }

                if (state.error != null) {
                    Text("Error: ${state.error}", color = Color.Red)
                }
            }
        }
    }
}
