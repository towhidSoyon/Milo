package com.towhid.mlkit.presentation.objectdetection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ObjectDetectionViewModel : ViewModel() {

    private val _state = MutableStateFlow(ObjectDetectionState())
    val state: StateFlow<ObjectDetectionState> = _state

    private val detector = ObjectDetection.getClient(
        ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
    )

    fun onEvent(event: ObjectDetectionEvent) {
        when (event) {
            is ObjectDetectionEvent.OnImageSelected -> {
                _state.update { it.copy(bitmap = event.bitmap, error = null) }
            }

            ObjectDetectionEvent.OnDetectObjects -> {
                val bitmap = _state.value.bitmap ?: return
                val image = InputImage.fromBitmap(bitmap, 0)

                _state.update { it.copy(isLoading = true) }

                detector.process(image)
                    .addOnSuccessListener { objects ->
                        _state.update {
                            it.copy(detectedObjects = objects, isLoading = false)
                        }
                    }
                    .addOnFailureListener {
                        _state.update {
                            it.copy(error = it.error, isLoading = false)
                        }
                    }
            }

            ObjectDetectionEvent.OnReset -> {
                _state.value = ObjectDetectionState()
            }
        }
    }
}

sealed class ObjectDetectionEvent {
    data class OnImageSelected(val bitmap: Bitmap) : ObjectDetectionEvent()
    object OnDetectObjects : ObjectDetectionEvent()
    object OnReset : ObjectDetectionEvent()
}

data class ObjectDetectionState(
    val bitmap: Bitmap? = null,
    val detectedObjects: List<DetectedObject> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


