package com.towhid.mlkit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🎨 Your brand colors
val PrimaryColor = Color(0xFF036C61)
val OnPrimaryColor = Color.White

val SecondaryColor = Color(0xFF4FB3A5)
val OnSecondaryColor = Color.White

val TertiaryColor = Color(0xFF84DCC6)
val OnTertiaryColor = Color.Black

val BackgroundLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF000000)

val BackgroundDark = Color(0xFF121212)
val OnBackgroundDark = Color(0xFFFFFFFF)

// 🌞 Light theme colors
private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    tertiary = TertiaryColor,
    onTertiary = OnTertiaryColor,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = Color.White,
    onSurface = Color.Black
)

// 🌙 Dark theme colors
private val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    tertiary = TertiaryColor,
    onTertiary = OnTertiaryColor,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)


@Composable
fun MLKitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
*/
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}