package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.RealisticLED
import kotlin.math.*



/**
 * A generic, themeable Composable for a horizontal 180-degree analog gauge.
 * (Version 4 - Value behind needle, Orange needle, Bigger pivot, Custom title font)
 *
 * @param modifier Modifier for size and layout of the Card container.
 * @param currentValue The value to display.
 * @param minValue Minimum scale value.
 * @param maxValue Maximum scale value.
 * @param title Optional title text (displayed below the gauge).
 * @param unit Optional unit text displayed next to the value (on Canvas).
 * @param titleFontFamily Optional custom font family for the title Text.
 * @param majorTickInterval Interval for major ticks. Auto-calculated if null.
 * @param minorTicksPerMajor Number of minor ticks between major ones.
 * @param cardBackgroundColor Background color of the Card.
 * @param scaleColor Color of the scale arc, ticks, and labels.
 * @param needleColor Color of the indicator needle (Defaults to Orange).
 * @param valueTextColor Color of the main value text (on Canvas).
 * @param unitTextColor Color of the unit text (on Canvas).
 * @param titleColor Color of the title text (for the Text Composable).
 * @param valueTextSize Font size for the main value (on Canvas).
 * @param unitTextSize Font size for the unit (on Canvas).
 * @param titleTextSize Font size for the title (for the Text Composable).
 * @param labelTextSize Font size for scale labels (on Canvas).
 * @param scaleArcStrokeWidth Stroke width (thickness) of the background scale arc.
 * @param majorTickStrokeWidth Stroke width for major ticks.
 * @param minorTickStrokeWidth Stroke width for minor ticks.
 * @param needleStrokeWidth Stroke width for the needle.
 * @param needleCap Cap style for the needle (Round, Square, Butt).
 * @param cardElevation Elevation of the Card.
 * @param cardCornerRadius Corner radius for the Card.
 * @param internalPadding Padding inside the card, around the Canvas and Text title.
 * @param animationDurationMillis Duration for the needle animation.
 */
