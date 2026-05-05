package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter

import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.core.content.res.ResourcesCompat
import com.lebaillyapp.deeper.R
import kotlin.math.floor

@Composable
fun DualTapeAltimeter(
    altitude: Float,
    recordDepth: Float,
    modifier: Modifier = Modifier,
    minAltitude: Float = -30000f,
    maxAltitude: Float = 0f,
    majorTickEvery: Int = 10,
    minorTickEvery: Int = 2,
    deepCountSize: Float = 33f,
    customFont: Typeface? = null,
    titleText: String = "METERS",
    arrowsIndicatorSize: Float = 0.25f,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
) {
    val backgroundColor = Color(0xFF181818)
    val tapeColor = Color(0xFF1E1F22)
    val windowBorderColor = Color(0xFFE0E0E0)
    val displayBackgroundColor = Color(0xFF181818)
    val recordLineColor = Color.Red
    val recordTextColor = Color(0xFF6B6666)

    val animatedAltitude by animateFloatAsState(
        targetValue = altitude.coerceIn(minAltitude, maxAltitude),
        animationSpec = animationSpec,
        label = "altitude"
    )

    val isRecordBroken = animatedAltitude < recordDepth

    val infiniteTransition = rememberInfiniteTransition(label = "recordBlink")
    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                1f at 250
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "blinkAlpha"
    )

    val dynamicRecordColor = if (isRecordBroken) {
        Color(0xFF1DE9B6).copy(alpha = blinkAlpha)
    } else {
        Color.Yellow
    }

    val context = LocalContext.current
    val fontTypeface = customFont ?: remember {
        ResourcesCompat.getFont(context, R.font.micro_regular)
    }

    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val tapeWidth = width * 0.25f
            val windowHeight = height * 0.2f
            val centerAreaWidth = width - (tapeWidth * 2)

            // 1. DESSIN DES FONDS
            drawRect(color = tapeColor, size = Size(tapeWidth, height))
            drawRect(color = tapeColor, topLeft = Offset(width - tapeWidth, 0f), size = Size(tapeWidth, height))
            drawRect(
                color = displayBackgroundColor,
                topLeft = Offset(tapeWidth, height / 2 - height * 0.15f),
                size = Size(centerAreaWidth, height * 0.3f)
            )

            // 2. & 3. CONTENU DES RUBANS
            clipRect(left = 0f, top = 0f, right = tapeWidth, bottom = height) {
                drawAltitudeTape(animatedAltitude, tapeWidth, true, majorTickEvery, minorTickEvery)
            }
            clipRect(left = width - tapeWidth, top = 0f, right = width, bottom = height) {
                drawAltitudeTape(animatedAltitude, tapeWidth, false, majorTickEvery, minorTickEvery)
            }

            // 4. LIGNE DE RECORD TRAVERSANTE
            val pxPerMeter = height / 50f
            val recordY = height / 2 + (animatedAltitude - recordDepth) * pxPerMeter

            if (recordY in 0f..height) {
                val horizontalMargin = 48.dp.toPx()
                drawLine(
                    color = recordLineColor,
                    start = Offset(horizontalMargin, recordY),
                    end = Offset(width - horizontalMargin, recordY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // 5. FENÊTRE CENTRALE & POINTEURS
            val windowY = height / 2 - windowHeight / 2
            drawLine(color = windowBorderColor, start = Offset(0f, windowY), end = Offset(width, windowY), strokeWidth = 1f)
            drawLine(color = windowBorderColor, start = Offset(0f, windowY + windowHeight), end = Offset(width, windowY + windowHeight), strokeWidth = 1f)

            val pointerSize = windowHeight * arrowsIndicatorSize

            drawPath(
                path = Path().apply {
                    moveTo(tapeWidth, height / 2)
                    lineTo(tapeWidth + pointerSize / 2, height / 2 - pointerSize / 2)
                    lineTo(tapeWidth + pointerSize / 2, height / 2 + pointerSize / 2)
                    close()
                },
                color = dynamicRecordColor
            )
            drawPath(
                path = Path().apply {
                    moveTo(width - tapeWidth, height / 2)
                    lineTo(width - tapeWidth - pointerSize / 2, height / 2 - pointerSize / 2)
                    lineTo(width - tapeWidth - pointerSize / 2, height / 2 + pointerSize / 2)
                    close()
                },
                color = dynamicRecordColor
            )

            // 6. TEXTES
            val textPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                typeface = fontTypeface
                textAlign = android.graphics.Paint.Align.CENTER
            }

            textPaint.textSize = deepCountSize.sp.toPx()
            textPaint.color = android.graphics.Color.WHITE
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", animatedAltitude),
                width / 2,
                height / 2 + textPaint.textSize / 4,
                textPaint
            )

            textPaint.textSize = (deepCountSize - 14).sp.toPx()
            drawContext.canvas.nativeCanvas.drawText(
                titleText,
                width / 2,
                height / 2 + textPaint.textSize * 2.8f,
                textPaint
            )

            val recordToDisplay = if (isRecordBroken) animatedAltitude else recordDepth
            textPaint.textSize = (deepCountSize - 12).sp.toPx()
            textPaint.color = if (isRecordBroken) dynamicRecordColor.toArgb() else recordTextColor.toArgb()

            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.1f", recordToDisplay),
                width / 2,
                height * 0.85f,
                textPaint
            )

            // --- 7. VIGNETAGE GLOBAL (FILTRE FINAL) ---
            // On l'applique sur toute la largeur (0f à width)
            // L'opacité à 0.8f sur les bords donne un effet "écran cathodique" ou "boîtier profond"
            val globalVignette = Brush.verticalGradient(
                0.0f to Color.Black.copy(alpha = 0.8f),
                0.22f to Color.Transparent,
                0.78f to Color.Transparent,
                1.0f to Color.Black.copy(alpha = 0.8f)
            )

            drawRect(
                brush = globalVignette,
                size = Size(width, height)
            )
        }
    }
}

