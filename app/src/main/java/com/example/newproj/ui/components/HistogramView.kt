package com.example.newproj.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.ImageAdjustments
import com.example.newproj.ui.theme.*
import kotlin.math.sin

@Composable
fun HistogramView(
    adjustments: ImageAdjustments,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(StudioSurface.copy(alpha = 0.85f))
            .border(0.5.dp, StudioSurfaceBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RGB HISTOGRAM",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = StudioTextMuted
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "R", color = Color(0xFFFF5252), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Text(text = "G", color = Color(0xFF69F0AE), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Text(text = "B", color = Color(0xFF40C4FF), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            ) {
                val width = size.width
                val height = size.height
                val steps = 30
                val stepWidth = width / steps

                // Shift values dynamically influenced by image adjustments
                val bShift = (adjustments.brightness / 100f) * 0.25f
                val cMultiplier = 1f + (adjustments.contrast / 100f) * 0.5f
                val wShift = (adjustments.warmth / 100f) * 0.2f

                // Draw Red Curve
                val redPath = Path()
                redPath.moveTo(0f, height)
                for (i in 0..steps) {
                    val progress = i.toFloat() / steps
                    val peakCenter = 0.45f + bShift + wShift
                    val dist = kotlin.math.abs(progress - peakCenter)
                    val curve = kotlin.math.max(0f, (1f - dist * 2.8f * cMultiplier))
                    val y = height - (curve * height * 0.85f)
                    redPath.lineTo(i * stepWidth, y.coerceIn(2f, height))
                }
                drawPath(
                    path = redPath,
                    color = Color(0xFFFF5252).copy(alpha = 0.55f),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw Green Curve
                val greenPath = Path()
                greenPath.moveTo(0f, height)
                for (i in 0..steps) {
                    val progress = i.toFloat() / steps
                    val peakCenter = 0.50f + bShift
                    val dist = kotlin.math.abs(progress - peakCenter)
                    val curve = kotlin.math.max(0f, (1f - dist * 2.5f * cMultiplier))
                    val y = height - (curve * height * 0.88f)
                    greenPath.lineTo(i * stepWidth, y.coerceIn(2f, height))
                }
                drawPath(
                    path = greenPath,
                    color = Color(0xFF69F0AE).copy(alpha = 0.55f),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Draw Blue Curve
                val bluePath = Path()
                bluePath.moveTo(0f, height)
                for (i in 0..steps) {
                    val progress = i.toFloat() / steps
                    val peakCenter = 0.55f + bShift - wShift
                    val dist = kotlin.math.abs(progress - peakCenter)
                    val curve = kotlin.math.max(0f, (1f - dist * 2.6f * cMultiplier))
                    val y = height - (curve * height * 0.82f)
                    bluePath.lineTo(i * stepWidth, y.coerceIn(2f, height))
                }
                drawPath(
                    path = bluePath,
                    color = Color(0xFF40C4FF).copy(alpha = 0.55f),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}
