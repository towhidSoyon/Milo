package com.towhid.mlkit.di

import com.towhid.mlkit.presentation.facedetection.FaceDetectionViewModel
import com.towhid.mlkit.presentation.languagedetection.LanguageIdViewModel
import com.towhid.mlkit.presentation.objectdetection.ObjectDetectionViewModel
import com.towhid.mlkit.presentation.samrtreply.SmartReplyViewModel
import com.towhid.mlkit.presentation.textrecognition.TextRecognitionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TextRecognitionViewModel() }
    viewModel { FaceDetectionViewModel() }
    viewModel { ObjectDetectionViewModel() }
    viewModel { SmartReplyViewModel() }
    viewModel { LanguageIdViewModel() }
}