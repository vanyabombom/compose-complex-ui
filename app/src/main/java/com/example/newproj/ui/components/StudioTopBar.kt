package com.example.newproj.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.ui.theme.*

@Composable
fun StudioTopBar(
    canUndo: Boolean,
    canRedo: Boolean,
    isCompareActive: Boolean,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onCompareToggle: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = StudioDarkBg,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding & Status Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonPurpleGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "LUMINA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            ),
                            color = StudioTextPrimary
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(StudioPrimary.copy(alpha = 0.25f))
                                .border(0.5.dp, StudioPrimary, RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PRO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = StudioSecondary
                            )
                        }
                    }

                    Text(
                        text = "Creative Studio • 32-bit HDR",
                        style = MaterialTheme.typography.labelSmall,
                        color = StudioTextMuted
                    )
                }
            }

            // Top Action Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Undo Button
                IconButton(
                    onClick = onUndoClick,
                    enabled = canUndo,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (canUndo) StudioSurfaceElevated else StudioSurface.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (canUndo) StudioTextPrimary else StudioTextMuted.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Redo Button
                IconButton(
                    onClick = onRedoClick,
                    enabled = canRedo,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (canRedo) StudioSurfaceElevated else StudioSurface.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (canRedo) StudioTextPrimary else StudioTextMuted.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Compare Mode Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isCompareActive) StudioSecondary.copy(alpha = 0.25f)
                            else StudioSurfaceElevated
                        )
                        .border(
                            1.dp,
                            if (isCompareActive) StudioSecondary else StudioSurfaceBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onCompareToggle() }
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = "Compare",
                            tint = if (isCompareActive) StudioSecondary else StudioTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Split",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isCompareActive) StudioSecondary else StudioTextSecondary
                        )
                    }
                }

                // Export Action Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurpleGradient)
                        .clickable { onExportClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Export",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Export",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
