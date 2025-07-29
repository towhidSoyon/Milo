package com.towhid.mlkit.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.towhid.mlkit.R

@Composable
fun SplashScreen(navToHome: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        navToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2FC675)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(id = R.drawable.miloicon), contentDescription = "Milo Logo")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Milo", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text("Scan. Understand. Respond.", fontSize = 14.sp, color = Color.White)
        }
    }
}
