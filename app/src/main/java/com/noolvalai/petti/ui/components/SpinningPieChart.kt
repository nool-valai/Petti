package com.noolvalai.petti.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.noolvalai.petti.ui.theme.LocalHighContrast
import com.noolvalai.petti.ui.theme.*

@Composable
fun SpinningPieChart(modifier: Modifier = Modifier) {
    val isHighContrast = LocalHighContrast.current

    // Resolve colors based on user preference
    val cSafe = if (isHighContrast) HcSafe else BentoSafe
    val cNotice = if (isHighContrast) HcNotice else BentoNotice
    val cWarning = if (isHighContrast) HcWarning else BentoWarning
    val cCritical = if (isHighContrast) HcCritical else BentoCritical

    val infiniteTransition = rememberInfiniteTransition(label = "Spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Rotation"
    )

    Canvas(modifier = modifier.size(100.dp)) {
        val sweepAngle = 90f
        val strokeWidth = 24.dp.toPx()

        rotate(rotation) {
            // Draw four equal arcs for the pie chart
            drawArc(cSafe, 0f, sweepAngle, useCenter = false, style = Stroke(strokeWidth))
            drawArc(cNotice, 90f, sweepAngle, useCenter = false, style = Stroke(strokeWidth))
            drawArc(cWarning, 180f, sweepAngle, useCenter = false, style = Stroke(strokeWidth))
            drawArc(cCritical, 270f, sweepAngle, useCenter = false, style = Stroke(strokeWidth))
        }
    }
}