private fun DrawScope.drawAltitudeTape(
    altitude: Float,
    tapeWidth: Float,
    isLeftTape: Boolean,
    majorTickEvery: Int,
    minorTickEvery: Int
) {
    val pxPerMeter = size.height / 50f
    val majorTickColor = Color.White
    val minorTickColor = Color(0xFFCCCCCC)

    val visibleRange = (size.height / pxPerMeter).toInt()
    val lowestVisible = (altitude - visibleRange / 2).toInt()
    val highestVisible = (altitude + visibleRange / 2).toInt()
    val tickStart = (floor(lowestVisible.toFloat() / minorTickEvery) * minorTickEvery).toInt()

    for (tickAltitude in tickStart..highestVisible step minorTickEvery) {
        val isMajorTick = tickAltitude % majorTickEvery == 0
        val isSpecialTick = tickAltitude % 100 == 0
        val y = size.height / 2 + (altitude - tickAltitude) * pxPerMeter

        if (y !in 0f..size.height) continue

        val tickLength = when {
            isSpecialTick -> tapeWidth * 0.4f
            isMajorTick -> tapeWidth * 0.25f
            else -> tapeWidth * 0.15f
        }

        val tickColor = if (isSpecialTick) Color.Yellow else if (isMajorTick) majorTickColor else minorTickColor
        val startX = if (isLeftTape) tapeWidth - tickLength else size.width - tapeWidth
        val endX = if (isLeftTape) tapeWidth else (size.width - tapeWidth) + tickLength

        drawLine(
            color = tickColor,
            start = Offset(startX, y),
            end = Offset(endX, y),
            strokeWidth = if (isMajorTick) 1.5.dp.toPx() else 1.dp.toPx()
        )

        if (isMajorTick || isSpecialTick) {
            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = if (isSpecialTick) 10.sp.toPx() else 9.sp.toPx()
                color = if (isSpecialTick) android.graphics.Color.YELLOW else android.graphics.Color.WHITE
                textAlign = if (isLeftTape) android.graphics.Paint.Align.RIGHT else android.graphics.Paint.Align.LEFT
            }
            val textX = if (isLeftTape) startX - 4.dp.toPx() else endX + 4.dp.toPx()
            drawContext.canvas.nativeCanvas.drawText(tickAltitude.toString(), textX, y + paint.textSize / 3, paint)
        }
    }
}