package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GlassLightColorScheme = lightColorScheme(
    primary = PrimaryCyan,
    onPrimary = Color.White,
    secondary = AccentAmber,
    onSecondary = Color.White,
    background = LightBackgroundStart,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    surfaceVariant = GlassSurfaceLight,
    onSurfaceVariant = TextMuted,
    error = Color(0xFFEF4444),
    onError = Color.White
)

@Composable
fun ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GlassLightColorScheme,
        typography = Typography,
        content = content
    )
}
