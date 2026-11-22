package com.example.newproj.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.FilterPreset
import com.example.newproj.model.ImageAdjustments
import com.example.newproj.ui.theme.*

val SAMPLE_PRESETS = listOf(
    FilterPreset(
        id = "cyberpunk",
        name = "Cyberpunk 2077",
        subtitle = "Neon High-Contrast",
        previewColors = listOf(Color(0xFF00E5FF), Color(0xFFD946EF), Color(0xFF0D0E1A)),
        adjustments = ImageAdjustments(brightness = 15f, contrast = 35f, saturation = 45f, warmth = -15f, vignette = 40f)
    ),
    FilterPreset(
        id = "tokyo_noir",
        name = "Tokyo Noir",
        subtitle = "Deep Black & Crushed",
        previewColors = listOf(Color(0xFFFFFFFF), Color(0xFF555555), Color(0xFF000000)),
        adjustments = ImageAdjustments(brightness = -10f, contrast = 50f, saturation = -100f, vignette = 55f)
    ),
    FilterPreset(
        id = "golden_hour",
        name = "Golden Sunset",
        subtitle = "Warm Amber Glow",
        previewColors = listOf(Color(0xFFFFB300), Color(0xFFFF7043), Color(0xFF4A148C)),
        adjustments = ImageAdjustments(brightness = 10f, contrast = 15f, saturation = 25f, warmth = 45f, vignette = 20f)
    ),
    FilterPreset(
        id = "emerald_synth",
        name = "Emerald Synth",
        subtitle = "Matrix Cyber Jade",
        previewColors = listOf(Color(0xFF00E676), Color(0xFF1DE9B6), Color(0xFF0A192F)),
        adjustments = ImageAdjustments(brightness = 8f, contrast = 30f, saturation = 35f, warmth = -35f, vignette = 30f)
    ),
    FilterPreset(
        id = "pastel_dream",
        name = "Pastel Dream",
        subtitle = "Soft Ethereal Lift",
        previewColors = listOf(Color(0xFFF48FB1), Color(0xFFCE93D8), Color(0xFF90CAF9)),
        adjustments = ImageAdjustments(brightness = 20f, contrast = -15f, saturation = 10f, warmth = 10f, vignette = 0f)
    ),
    FilterPreset(
        id = "solar_flare",
        name = "Solar Flare",
        subtitle = "Intense Warm Beam",
        previewColors = listOf(Color(0xFFFFD600), Color(0xFFFF3D00), Color(0xFFDD2C00)),
        adjustments = ImageAdjustments(brightness = 25f, contrast = 25f, saturation = 40f, warmth = 50f, vignette = 15f)
    )
)

@Composable
fun FilterPresetsPanel(
    selectedPresetId: String?,
    onPresetSelect: (FilterPreset) -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var filterIntensity by remember { mutableFloatStateOf(100f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Preset Header & Intensity Slider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "COLOR GRADING PRESETS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = StudioTextSecondary
            )

            if (selectedPresetId != null) {
                Text(
                    text = "Clear Preset",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioAccent
                    ),
                    modifier = Modifier
                        .clickable { onResetFilters() }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        // Horizontal Carousel of Presets
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SAMPLE_PRESETS) { preset ->
                val isSelected = selectedPresetId == preset.id

                Column(
                    modifier = Modifier
                        .width(96.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) StudioSurfaceElevated else StudioSurface)
                        .border(
                            1.5.dp,
                            if (isSelected) StudioSecondary else StudioSurfaceBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onPresetSelect(preset) }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Visual Color Gradient Swatch
                    Box(
                        modifier = Modifier
                            .size(76.dp, 56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(preset.previewColors)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(StudioSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = preset.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = if (isSelected) StudioTextPrimary else StudioTextSecondary,
                        maxLines = 1
                    )

                    Text(
                        text = preset.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = StudioTextMuted,
                        maxLines = 1
                    )
                }
            }
        }

        // Filter Intensity Slider (when preset active)
        AnimatedVisibility(visible = selectedPresetId != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Preset Strength",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        color = StudioTextSecondary
                    )
                    Text(
                        text = "${filterIntensity.toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudioSecondary
                        )
                    )
                }

                Slider(
                    value = filterIntensity,
                    onValueChange = {
                        filterIntensity = it
                        // recalculate based on preset
                        val p = SAMPLE_PRESETS.find { preset -> preset.id == selectedPresetId }
                        if (p != null) {
                            val factor = it / 100f
                            onPresetSelect(
                                p.copy(
                                    adjustments = ImageAdjustments(
                                        brightness = p.adjustments.brightness * factor,
                                        contrast = p.adjustments.contrast * factor,
                                        saturation = p.adjustments.saturation * factor,
                                        warmth = p.adjustments.warmth * factor,
                                        vignette = p.adjustments.vignette * factor
                                    )
                                )
                            )
                        }
                    },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioSecondary,
                        activeTrackColor = StudioPrimary,
                        inactiveTrackColor = StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                )
            }
        }
    }
}
