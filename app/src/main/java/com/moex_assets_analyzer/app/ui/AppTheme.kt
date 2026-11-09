package com.moex_assets_analyzer.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



val Chalk = Color(0xFFFAFBFC)        // фон
val Paper = Color(0xFFFFFFFF)        // поверхности
val Ink = Color(0xFF10131A)          // основной текст
val Slate = Color(0xFF5A6272)        // второстепенный текст
val Mist = Color(0xFFDCE1EA)         // тонкие границы
val Cobalt = Color(0xFF1F4BFF)       // бренд
val Vermilion = Color(0xFFFF5A36)    // акцент
val GainGreen = Color(0xFF1E8E5A)    // рост
val LossRed = Color(0xFFD6334A)      // падение

private val AppColorScheme = lightColorScheme(
    primary = Cobalt,
    onPrimary = Color.White,
    secondary = Vermilion,
    onSecondary = Color.White,
    tertiary = GainGreen,
    onTertiary = Color.White,
    background = Chalk,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    onSurfaceVariant = Slate,
    error = LossRed,
    onError = Color.White,
    outline = Mist,
    outlineVariant = Mist
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme
    ){
        content()
    }
}