@Composable
fun HorizontalGauge(
    modifier: Modifier,
    // --- Core Data ---
    currentValue: Float,
    minValue: Float = 0f,
    maxValue: Float = 100f,
    title: String? = null,
    // --- Font ---
    titleFontFamily: FontFamily? = null, // Font for the title Text
    // --- Scale Configuration ---
    majorTickInterval: Float? = null,
    minorTicksPerMajor: Int = 4,
    // --- Colors (Dark Theme Defaults, Orange Needle) ---
    cardBackgroundColor: Color = Color(0xFF181818),
    pinColor: Color = Color(0xFF181818),
    scaleColor: Color = Color(0xFFBBBBBB), // Couleur par défaut pour les graduations intermédiaires
    firstTickColor: Color = Color(0xFFA40525),
    lastTickColor: Color = Color(0xFFBBBBBB),// Nouvelle couleur pour la première et dernière graduation
    needleColor: Color = Color(0xFFFFA500), // Default Orange needle
    valueTextColor: Color = Color.White,
    unitTextColor: Color = Color(0xFFBBBBBB),
    titleColor: Color = Color(0xFFAAAAAA), // For the Text Composable
    // --- Text Sizes ---
    valueTextSize: TextUnit = 18.sp, // Slightly larger to be visible behind needle
    unitTextSize: TextUnit = 6.sp,  // Unit smaller
    titleTextSize: TextUnit = 12.sp, // For the Text Composable
    labelTextSize: TextUnit = 6.sp,
    // --- Stroke Widths & Style ---
    scaleArcStrokeWidth: Dp = 2.dp,
    majorTickStrokeWidth: Dp = 1.dp,
    minorTickStrokeWidth: Dp = 0.5.dp,
    needleStrokeWidth: Dp = 1.dp,
    needleCap: StrokeCap = StrokeCap.Round,
    // --- Card Style ---
    cardElevation: Dp = 2.dp,
    cardCornerRadius: Dp = 5.dp,
    internalPadding: Dp = 1.dp, // Overall padding
    // --- Animation ---
    animationDurationMillis: Int = 500,
    ledColor: Color = Color(0xFFFF9100),
    ledIsOn: Boolean = true,
    ledBlinkInterval: Int = 0
) {
    // --- Input Validation ---
    if (maxValue <= minValue) {
        Text(
            "Error: maxValue must be > minValue",
            color = Color.Red,
            modifier = modifier.padding(internalPadding) // Apply padding for error msg too
        )
        return
    }

    // --- Tick Interval Calculation ---
    val effectiveMajorTickInterval = majorTickInterval ?: run {
        val range = maxValue - minValue
        if (range <= 0) return@run 10f // Should not happen due to check above, but safe
        val roughInterval = range / 8.0 // Aim for about 8 intervals
        // Snap to nearest "nice" number (1, 2, 5, 10, 20, 50, ...)
        val magnitude = 10f.pow(floor(log10(roughInterval.toFloat())))
        when ((roughInterval / magnitude).roundToInt()) {
            in 0..1 -> magnitude
            in 2..3 -> 2 * magnitude
            in 4..7 -> 5 * magnitude
            else -> 10 * magnitude
        }
    }
    val range = maxValue - minValue
    // Ensure tickIntervalValue is positive and sensible
    val tickIntervalValue = if (minorTicksPerMajor >= 0 && effectiveMajorTickInterval > 0) {
        effectiveMajorTickInterval / (minorTicksPerMajor + 1)
    } else if (effectiveMajorTickInterval > 0) {
        effectiveMajorTickInterval // Only major ticks if minorTicksPerMajor is invalid
    } else {
        max(Float.MIN_VALUE, range / 10f) // Fallback, avoid zero
    }

    // --- Density & Pixel Conversions ---
    val density = LocalDensity.current
    val valueTextSizePx = with(density) { valueTextSize.toPx() }
    val unitTextSizePx = with(density) { unitTextSize.toPx() }
    val labelTextSizePx = with(density) { labelTextSize.toPx() }
    val scaleArcStrokeWidthPx = with(density) { scaleArcStrokeWidth.toPx() }
    val majorTickStrokeWidthPx = with(density) { majorTickStrokeWidth.toPx() }
    val minorTickStrokeWidthPx = with(density) { minorTickStrokeWidth.toPx() }
    val needleStrokeWidthPx = with(density) { needleStrokeWidth.toPx() }


    val labelTextPaint = android.graphics.Paint().apply {
        isAntiAlias = true; color = scaleColor.hashCode(); textSize = labelTextSizePx; textAlign = android.graphics.Paint.Align.CENTER
    }

    // --- Angle Animation ---
    val clampedValue = currentValue.coerceIn(minValue, maxValue)
    val targetAngle = mapValueToAngle(clampedValue, minValue, maxValue)
    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(animationDurationMillis),
        label = "GaugeAngleAnim"
    )

    // --- Drawing Structure: Card -> Column -> (Canvas + Text) ---
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Box(
            modifier = Modifier.padding(internalPadding), // Apply overall padding here
        ) {
            // --- Canvas for the gauge graphics ---
            Canvas(modifier = Modifier.fillMaxSize()) { // Takes remaining vertical space
                val canvasWidth = size.width
                val canvasHeight = size.height

                // If canvas has no size yet, don't draw
                if (canvasWidth <= 0f || canvasHeight <= 0f) return@Canvas

                // --- Adjusted Geometry ---
                val pivot = Offset(canvasWidth / 2f, canvasHeight * 0.80f)
                val availableRadiusY = pivot.y
                val availableRadiusX = canvasWidth / 2f
                // Ensure radius is positive
                val gaugeRadius = max(0f, min(availableRadiusX, availableRadiusY) * 0.90f)

                // Define radii based on the main gaugeRadius
                val scaleArcOuterRadius = gaugeRadius
                // Ensure inner radius isn't larger than outer due to large stroke width
                val scaleArcInnerRadius = max(0f, gaugeRadius - scaleArcStrokeWidthPx)
                val majorTickOuterRadius = scaleArcOuterRadius
                val majorTickInnerRadius = gaugeRadius * 0.85f
                val minorTickOuterRadius = scaleArcOuterRadius
                val minorTickInnerRadius = gaugeRadius * 0.90f
                val labelRadius = majorTickInnerRadius * 0.90f
                val needleLength = gaugeRadius * 0.98f

                // --- 1. Draw Scale Arc ---
                if (scaleArcStrokeWidthPx > 0 && scaleArcInnerRadius < scaleArcOuterRadius) {
                    drawArc(
                        color = Color(0x3B4D4D4D),
                        startAngle = 180f, sweepAngle = 180f, useCenter = false,
                        topLeft = Offset(pivot.x - scaleArcOuterRadius, pivot.y - scaleArcOuterRadius),
                        size = Size(scaleArcOuterRadius * 2, scaleArcOuterRadius * 2),
                        style = Stroke(width = scaleArcStrokeWidthPx)
                    )
                }

                // --- 2. Draw Ticks and Labels ---
                if (range > 0 && tickIntervalValue > Float.MIN_VALUE) {
                    // Calculate total ticks carefully to avoid excessive loops
                    val totalTicks = (range / tickIntervalValue).roundToInt()
                    // Limit loop iterations for safety
                    for (i in 0..min(totalTicks, 1000)) {
                        val tickValue = minValue + i * tickIntervalValue
                        val tolerance = tickIntervalValue * 0.01f + Float.MIN_VALUE
                        if (tickValue > maxValue + tolerance) continue

                        val angleDeg = mapValueToAngle(tickValue, minValue, maxValue)
                        val angleRad = Math.toRadians(angleDeg.toDouble())
                        // Check for major tick (handle potential floating point inaccuracies)
                        val remainder = abs((tickValue - minValue) % effectiveMajorTickInterval)
                        val isMajorTick = remainder < tolerance || abs(remainder - effectiveMajorTickInterval) < tolerance

                        val tickInnerRadius = if (isMajorTick) majorTickInnerRadius else minorTickInnerRadius
                        // Ensure radii are valid before calculating coordinates
                        if (tickInnerRadius >= 0f && majorTickOuterRadius >= 0f) {
                            val tickStartX = pivot.x + cos(angleRad).toFloat() * tickInnerRadius
                            val tickStartY = pivot.y + sin(angleRad).toFloat() * tickInnerRadius
                            val tickEndX = pivot.x + cos(angleRad).toFloat() * majorTickOuterRadius
                            val tickEndY = pivot.y + sin(angleRad).toFloat() * majorTickOuterRadius

                            // Déterminer la couleur de la graduation actuelle
                            val currentTickColor = when{
                                i == 0 -> firstTickColor // Utiliser la couleur rouge pour la première (i=0)
                                i == totalTicks -> lastTickColor // Utiliser la couleur verte pour la dernière (i=totalTicks)
                                else -> scaleColor
                            }

                            drawLine(
                                color = currentTickColor, // Utiliser la couleur déterminée
                                start = Offset(tickStartX, tickStartY), end = Offset(tickEndX, tickEndY),
                                strokeWidth = if (isMajorTick) majorTickStrokeWidthPx else minorTickStrokeWidthPx
                            )

                            // Labels for Major Ticks
                            if (isMajorTick && labelRadius > 0f) {
                                val labelText = tickValue.roundToInt().toString()
                                val labelX = pivot.x + cos(angleRad).toFloat() * labelRadius
                                val labelY = pivot.y + sin(angleRad).toFloat() * labelRadius + (labelTextSizePx / 3f)
                                // --- MODIFICATION ICI (Optionnel mais cohérent) ---
                                // Changer aussi la couleur du label pour la 1ère/dernière graduation si désiré

                                val currentLabelPaint = when{
                                    i == 0 -> android.graphics.Paint(labelTextPaint).apply { color = firstTickColor.toArgb() }
                                    i == totalTicks -> android.graphics.Paint(labelTextPaint).apply { color = lastTickColor.toArgb() }
                                    else -> labelTextPaint
                                }
                                drawIntoCanvas { c -> c.nativeCanvas.drawText(labelText, labelX, labelY, currentLabelPaint) }
                            }
                        }
                    }
                }

                // --- 4. Draw Needle ---
                // Ensure needleLength is positive before calculating tip
                if (needleLength > 0f) {
                    val needleAngleRad = Math.toRadians(animatedAngle.toDouble())
                    val needleTipX = pivot.x + cos(needleAngleRad).toFloat() * needleLength
                    val needleTipY = pivot.y + sin(needleAngleRad).toFloat() * needleLength

                    drawLine(
                        color = needleColor, start = pivot, end = Offset(needleTipX, needleTipY),
                        strokeWidth = needleStrokeWidthPx, cap = needleCap
                    )
                }
                // Pivot decoration (larger)
                if(needleStrokeWidthPx > 0) {
                    val outerPivotRadius = needleStrokeWidthPx * 22.5f
                    val innerPivotRadius = needleStrokeWidthPx * 23.5f
                    drawCircle(color = needleColor, radius = outerPivotRadius, center = pivot)
                    drawCircle(color = pinColor, radius = innerPivotRadius, center = pivot)
                }
            } // End of Canvas

            // Regrouper les styles dans un TextStyle
            val titleStyle = LocalTextStyle.current.merge( // Fusionner avec le style local actuel si nécessaire
                TextStyle(
                    color = titleColor,
                    fontSize = titleTextSize,
                    fontFamily = titleFontFamily,
                    textAlign = TextAlign.Center // Mettre textAlign dans le TextStyle
                )
            )
            val NumericValueStyle = LocalTextStyle.current.merge( // Fusionner avec le style local actuel si nécessaire
                TextStyle(
                    color = titleColor,
                    fontSize = valueTextSize,
                    fontFamily = titleFontFamily,
                    textAlign = TextAlign.Center // Mettre textAlign dans le TextStyle
                )
            )


            Text(
                text = title?:"",
                modifier = Modifier.padding(bottom = 2.dp).align(Alignment.BottomCenter),
                style = titleStyle
            )
            Text(
                text = "${String.format("%.1f", clampedValue)}",
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 15.dp),
                style = NumericValueStyle
            )


            //led
            RealisticLED(
                modifier = Modifier.align(Alignment.TopStart).padding(start = 3.dp, top = 3.dp),
                isOn = ledIsOn,
                color = ledColor,
                size = 8.0f,
                haloSpacer = 1,
                blinkInterval = ledBlinkInterval
            )



        } // End of Column
    } // End of Card
}


// Helper function to map value to angle (assuming standard 0-180 degree gauge)
private fun mapValueToAngle(value: Float, minValue: Float, maxValue: Float): Float {
    // Ensure value is within bounds for angle calculation
    val clamped = value.coerceIn(minValue, maxValue)
    // Map value range [minValue, maxValue] to angle range [180, 360] (or [-180, 0] visually)
    // However, Compose drawArc uses 0 degrees at 3 o'clock, positive angles clockwise.
    // So we want angles from 180 (left) to 360 (right, same as 0).
    val range = maxValue - minValue
    // Avoid division by zero if range is zero
    return if (range == 0f) 180f else 180f + ((clamped - minValue) / range) * 180f
}


