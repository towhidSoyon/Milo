package com.towhid.mlkit.presentation.barcodedetection

import android.graphics.ImageFormat
import android.media.Image
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import com.towhid.mlkit.R
import android.net.Uri
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import android.provider.MediaStore
import androidx.compose.ui.res.painterResource

@Composable
fun BarcodeScannerScreen(
    navController: NavController,
    viewModel: BarcodeScannerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Load bitmap from the selected image uri
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                // Convert bitmap to Image for ML Kit barcode scanner requires Media.Image,
                // but since that’s tricky, we simulate a call with bitmap
                // For real camera image, pass Media.Image, here we fake using bitmap.
                viewModel.onEvent(BarcodeScannerEvent.OnProcessBitmap(bitmap))
            }
        }
    )

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
                    text = "Barcode Scanner",
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                onClick = {
                    imagePickerLauncher.launch("image/*")
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.scannedImageBitmap != null) {
                        Image(
                            bitmap = state.scannedImageBitmap!!.asImageBitmap(),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .wrapContentSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.onBackground.copy(0.1f),
                                    RoundedCornerShape(40.dp)
                                )
                                .size(70.dp)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.camera),
                                contentDescription = "Select Image",
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            state.scannedImageBitmap?.let {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onEvent(BarcodeScannerEvent.OnRecognizeBarcode) }
                ) {
                    Text("Scan Barcode")
                }
            }

            Spacer(Modifier.height(16.dp))

            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Error: ${state.error}", color = Color.Red)
                state.scannedBarcodes.isNotEmpty() -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Detected Barcodes:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            for (code in state.scannedBarcodes) {
                                Text(text = code, fontSize = 18.sp)
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
