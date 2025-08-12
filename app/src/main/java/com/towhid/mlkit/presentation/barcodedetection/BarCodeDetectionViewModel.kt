package com.towhid.mlkit.presentation.barcodedetection

import com.google.mlkit.vision.barcode.common.Barcode
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class BarcodeScannerViewModel : ViewModel() {

    private val _state = MutableStateFlow(BarcodeScannerState())
    val state: StateFlow<BarcodeScannerState> = _state

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

    // Store picked image bitmap to display
    private var currentBitmap: Bitmap? = null

    fun onEvent(event: BarcodeScannerEvent) {
        when (event) {
            is BarcodeScannerEvent.OnProcessBitmap -> {
                currentBitmap = event.bitmap
                _state.update {
                    it.copy(
                        scannedImageBitmap = currentBitmap,
                        scannedBarcodes = emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
            }

            BarcodeScannerEvent.OnRecognizeBarcode -> {
                currentBitmap?.let { bitmap ->
                    _state.update { it.copy(isLoading = true, error = null) }
                    try {
                        val image = InputImage.fromBitmap(bitmap, 0)
                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                val codes = barcodes.map { it.rawValue ?: "Unknown" }
                                _state.update {
                                    it.copy(
                                        scannedBarcodes = codes,
                                        isLoading = false,
                                        error = if (codes.isEmpty()) "No barcodes found" else null
                                    )
                                }
                            }
                            .addOnFailureListener { e ->
                                _state.update {
                                    it.copy(
                                        error = e.localizedMessage ?: "Error scanning barcode",
                                        isLoading = false
                                    )
                                }
                            }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                error = e.localizedMessage ?: "Invalid image",
                                isLoading = false
                            )
                        }
                    }
                } ?: run {
                    _state.update { it.copy(error = "No image selected") }
                }
            }

            BarcodeScannerEvent.OnReset -> {
                currentBitmap = null
                _state.value = BarcodeScannerState()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scanner.close()
    }
}

data class BarcodeScannerState(
    val scannedImageBitmap: Bitmap? = null,
    val scannedBarcodes: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class BarcodeScannerEvent {
    data class OnProcessBitmap(val bitmap: Bitmap) : BarcodeScannerEvent()
    object OnRecognizeBarcode : BarcodeScannerEvent()
    object OnReset : BarcodeScannerEvent()
}
