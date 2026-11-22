package com.example.newproj.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.AspectRatioOption
import com.example.newproj.ui.theme.*

@Composable
fun CropTransformPanel(
    selectedRatio: AspectRatioOption,
    onRatioSelected: (AspectRatioOption) -> Unit,
    rotationAngle: Float,
    onRotateClockwise: () -> Unit,
    onRotateCounterClockwise: () -> Unit,
    isFlippedH: Boolean,
    isFlippedV: Boolean,
    onToggleFlipH: () -> Unit,
    onToggleFlipV: () -> Unit,
    showGrid: Boolean,
    onToggleGrid: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Aspect Ratio Selector Header
        Text(
            text = "CANVAS ASPECT RATIO",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            ),
            color = StudioTextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Aspect Ratio Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            items(AspectRatioOption.values()) { ratio ->
                val isSelected = selectedRatio == ratio
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) StudioPrimary else StudioSurfaceElevated)
                        .border(
                            1.dp,
                            if (isSelected) StudioSecondary else StudioSurfaceBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onRatioSelected(ratio) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = ratio.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Color.White else StudioTextPrimary
                        )
                    }
                }
            }
        }

        // Transform Tool Buttons (Rotate, Flip, Grid)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransformActionButton(
                icon = Icons.AutoMirrored.Filled.RotateLeft,
                label = "Rotate -90°",
                isActive = false,
                onClick = onRotateCounterClockwise
            )

            TransformActionButton(
                icon = Icons.AutoMirrored.Filled.RotateRight,
                label = "Rotate +90°",
                isActive = false,
                onClick = onRotateClockwise
            )

            TransformActionButton(
                icon = Icons.Default.Flip,
                label = "Flip H",
                isActive = isFlippedH,
                onClick = onToggleFlipH
            )

            TransformActionButton(
                icon = Icons.Default.SwapVert,
                label = "Flip V",
                isActive = isFlippedV,
                onClick = onToggleFlipV
            )

            TransformActionButton(
                icon = Icons.Default.GridOn,
                label = "Grid 3x3",
                isActive = showGrid,
                onClick = onToggleGrid
            )
        }
    }
}

@Composable
private fun TransformActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isActive) StudioSecondary.copy(alpha = 0.2f) else StudioSurfaceElevated)
                .border(
                    1.dp,
                    if (isActive) StudioSecondary else StudioSurfaceBorder,
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) StudioSecondary else StudioTextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = if (isActive) StudioSecondary else StudioTextMuted
        )
    }
}
