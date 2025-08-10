package com.towhid.mlkit.presentation.facedetection

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FaceDetectionScreen(viewModel: FaceDetectionViewModel = viewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                viewModel.onEvent(FaceDetectionEvent.OnImageSelected(bitmap))
            }
        }
    )

    Scaffold(
        topBar = {
            Text("Face Detection",modifier= Modifier
                .statusBarsPadding()
                .padding(16.dp), style = MaterialTheme.typography.headlineLarge)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp).verticalScroll(rememberScrollState())
        ) {

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Pick Image")
            }
            Spacer(Modifier.height(12.dp))
            state.bitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(12.dp))


                Button(onClick = { viewModel.onEvent(FaceDetectionEvent.OnDetectFaces) }) {
                    Text("Detect Faces")
                }

                Spacer(Modifier.height(8.dp))

                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.faces.isNotEmpty() -> {
                        Text("Faces detected: ${state.faces.size}")
                        state.faces.forEachIndexed { index, face ->
                            Text(
                                "Face ${index+1} - Smiling: ${
                                    face.smilingProbability?.times(100)?.toInt()
                                }%"
                            )
                        }
                    }

                    state.error != null -> Text("Error: ${state.error}", color = Color.Red)
                }
            }
        }
    }
}
