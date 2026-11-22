package com.example.newproj.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.AspectRatioOption
import com.example.newproj.model.ImageAdjustments
import com.example.newproj.model.LayerType
import com.example.newproj.model.StudioLayer
import com.example.newproj.ui.theme.*

@Composable
fun CanvasWorkspace(
    adjustments: ImageAdjustments,
    layers: List<StudioLayer>,
    aspectRatio: AspectRatioOption,
    rotationAngle: Float,
    isFlippedH: Boolean,
    isFlippedV: Boolean,
    showGrid: Boolean,
    isCompareActive: Boolean,
    modifier: Modifier = Modifier
) {
    var splitFraction by remember { mutableFloatStateOf(0.5f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        val targetRatio = aspectRatio.ratio ?: 1.0f

        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .aspectRatio(targetRatio)
                .shadow(16.dp, RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF07080D))
                .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(14.dp))
                .graphicsLayer {
                    rotationZ = rotationAngle
                    scaleX = if (isFlippedH) -1f else 1f
                    scaleY = if (isFlippedV) -1f else 1f
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (isCompareActive) {
                    val splitX = size.width * splitFraction

                    // Draw Original (no adjustments) on the Left
                    clipRect(left = 0f, top = 0f, right = splitX, bottom = size.height) {
                        drawArtLayers(
                            adjustments = ImageAdjustments(),
                            layers = layers,
                            canvasSize = size
                        )
                    }

                    // Draw Adjusted on the Right
                    clipRect(left = splitX, top = 0f, right = size.width, bottom = size.height) {
                        drawArtLayers(
                            adjustments = adjustments,
                            layers = layers,
                            canvasSize = size
                        )
                    }

                    // Split Line
                    drawLine(
                        color = StudioSecondary,
                        start = Offset(splitX, 0f),
                        end = Offset(splitX, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                } else {
                    drawArtLayers(
                        adjustments = adjustments,
                        layers = layers,
                        canvasSize = size
                    )
                }

                // Rule-of-thirds grid
                if (showGrid) {
                    val stroke = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                    val oneThirdX = size.width / 3f
                    val twoThirdX = size.width * 2f / 3f
                    val oneThirdY = size.height / 3f
                    val twoThirdY = size.height * 2f / 3f

                    val gridColor = Color.White.copy(alpha = 0.35f)
                    drawLine(gridColor, Offset(oneThirdX, 0f), Offset(oneThirdX, size.height), strokeWidth = 1.dp.toPx())
                    drawLine(gridColor, Offset(twoThirdX, 0f), Offset(twoThirdX, size.height), strokeWidth = 1.dp.toPx())
                    drawLine(gridColor, Offset(0f, oneThirdY), Offset(size.width, oneThirdY), strokeWidth = 1.dp.toPx())
                    drawLine(gridColor, Offset(0f, twoThirdY), Offset(size.width, twoThirdY), strokeWidth = 1.dp.toPx())
                }
            }

            // Interactive Split Divider Handle
            if (isCompareActive) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newFrac = (splitFraction + (dragAmount.x / size.width)).coerceIn(0.05f, 0.95f)
                                splitFraction = newFrac
                            }
                        }
                ) {
                    // Draggable Center Handle
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = ((300 * splitFraction) - 16).dp) // approximate visual offset
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(StudioSecondary)
                            .shadow(8.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.UnfoldMore,
                            contentDescription = "Split Handle",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(18.dp)
                                .graphicsLayer { rotationZ = 90f }
                        )
                    }

                    // Side Badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "RAW ORIGINAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StudioPrimary.copy(alpha = 0.75f))
                                .border(0.5.dp, StudioSecondary, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "EDITED HDR",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawArtLayers(
    adjustments: ImageAdjustments,
    layers: List<StudioLayer>,
    canvasSize: Size
) {
    val w = canvasSize.width
    val h = canvasSize.height

    // Calculate dynamic color tone adjustments
    val bFactor = (adjustments.brightness / 100f) * 0.3f
    val cFactor = 1f + (adjustments.contrast / 100f) * 0.4f
    val sFactor = 1f + (adjustments.saturation / 100f) * 0.5f
    val wFactor = (adjustments.warmth / 100f) * 0.25f

    // 1. Base Sky Gradient Layer
    val skyLayer = layers.find { it.type == LayerType.BACKGROUND_SKY }
    if (skyLayer?.isVisible != false) {
        val skyAlpha = skyLayer?.opacity ?: 1f
        val topSky = Color(
            red = ((0.05f + bFactor + wFactor) * cFactor).coerceIn(0f, 1f),
            green = ((0.04f + bFactor) * cFactor).coerceIn(0f, 1f),
            blue = ((0.18f + bFactor - wFactor) * cFactor).coerceIn(0f, 1f),
            alpha = skyAlpha
        )
        val midSky = Color(
            red = ((0.28f + bFactor + wFactor) * cFactor).coerceIn(0f, 1f),
            green = ((0.08f + bFactor) * cFactor).coerceIn(0f, 1f),
            blue = ((0.42f + bFactor - wFactor) * cFactor).coerceIn(0f, 1f),
            alpha = skyAlpha
        )
        val bottomSky = Color(
            red = ((0.55f + bFactor + wFactor) * cFactor).coerceIn(0f, 1f),
            green = ((0.15f + bFactor + wFactor * 0.5f) * cFactor).coerceIn(0f, 1f),
            blue = ((0.25f + bFactor) * cFactor).coerceIn(0f, 1f),
            alpha = skyAlpha
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(topSky, midSky, bottomSky),
                startY = 0f,
                endY = h * 0.75f
            ),
            size = canvasSize
        )
    }

    // 2. Stars & Aurora Layer
    val auroraLayer = layers.find { it.type == LayerType.NEBULA_AURORA }
    if (auroraLayer?.isVisible != false) {
        val auroraAlpha = (auroraLayer?.opacity ?: 1f) * 0.7f

        // Starfield dots
        val starPositions = listOf(
            Offset(w * 0.12f, h * 0.15f), Offset(w * 0.28f, h * 0.08f),
            Offset(w * 0.45f, h * 0.22f), Offset(w * 0.68f, h * 0.12f),
            Offset(w * 0.85f, h * 0.18f), Offset(w * 0.92f, h * 0.32f),
            Offset(w * 0.18f, h * 0.38f), Offset(w * 0.75f, h * 0.26f)
        )
        starPositions.forEachIndexed { i, pos ->
            val radius = if (i % 2 == 0) 2.dp.toPx() else 1.2.dp.toPx()
            drawCircle(
                color = Color.White.copy(alpha = auroraAlpha * 0.9f),
                radius = radius,
                center = pos
            )
        }

        // Aurora Ribbon Wave
        val auroraPath = Path().apply {
            moveTo(0f, h * 0.35f)
            cubicTo(w * 0.3f, h * 0.2f, w * 0.6f, h * 0.45f, w, h * 0.28f)
            lineTo(w, h * 0.55f)
            cubicTo(w * 0.6f, h * 0.65f, w * 0.3f, h * 0.4f, 0f, h * 0.55f)
            close()
        }
        drawPath(
            path = auroraPath,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF00E5FF).copy(alpha = auroraAlpha * 0.5f),
                    Color(0xFFD946EF).copy(alpha = auroraAlpha * 0.6f),
                    Color(0xFF8B5CF6).copy(alpha = auroraAlpha * 0.4f)
                )
            )
        )
    }

    // 3. Cyberpunk Geometric Sun
    val sunLayer = layers.find { it.type == LayerType.GEOMETRIC_SUN }
    if (sunLayer?.isVisible != false) {
        val sunAlpha = sunLayer?.opacity ?: 1f
        val sunCenter = Offset(w * 0.5f, h * 0.46f)
        val sunRadius = w * 0.22f

        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFEE58).copy(alpha = sunAlpha),
                    Color(0xFFFF007F).copy(alpha = sunAlpha)
                ),
                startY = sunCenter.y - sunRadius,
                endY = sunCenter.y + sunRadius
            ),
            radius = sunRadius,
            center = sunCenter
        )

        // Sun Horizontal Slice Lines
        for (i in 1..5) {
            val sliceY = sunCenter.y + (sunRadius * (i / 6.5f))
            val sliceHeight = (i * 2.2f).dp.toPx()
            drawRect(
                color = Color(0xFF161828).copy(alpha = sunAlpha * 0.85f),
                topLeft = Offset(sunCenter.x - sunRadius, sliceY),
                size = Size(sunRadius * 2, sliceHeight)
            )
        }
    }

    // 4. Cyber Skyline Silhouettes
    val skylineLayer = layers.find { it.type == LayerType.CYBER_SKYLINE }
    if (skylineLayer?.isVisible != false) {
        val skylineAlpha = skylineLayer?.opacity ?: 1f
        val horizonY = h * 0.62f

        val buildings = listOf(
            Rect(w * 0.04f, horizonY - h * 0.22f, w * 0.16f, horizonY),
            Rect(w * 0.18f, horizonY - h * 0.34f, w * 0.29f, horizonY),
            Rect(w * 0.31f, horizonY - h * 0.20f, w * 0.42f, horizonY),
            Rect(w * 0.45f, horizonY - h * 0.38f, w * 0.57f, horizonY),
            Rect(w * 0.60f, horizonY - h * 0.26f, w * 0.70f, horizonY),
            Rect(w * 0.73f, horizonY - h * 0.33f, w * 0.84f, horizonY),
            Rect(w * 0.86f, horizonY - h * 0.19f, w * 0.96f, horizonY)
        )

        buildings.forEach { b ->
            drawRect(
                color = Color(0xFF0A0C16).copy(alpha = skylineAlpha),
                topLeft = Offset(b.left, b.top),
                size = Size(b.width, b.height)
            )

            // Neon roof antenna/spire
            drawLine(
                color = Color(0xFF00E5FF).copy(alpha = skylineAlpha * 0.8f),
                start = Offset(b.left + b.width / 2f, b.top),
                end = Offset(b.left + b.width / 2f, b.top - 12.dp.toPx()),
                strokeWidth = 1.5.dp.toPx()
            )

            // Glowing windows matrix
            val windowRows = 5
            val windowCols = 3
            val cellW = b.width / (windowCols + 1)
            val cellH = b.height / (windowRows + 2)

            for (r in 1..windowRows) {
                for (c in 1..windowCols) {
                    if ((r + c) % 2 == 0) {
                        drawRect(
                            color = if (r % 3 == 0) Color(0xFFFF007F).copy(alpha = skylineAlpha * 0.7f)
                            else Color(0xFF00E5FF).copy(alpha = skylineAlpha * 0.7f),
                            topLeft = Offset(b.left + c * cellW, b.top + r * cellH),
                            size = Size(3.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }
        }
    }

    // 5. 3D Perspective Neon Grid Horizon
    val gridLayer = layers.find { it.type == LayerType.PERSPECTIVE_GRID }
    if (gridLayer?.isVisible != false) {
        val gridAlpha = gridLayer?.opacity ?: 1f
        val horizonY = h * 0.62f

        // Dark reflective floor
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF090A14), Color(0xFF030308)),
                startY = horizonY,
                endY = h
            ),
            topLeft = Offset(0f, horizonY),
            size = Size(w, h - horizonY)
        )

        val gridColor = Color(0xFFD946EF).copy(alpha = gridAlpha * 0.6f)

        // Horizontal lines expanding downwards
        val hLines = 9
        for (i in 0..hLines) {
            val progress = (i.toFloat() / hLines)
            val expY = horizonY + (progress * progress) * (h - horizonY)
            drawLine(
                color = gridColor,
                start = Offset(0f, expY),
                end = Offset(w, expY),
                strokeWidth = (1f + progress * 2f).dp.toPx()
            )
        }

        // Perspective fan lines
        val vLines = 11
        val vanishPoint = Offset(w * 0.5f, horizonY)
        for (i in 0..vLines) {
            val bottomX = (w / vLines) * i
            drawLine(
                color = gridColor,
                start = vanishPoint,
                end = Offset(bottomX, h),
                strokeWidth = 1.2.dp.toPx()
            )
        }
    }

    // 6. Typography Watermark Layer
    val typoLayer = layers.find { it.type == LayerType.TYPOGRAPHY_WATERMARK }
    if (typoLayer?.isVisible != false) {
        val typoAlpha = typoLayer?.opacity ?: 1f
        val badgeX = w * 0.06f
        val badgeY = h * 0.06f

        drawRoundRect(
            color = Color.Black.copy(alpha = typoAlpha * 0.5f),
            topLeft = Offset(badgeX, badgeY),
            size = Size(110.dp.toPx(), 26.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF00E5FF).copy(alpha = typoAlpha),
            radius = 3.5.dp.toPx(),
            center = Offset(badgeX + 14.dp.toPx(), badgeY + 13.dp.toPx())
        )
    }

    // 7. Vignette Overlay Layer
    val vigValue = adjustments.vignette
    val vigLayer = layers.find { it.type == LayerType.VIGNETTE_OVERLAY }
    if ((vigLayer?.isVisible != false) && (vigValue > 0f || vigLayer != null)) {
        val vigStrength = ((vigValue / 100f) * 0.85f * (vigLayer?.opacity ?: 1f)).coerceIn(0f, 1f)
        if (vigStrength > 0.01f) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = vigStrength * 0.6f),
                        Color.Black.copy(alpha = vigStrength)
                    ),
                    center = Offset(w * 0.5f, h * 0.5f),
                    radius = kotlin.math.max(w, h) * 0.7f
                ),
                size = canvasSize
            )
        }
    }
}
