package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.pulsingPointEkg


import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.math.sin

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PulsingPointEkg(
    modifier: Modifier = Modifier,
    oxygenLevel: Int,
    pointRadius: Dp = 2.dp,
    trailThickness: Dp = 0.5.dp,
    glowRadius: Dp = 4.dp,
    normalColor: Color = Color(0xFF1DE9B6),
    warningColor: Color = Color(0xFFF1AB2B),
    dangerColor: Color = Color(0xFFFC4242),
    bpmState: MutableState<Int>
) {
    val trailPointsY = remember { mutableStateListOf<Float>() }
    val density = LocalDensity.current

    // Calcul dynamique du BPM (bpm = 0 quand oxygenLevel == 0)
    val bpm = remember(oxygenLevel) {
        if (oxygenLevel <= 0) 0
        else (10+(oxygenLevel / 100f) * 50).toInt()
    }.also { bpmState.value = it }


    // Détermination de la couleur
    val lineColor = when {
        oxygenLevel >= 60 -> normalColor
        oxygenLevel >= 40 -> warningColor
        else -> dangerColor
    }

    val pointSpacing: Dp = 2.dp
    val pointCycleDuration = if (bpm > 0) (60_000 / bpm) else Int.MAX_VALUE

    val infiniteTransition = rememberInfiniteTransition(label = "HeartCycle")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = pointCycleDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Phase"
    )

    BoxWithConstraints(modifier = modifier) {
        val canvasWidth = constraints.maxWidth.toFloat()
        val canvasHeight = constraints.maxHeight.toFloat()
        val pointSpacingPx = with(density) { pointSpacing.toPx() }

        val maxTrailPoints = remember(canvasWidth, pointSpacingPx) {
            if (pointSpacingPx > 0) (canvasWidth / 2f / pointSpacingPx).toInt() + 1 else 100
        }

        LaunchedEffect(phase, bpm, canvasHeight) {
            if (!isActive) return@LaunchedEffect

            val currentY = if (bpm == 0)
                canvasHeight / 2f
            else
                calculateYForHeartCyclePhase(phase, canvasHeight, oxygenLevel)

            trailPointsY.add(currentY)
            while (trailPointsY.size > maxTrailPoints && trailPointsY.isNotEmpty()) {
                trailPointsY.removeAt(0)
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            if (width <= 0f || height <= 0f || trailPointsY.isEmpty()) return@Canvas

            val endMarginPx = with(density) { 10.dp.toPx() }
            val pointX = width - endMarginPx
            val currentY = trailPointsY.last()
            val numPoints = trailPointsY.size
            val trailPath = Path()

            if (numPoints > 1) {
                val startX = pointX - (numPoints - 1) * pointSpacingPx
                trailPath.moveTo(startX, trailPointsY[0])
                for (i in 1 until numPoints) {
                    val xPos = startX + i * pointSpacingPx
                    if (xPos <= pointX) {
                        trailPath.lineTo(xPos, trailPointsY[i])
                    }
                }
            }

            drawPath(
                path = trailPath,
                color = lineColor.copy(alpha = 0.7f),
                style = Stroke(width = trailThickness.toPx(), cap = StrokeCap.Round)
            )

            val currentPointOffset = Offset(pointX, currentY)
            drawCircle(
                color = lineColor.copy(alpha = 0.3f),
                radius = glowRadius.toPx(),
                center = currentPointOffset
            )
            drawCircle(
                color = lineColor,
                radius = pointRadius.toPx(),
                center = currentPointOffset
            )
        }
    }
}

fun calculateYForHeartCyclePhase(phase: Float, height: Float, oxygenLevel: Int): Float {
    val midY = height / 2f
    val amplitude = getAmplitudeFromOxygen(oxygenLevel, height)
    val p = phase.coerceIn(0f, 1f)

    val yOffset = when {
        p < 0.1f -> 0f
        p < 0.2f -> -amplitude * 0.1f * sin(((p - 0.1f) / 0.1f) * Math.PI.toFloat())
        p < 0.25f -> 0f
        p < 0.30f -> amplitude * 0.2f * ((p - 0.25f) / 0.05f)
        p < 0.38f -> -amplitude * 1.0f * (1f - ((p - 0.30f) / 0.08f).coerceAtMost(1f) * 1.5f)
        p < 0.45f -> -amplitude * 0.2f + amplitude * 0.7f * ((p - 0.38f) / 0.07f)
        p < 0.55f -> 0f
        p < 0.80f -> -amplitude * 0.25f * sin(((p - 0.55f) / 0.25f) * Math.PI.toFloat())
        else -> 0f
    }

    return (midY + yOffset).coerceIn(0f, height)
}

fun getAmplitudeFromOxygen(oxygenLevel: Int, height: Float): Float {
    val minAmplitudeRatio = 0.2f // même à bas taux, garder un peu d'oscillation
    val ratio = (oxygenLevel.coerceIn(0, 100) / 100f).coerceIn(minAmplitudeRatio, 1f)
    return height * 0.4f * ratio
}