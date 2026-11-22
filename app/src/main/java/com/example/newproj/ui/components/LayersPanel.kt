package com.example.newproj.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.LayerType
import com.example.newproj.model.StudioLayer
import com.example.newproj.ui.theme.*

@Composable
fun LayersPanel(
    layers: List<StudioLayer>,
    selectedLayerId: String,
    onSelectLayer: (String) -> Unit,
    onToggleVisibility: (String) -> Unit,
    onOpacityChange: (String, Float) -> Unit,
    onAddLayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Layer Panel Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Layers",
                    tint = StudioSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "LAYER STACK (${layers.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = StudioTextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(StudioPrimary.copy(alpha = 0.2f))
                    .border(0.5.dp, StudioPrimary, RoundedCornerShape(6.dp))
                    .clickable { onAddLayer() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Layer",
                        tint = StudioPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Add FX",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = StudioPrimary
                    )
                }
            }
        }

        // Layer List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 180.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(layers) { index, layer ->
                val isSelected = layer.id == selectedLayerId

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) StudioSurfaceElevated else StudioSurface)
                        .border(
                            1.dp,
                            if (isSelected) StudioPrimary else StudioSurfaceBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onSelectLayer(layer.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Layer Visibility Eye Toggle
                            IconButton(
                                onClick = { onToggleVisibility(layer.id) },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = if (layer.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Visibility",
                                    tint = if (layer.isVisible) StudioSecondary else StudioTextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = layer.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (layer.isVisible) StudioTextPrimary else StudioTextMuted
                                )

                                Text(
                                    text = "${layer.blendModeName} • ${(layer.opacity * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = StudioTextSecondary
                                )
                            }
                        }

                        // Blend mode badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(StudioSurfaceBorder)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "L${layers.size - index}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = StudioTextSecondary
                            )
                        }
                    }

                    // Opacity Slider for Selected Layer
                    if (isSelected) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Opacity",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = StudioTextMuted
                            )

                            Slider(
                                value = layer.opacity,
                                onValueChange = { onOpacityChange(layer.id, it) },
                                valueRange = 0f..1f,
                                colors = SliderDefaults.colors(
                                    thumbColor = StudioSecondary,
                                    activeTrackColor = StudioPrimary,
                                    inactiveTrackColor = StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(22.dp)
                            )

                            Text(
                                text = "${(layer.opacity * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
