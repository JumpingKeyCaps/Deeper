package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toArgb
import kotlin.math.*

/**
 * ## CircularAltimeterGauge
 *
 * Instrument de bord pour la mesure de profondeur (altimètre inversé).
 *
 * - **Grande Aiguille** : Indique les hectomètres (1 tour complet = 1000m).
 * - **Petite Aiguille** : Indique les kilomètres (1 tour complet = 10 000m).
 *
 * @param modifier Le modifier pour le positionnement.
 * @param config La configuration statique (ratios, couleurs de base, stroke).
 * @param state L'état dynamique du composant (profondeur, état machine).
 */
@Composable
fun CircularAltimeterGauge(
    modifier: Modifier = Modifier,
    config: CircularAltimeterGaugeConfig,
    state: CircularAltimeterGaugeState
) {

    // Détermination de l'état d'activité
    val isOn = state.value.machineState != AltimeterMachineState.OFF
    val density = LocalDensity.current
    val offLightAlpha = 0.3f

    // Animation fluide de l'aiguille basée sur la profondeur
    val animatedDepth by animateFloatAsState(
        targetValue = state.value.depth,
        animationSpec = tween(if(state.value.depth < 3000) config.animationDurationMillis else config.animationDurationMillis * 4  , easing = LinearOutSlowInEasing),
        label = "DepthAnimation"
    )

    // Logique de démultiplication des aiguilles
    val bigNeedleAngle = (animatedDepth % 1000f) * 0.36f - 90f
    val smallNeedleAngle = (animatedDepth % 10000f) * 0.036f - 90f

    // Configuration du Paint pour les graduations (Native Canvas pour les perfs)
    val labelTextSizePx = with(density) { (config.gaugeSize.value * 0.06f).sp.toPx() }
    val labelTextPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        color = (if (isOn) config.scaleColor else config.scaleColor.copy(alpha = offLightAlpha)).toArgb()
        textSize = labelTextSizePx
        textAlign = android.graphics.Paint.Align.CENTER
        typeface = android.graphics.Typeface.MONOSPACE
    }

    Surface(
        modifier = modifier
            .size(config.gaugeSize)
            .aspectRatio(1f),
        shape = CircleShape,
        color = config.cardBackgroundColor,
        tonalElevation = 6.dp
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val pivot = Offset(canvasWidth / 2f, canvasHeight / 2f)
                val radius = min(canvasWidth, canvasHeight) / 2f * 0.9f

                val floatingHoleRadius = radius * config.pivotHoleRatio
                val majorTickInnerRadius = radius * 0.88f
                val minorTickInnerRadius = radius * 0.94f
                val labelRadius = radius * 0.70f

                // 1. EFFET DE PROFONDEUR RADIAL (FOND)
                drawCircle(
                    brush = Brush.radialGradient(
                        0.8f to Color.Transparent,
                        1.0f to Color.Black.copy(alpha = 0.6f),
                        center = pivot,
                        radius = radius * 1.2f
                    ),
                    radius = radius * 1.5f,
                    center = pivot
                )

                // 2. GRADUATIONS (0 à 9)
                for (i in 0 until 50) {
                    val isMajor = i % 5 == 0
                    val angleDeg = (i * (360f / 50f)) - 90f
                    val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()
                    val innerR = if (isMajor) majorTickInnerRadius else minorTickInnerRadius

                    val tickColor = if (isMajor) {
                        if (isOn) config.graduationBigColor else config.scaleColor.copy(alpha = offLightAlpha)
                    } else {
                        if (isOn) config.scaleColor else config.scaleColor.copy(alpha = offLightAlpha)
                    }

                    drawLine(
                        color = tickColor,
                        start = Offset(pivot.x + cos(angleRad) * innerR, pivot.y + sin(angleRad) * innerR),
                        end = Offset(pivot.x + cos(angleRad) * radius, pivot.y + sin(angleRad) * radius),
                        strokeWidth = (if (isMajor) config.majorTickStrokeWidth else config.minorTickStrokeWidth).toPx(),
                        cap = StrokeCap.Butt
                    )

                    if (isMajor) {
                        val digit = (i / 5).toString()
                        val labelX = pivot.x + cos(angleRad) * labelRadius
                        val labelY = pivot.y + sin(angleRad) * labelRadius + (labelTextSizePx / 3f)
                        drawIntoCanvas { it.nativeCanvas.drawText(digit, labelX, labelY, labelTextPaint) }
                    }
                }

                // 3. INNER SHADOW (RELIEF DU CADRAN)
                drawCircle(
                    brush = Brush.radialGradient(
                        0.90f to Color.Transparent,
                        1.0f to Color.Black.copy(alpha = 0.5f),
                        center = pivot,
                        radius = radius
                    ),
                    radius = radius,
                    center = pivot
                )

                // 4. PETITE AIGUILLE (KILOMÈTRES)
                val sAngleRad = Math.toRadians(smallNeedleAngle.toDouble()).toFloat()
                drawLine(
                    color = if (isOn) config.smallNeedleColor else config.smallNeedleColor.copy(alpha = offLightAlpha),
                    start = pivot,
                    end = Offset(
                        pivot.x + cos(sAngleRad) * (radius * 0.68f),
                        pivot.y + sin(sAngleRad) * (radius * 0.68f)
                    ),
                    strokeWidth = config.smallNeedleStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )

                // 5. GRANDE AIGUILLE (HECTOMÈTRES)
                val bAngleRad = Math.toRadians(bigNeedleAngle.toDouble()).toFloat()
                drawLine(
                    color = if (isOn) config.needleColor else config.needleColor.copy(alpha = offLightAlpha),
                    start = Offset(
                        pivot.x + cos(bAngleRad) * (floatingHoleRadius * 0.8f),
                        pivot.y + sin(bAngleRad) * (floatingHoleRadius * 0.8f)
                    ),
                    end = Offset(
                        pivot.x + cos(bAngleRad) * (radius * 0.95f),
                        pivot.y + sin(bAngleRad) * (radius * 0.95f)
                    ),
                    strokeWidth = config.bigNeedleStrokeWidth.toPx(),
                    cap = StrokeCap.Round
                )

                // 6. CACHE PIVOT CENTRAL
                drawCircle(
                    color = config.cardBackgroundColor,
                    radius = floatingHoleRadius,
                    center = pivot
                )
            }

            // 7. TEXTES CENTRAUX (VALEUR NUMÉRIQUE)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${animatedDepth.toInt()}",
                    style = TextStyle(
                        color = if (isOn) config.valueTextColor else config.valueTextColor.copy(alpha = offLightAlpha),
                        fontSize = (config.gaugeSize.value * 0.20f).sp,
                        fontFamily = config.gaugeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // 8. TITRE (BAS DU COMPOSANT)
            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = (config.gaugeSize.value * config.titleBottomMarginRatio).dp),
                text = config.titleParam,
                style = TextStyle(
                    color = if (isOn) config.scaleColor.copy(alpha = 0.5f) else config.scaleColor.copy(alpha = 0.1f),
                    fontSize = (config.gaugeSize.value * 0.10f).sp,
                    fontFamily = config.gaugeFontFamily,
                )
            )
        }
    }
}