package com.example.newproj.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.StudioTool
import com.example.newproj.ui.theme.*

@Composable
fun StudioBottomNavigation(
    selectedTool: StudioTool,
    onSelectTool: (StudioTool) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = StudioDarkBg,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudioTool.values().forEach { tool ->
                val isSelected = selectedTool == tool
                val icon = when (tool) {
                    StudioTool.ADJUST -> Icons.Default.Tune
                    StudioTool.FILTERS -> Icons.Default.ColorLens
                    StudioTool.LAYERS -> Icons.Default.Layers
                    StudioTool.TRANSFORM -> Icons.Default.Crop
                    StudioTool.FX -> Icons.Default.AutoFixNormal
                }

                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) StudioSecondary else StudioTextSecondary,
                    label = "IconTint"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectTool(tool) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) StudioPrimary.copy(alpha = 0.25f)
                                else Color.Transparent
                            )
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) StudioPrimary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tool.label,
                            tint = iconTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = tool.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSelected) StudioTextPrimary else StudioTextMuted
                    )
                }
            }
        }
    }
}
