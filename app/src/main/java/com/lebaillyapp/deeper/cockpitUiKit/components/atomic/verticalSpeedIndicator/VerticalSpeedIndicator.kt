package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults


/**
 * A Composable function that displays a vertical analog speed indicator (variometer)
 * within a Card with rounded corners. (Version 4 - Needle overlaying scale on left)
 *
 * @param modifier The modifier to be applied to the Card container.
 * @param verticalSpeed The current vertical speed to display. Units depend on context.
 * @param maxSpeed The maximum absolute speed value for the scale.
 * @param majorTickInterval The interval between major ticks with labels. Defaults to 2.
 * @param minorTicksPerMajor The number of minor ticks between each major tick.
 * @param cardBackgroundColor The background color of the Card.
 * @param indicatorColor The color of the moving indicator needle.
 * @param scaleColor The color of the scale lines and labels. Defaults to White.
 * @param zeroLineColor The color of the line indicating zero speed.
 * @param tickWidthMajor The stroke width for major ticks.
 * @param tickWidthMinor The stroke width for minor ticks.
 * @param labelTextSize The text size for the labels.
 * @param cardElevation Elevation of the Card.
 * @param cardCornerRadius Corner radius for the Card.
 * @param internalPadding Padding inside the card before the canvas drawing starts.
 * @param animationDurationMillis Duration for the indicator animation in milliseconds.
 */
@Composable
fun VerticalSpeedIndicator(
    modifier: Modifier = Modifier,
    verticalSpeed: Float,
    maxSpeed: Float = 10f, // Example: +/- 10 units
    majorTickInterval: Float = 2f, // Default interval is 2 units
    minorTicksPerMajor: Int = 1, // -> one minor tick every 1 unit
    cardBackgroundColor: Color = Color(0xFF1A1A1A), // Dark background
    indicatorColor: Color = Color(0xFFFFA500), // Orange
    scaleColor: Color = Color.White, // Scale/Labels are White
    zeroLineColor: Color = Color.LightGray, // Dimmer zero line
    tickWidthMajor: Dp = 1.5.dp, // Thinner major ticks
    tickWidthMinor: Dp = 0.75.dp, // Thinner minor ticks
    labelTextSize: Dp = 11.dp,
    cardElevation: Dp = 4.dp,
    cardCornerRadius: Dp = 8.dp,
    internalPadding: Dp = 10.dp,
    animationDurationMillis: Int = 300
) {
    val absMaxSpeed = abs(maxSpeed)
    if (absMaxSpeed <= 0f || majorTickInterval <= 0f) {
        return
    }

    val animatedSpeed by animateFloatAsState(
        targetValue = verticalSpeed.coerceIn(-absMaxSpeed, absMaxSpeed),
        animationSpec = tween(durationMillis = animationDurationMillis),
        label = "VerticalSpeedAnimation"
    )

    val density = LocalDensity.current
    val tickWidthMajorPx = with(density) { tickWidthMajor.toPx() }
    val tickWidthMinorPx = with(density) { tickWidthMinor.toPx() }
    val labelTextSizePx = with(density) { labelTextSize.toPx() }
    val horizontalTickPaddingPx = with(density) { 4.dp.toPx() } // Small padding from left edge

    val textPaint = android.graphics.Paint().apply {
        color = scaleColor.hashCode()
        textSize = labelTextSizePx
        textAlign = android.graphics.Paint.Align.LEFT
        isAntiAlias = true
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(internalPadding)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerY = canvasHeight / 2f
            val scaleHeight = canvasHeight
            val halfScaleHeight = scaleHeight / 2f

            // --- Horizontal Layout Definitions ---
            val tickStartX = horizontalTickPaddingPx
            val majorTickEndX = canvasWidth * 0.18f
            val minorTickEndX = canvasWidth * 0.12f
            val labelStartX = majorTickEndX + with(density) { 4.dp.toPx() }
            // Needle overlays the scale on the left
            val needleEndX = majorTickEndX + with(density) { 6.dp.toPx() } // Needle extends slightly past major ticks

            // --- Function to map speed value to Y coordinate ---
            fun mapSpeedToY(speed: Float): Float {
                val clampedSpeed = speed.coerceIn(-absMaxSpeed, absMaxSpeed)
                return centerY - (clampedSpeed / absMaxSpeed) * halfScaleHeight
            }

            // --- Draw Scale Ticks (Major and Minor) ---
            val numSteps = (absMaxSpeed / (majorTickInterval / (minorTicksPerMajor + 1))).toInt() * 2
            for (step in -numSteps..numSteps) {
                val speedValue = step * (majorTickInterval / (minorTicksPerMajor + 1))
                if (abs(speedValue) > absMaxSpeed + 0.001f) continue

                val yPos = mapSpeedToY(speedValue)
                val isMajorTick = abs(speedValue / majorTickInterval - (speedValue / majorTickInterval).roundToInt()) < 0.01f || abs(speedValue)<0.01f

                if (isMajorTick) {
                    drawLine(
                        color = scaleColor,
                        start = Offset(tickStartX, yPos),
                        end = Offset(majorTickEndX, yPos),
                        strokeWidth = tickWidthMajorPx
                    )
                } else {
                    drawLine(
                        color = scaleColor,
                        start = Offset(tickStartX, yPos),
                        end = Offset(minorTickEndX, yPos),
                        strokeWidth = tickWidthMinorPx
                    )
                }
            }

            // --- Draw Labels for Major Ticks ---
            val numMajorTicksTotal = (absMaxSpeed / majorTickInterval).toInt() * 2
            for (i in -numMajorTicksTotal..numMajorTicksTotal) {
                val speedValue = i * majorTickInterval
                if (abs(speedValue) > absMaxSpeed + 0.001f) continue

                val yPos = mapSpeedToY(speedValue)
                val labelText = abs(speedValue).roundToInt().toString() // Use abs()
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        labelText,
                        labelStartX,
                        yPos + labelTextSizePx * 0.35f,
                        textPaint
                    )
                }
            }

            // --- Draw Zero Line ---
            val zeroY = mapSpeedToY(0f)
            drawLine(
                color = zeroLineColor,
                start = Offset(tickStartX, zeroY),
                end = Offset(majorTickEndX, zeroY),
                strokeWidth = tickWidthMajorPx,
            )

            // --- Draw Indicator Needle (Overlaying Left Scale) ---
            // **** This is drawn AFTER ticks/labels to appear on top ****
            val indicatorY = mapSpeedToY(animatedSpeed)
            drawLine(
                color = indicatorColor, // Orange color stands out on white/dark
                start = Offset(0f, indicatorY), // Start from the very left edge (inside padding)
                end = Offset(needleEndX, indicatorY),   // Extend slightly past major ticks
                strokeWidth = tickWidthMajorPx * 2.0f // Make indicator thicker to be more visible
            )
            // Optional: small circle at the tip for focus
            drawCircle(
                color = indicatorColor,
                radius = tickWidthMajorPx, // Adjust size as needed
                center = Offset(needleEndX, indicatorY)
            )
        }
    }
}