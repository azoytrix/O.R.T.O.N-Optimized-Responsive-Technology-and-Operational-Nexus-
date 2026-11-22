package com.azoytrix.orton_optimized_responsive_technology_and_operational_nexus_.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GlassDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF070A10),
    secondary = GlowingAmber,
    onSecondary = Color(0xFF070A10),
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextMuted,
    error = Color(0xFFFF5252),
    onError = Color.White
)

@Composable
fun ORTONOptimizedResponsiveTechnologyandOperationalNexusTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GlassDarkColorScheme,
        typography = Typography,
        content = content
    )
}
