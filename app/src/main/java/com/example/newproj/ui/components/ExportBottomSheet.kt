package com.example.newproj.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.newproj.model.ExportFormat
import com.example.newproj.model.ExportResolution
import com.example.newproj.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFormat by remember { mutableStateOf(ExportFormat.JPEG) }
    var selectedResolution by remember { mutableStateOf(ExportResolution.FHD) }
    var quality by remember { mutableFloatStateOf(92f) }

    var isRendering by remember { mutableStateOf(false) }
    var renderProgress by remember { mutableFloatStateOf(0f) }
    var isSuccess by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = StudioSurface,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(StudioSurfaceBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Export Artwork",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = StudioTextPrimary
                    )
                    Text(
                        text = "Lossless Color Grading Pipeline",
                        style = MaterialTheme.typography.labelSmall,
                        color = StudioTextMuted
                    )
                }

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = StudioTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // File Format Selector
            Text(
                text = "FORMAT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = StudioTextSecondary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExportFormat.values().forEach { fmt ->
                    val isSelected = selectedFormat == fmt
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceElevated)
                            .border(
                                1.dp,
                                if (isSelected) StudioSecondary else StudioSurfaceBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedFormat = fmt }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = fmt.extension,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) Color.White else StudioTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Resolution Selector
            Text(
                text = "RENDER RESOLUTION",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = StudioTextSecondary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExportResolution.values().forEach { res ->
                    val isSelected = selectedResolution == res
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) StudioPrimary else StudioSurfaceElevated)
                            .border(
                                1.dp,
                                if (isSelected) StudioSecondary else StudioSurfaceBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedResolution = res }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = res.label,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else StudioTextPrimary
                            )
                            Text(
                                text = res.dimensions,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = if (isSelected) StudioTextPrimary.copy(alpha = 0.8f) else StudioTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quality Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Compression Quality",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = StudioTextPrimary
                )
                Text(
                    text = "${quality.toInt()}%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondary
                    )
                )
            }

            Slider(
                value = quality,
                onValueChange = { quality = it },
                valueRange = 20f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = StudioSecondary,
                    activeTrackColor = StudioPrimary,
                    inactiveTrackColor = StudioSurfaceBorder
                ),
                modifier = Modifier.height(28.dp)
            )

            // Info Estimate
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(StudioSurfaceElevated)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estimated File Size",
                    style = MaterialTheme.typography.labelSmall,
                    color = StudioTextMuted
                )
                val estSize = when (selectedResolution) {
                    ExportResolution.FHD -> "2.4 MB"
                    ExportResolution.QHD -> "5.1 MB"
                    ExportResolution.UHD -> "11.8 MB"
                }
                Text(
                    text = estSize,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Render / Export Button or Progress State
            if (isSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(StudioSuccess.copy(alpha = 0.2f))
                        .border(1.dp, StudioSuccess, RoundedCornerShape(10.dp))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = StudioSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Artwork Exported Successfully!",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = StudioSuccess
                        )
                    }
                }
            } else if (isRendering) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { renderProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = StudioSecondary,
                        trackColor = StudioSurfaceBorder
                    )
                    Text(
                        text = "Rendering multi-pass frame... ${(renderProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = StudioTextSecondary
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NeonPurpleGradient)
                        .clickable {
                            isRendering = true
                            renderProgress = 0f
                            coroutineScope.launch {
                                for (i in 1..10) {
                                    delay(80)
                                    renderProgress = i / 10f
                                }
                                delay(150)
                                isRendering = false
                                isSuccess = true
                                delay(1200)
                                onDismissRequest()
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Render",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Render & Save Image",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
