package com.towhid.mlkit.presentation.facedetection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FaceDetectionViewModel : ViewModel() {

    private val _state = MutableStateFlow(FaceDetectionState())
    val state: StateFlow<FaceDetectionState> = _state

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()
    )

    fun onEvent(event: FaceDetectionEvent) {
        when (event) {
            is FaceDetectionEvent.OnImageSelected -> {
                _state.update { it.copy(bitmap = event.bitmap, error = null) }
            }

            FaceDetectionEvent.OnDetectFaces -> {
                val bitmap = _state.value.bitmap ?: return
                val inputImage = InputImage.fromBitmap(bitmap, 0)

                _state.update { it.copy(isLoading = true) }

                detector.process(inputImage)
                    .addOnSuccessListener { faces ->
                        _state.update { it.copy(faces = faces, isLoading = false) }
                    }
                    .addOnFailureListener {
                        _state.update {
                            it.copy(isLoading = false, error = it.error)
                        }
                    }
            }

            FaceDetectionEvent.OnReset -> {
                _state.value = FaceDetectionState()
            }
        }
    }
}
sealed class FaceDetectionEvent {
    data class OnImageSelected(val bitmap: Bitmap) : FaceDetectionEvent()
    object OnDetectFaces : FaceDetectionEvent()
    object OnReset : FaceDetectionEvent()
}

data class FaceDetectionState(
    val bitmap: Bitmap? = null,
    val faces: List<Face> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)