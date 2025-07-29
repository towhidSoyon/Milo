package com.towhid.mlkit.presentation.textrecognition

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextRecognitionViewModel : ViewModel() {

    private val _state = MutableStateFlow(TextRecognitionState())
    val state: StateFlow<TextRecognitionState> = _state

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


    fun onEvent(event: TextRecognitionEvent) {
        when (event) {
            is TextRecognitionEvent.OnImageSelected -> {
                _state.update { it.copy(imageBitmap = event.bitmap, recognizedText = "", error = null) }
            }

            TextRecognitionEvent.OnRecognizeText -> {
                val bitmap = _state.value.imageBitmap ?: return
                val inputImage = InputImage.fromBitmap(bitmap, 0)

                _state.update { it.copy(isLoading = true, error = null) }

                recognizer.process(inputImage)
                    .addOnSuccessListener { result ->
                        _state.update {
                            it.copy(
                                recognizedText = result.text,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = e.localizedMessage ?: "Error recognizing text"
                            )
                        }
                    }
            }

            TextRecognitionEvent.OnReset -> {
                _state.value = TextRecognitionState()
            }
        }
    }
}

sealed class TextRecognitionEvent {
    data class OnImageSelected(val bitmap: Bitmap) : TextRecognitionEvent()
    object OnRecognizeText : TextRecognitionEvent()
    object OnReset : TextRecognitionEvent()
}

data class TextRecognitionState(
    val imageBitmap: Bitmap? = null,
    val recognizedText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
