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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.ImageAdjustments
import com.example.newproj.ui.theme.*

private enum class AdjustmentTab(val title: String) {
    LIGHT("Light"),
    COLOR("Color"),
    EFFECTS("Effects")
}

@Composable
fun AdjustmentsPanel(
    adjustments: ImageAdjustments,
    onAdjustmentsChange: (ImageAdjustments) -> Unit,
    onResetAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(AdjustmentTab.LIGHT) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Row: Histogram & Quick Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HistogramView(
                adjustments = adjustments,
                modifier = Modifier.weight(1f)
            )

            // Auto-Enhance Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberGlowGradient)
                        .clickable {
                            onAdjustmentsChange(
                                adjustments.copy(
                                    brightness = 14f,
                                    contrast = 22f,
                                    saturation = 28f,
                                    warmth = 8f,
                                    vignette = 30f
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = "Auto Enhance",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Auto HDR",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = StudioTextSecondary
                )
            }
        }

        // Subcategory Pills (Light / Color / Effects)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AdjustmentTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceElevated)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Color.White else StudioTextSecondary
                        )
                    }
                }
            }

            if (!adjustments.isDefault()) {
                Text(
                    text = "Reset All",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioAccent
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onResetAll() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }

        // Adjustment Sliders based on Selected Tab
        when (selectedTab) {
            AdjustmentTab.LIGHT -> {
                AdjustmentSliderItem(
                    label = "Brightness",
                    icon = Icons.Default.Brightness5,
                    value = adjustments.brightness,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(brightness = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(brightness = 0f)) }
                )
                AdjustmentSliderItem(
                    label = "Contrast",
                    icon = Icons.Default.Contrast,
                    value = adjustments.contrast,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(contrast = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(contrast = 0f)) }
                )
                AdjustmentSliderItem(
                    label = "Exposure",
                    icon = Icons.Default.WbSunny,
                    value = adjustments.exposure,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(exposure = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(exposure = 0f)) }
                )
            }
            AdjustmentTab.COLOR -> {
                AdjustmentSliderItem(
                    label = "Saturation",
                    icon = Icons.Default.ColorLens,
                    value = adjustments.saturation,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(saturation = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(saturation = 0f)) }
                )
                AdjustmentSliderItem(
                    label = "Warmth / Temperature",
                    icon = Icons.Default.DeviceThermostat,
                    value = adjustments.warmth,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(warmth = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(warmth = 0f)) }
                )
                AdjustmentSliderItem(
                    label = "Tint",
                    icon = Icons.Default.Palette,
                    value = adjustments.tint,
                    range = -100f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(tint = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(tint = 0f)) }
                )
            }
            AdjustmentTab.EFFECTS -> {
                AdjustmentSliderItem(
                    label = "Vignette",
                    icon = Icons.Default.FilterTiltShift,
                    value = adjustments.vignette,
                    range = 0f..100f,
                    onValueChange = { onAdjustmentsChange(adjustments.copy(vignette = it)) },
                    onReset = { onAdjustmentsChange(adjustments.copy(vignette = 0f)) }
                )
            }
        }
    }
}

@Composable
private fun AdjustmentSliderItem(
    label: String,
    icon: ImageVector,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    onReset: () -> Unit
) {
    val displayValue = value.toInt()
    val isModified = displayValue != 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isModified) StudioSecondary else StudioTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = StudioTextPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (displayValue > 0) "+$displayValue" else "$displayValue",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isModified) StudioSecondary else StudioTextMuted
                    )
                )

                if (isModified) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(StudioSurfaceBorder)
                            .clickable { onReset() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Reset",
                            tint = StudioTextSecondary,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = StudioSecondary,
                activeTrackColor = StudioPrimary,
                inactiveTrackColor = StudioSurfaceBorder
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
        )
    }
}
