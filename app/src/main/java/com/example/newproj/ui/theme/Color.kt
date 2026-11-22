package com.example.newproj.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val StudioDarkBg = Color(0xFF090A10)
val StudioSurface = Color(0xFF121420)
val StudioSurfaceElevated = Color(0xFF1B1E30)
val StudioSurfaceBorder = Color(0xFF282C44)

val StudioPrimary = Color(0xFF8B5CF6)       // Electric Violet
val StudioSecondary = Color(0xFF06B6D4)     // Cyber Cyan
val StudioAccent = Color(0xFFF43F5E)        // Neon Pink
val StudioSuccess = Color(0xFF10B981)
val StudioWarning = Color(0xFFF59E0B)

val StudioTextPrimary = Color(0xFFF8FAFC)
val StudioTextSecondary = Color(0xFF94A3B8)
val StudioTextMuted = Color(0xFF64748B)

val GlassOverlay = Color(0x2A1E2238)
val GlassBorder = Color(0x33FFFFFF)

val NeonPurpleGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF8B5CF6), Color(0xFFD946EF))
)

val CyberGlowGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF8B5CF6), Color(0xFFF43F5E))
)

val DarkSurfaceGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF161828), Color(0xFF0E0F1A))
)
