package com.towhid.mlkit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.towhid.mlkit.presentation.HomeScreen
import com.towhid.mlkit.presentation.SplashScreen
import com.towhid.mlkit.presentation.barcodedetection.BarcodeScannerScreen
import com.towhid.mlkit.presentation.facedetection.FaceDetectionScreen
import com.towhid.mlkit.presentation.languagedetection.LanguageIdScreen
import com.towhid.mlkit.presentation.objectdetection.ObjectDetectionScreen
import com.towhid.mlkit.presentation.samrtreply.SmartReplyScreen
import com.towhid.mlkit.presentation.textrecognition.TextRecognitionScreen

@Composable
fun MLKitApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen { navController.navigate("home"){
            popUpTo("splash") { inclusive = true }
        } } }
        composable("home") { HomeScreen(navController) }
        composable("textRecognition") { TextRecognitionScreen(navController) }
        composable("faceDetection") { FaceDetectionScreen(navController) }
        composable("objectDetection") { ObjectDetectionScreen(navController) }
        composable("smartReply") { SmartReplyScreen(navController) }
        composable("languageId") { LanguageIdScreen(navController) }
        composable("barcodeScanner") { BarcodeScannerScreen(navController) }
    }
}
