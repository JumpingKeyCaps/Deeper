package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

/**
 * CircularGauge - Indicateur analogique circulaire de type "Médaillon Cockpit".
 *
 * Ce composant affiche une valeur sur un arc de 270 degrés, allant de -135° (Sud-Ouest)
 * à +135° (Sud-Est), plaçant le zéro ou le point central exactement au Nord.
 *
 * ### Fonctionnalités clés :
 * - **Aiguille Flottante :** L'aiguille est dessinée sous un cache-pivot central (`floatingHoleRadius`),
 *   créant un effet visuel de suspension mécanique propre aux instruments aéro.
 * - **Animations Synchronisées :** La valeur numérique centrale et la position de l'aiguille
 *   sont animées de concert via un [animateFloatAsState] pour garantir une fluidité parfaite
 *   lors des changements de données ou de la mise sous tension.
 * - **Rendu Adaptatif (Mode OFF) :** Le paramètre [isOn] réduit l'opacité des éléments
 *   de l'échelle et de l'aiguille pour simuler une coupure du rétro-éclairage.
 * - **Échelle Intelligente :** Si [majorTickInterval] n'est pas fourni, le composant calcule
 *   automatiquement un intervalle lisible basé sur les puissances de 10.
 *
 * @param modifier Modifier appliqué au conteneur Surface.
 * @param gaugeSize Diamètre total du médaillon (le composant force un aspect ratio 1:1).
 * @param currentValue La valeur actuelle à afficher (sera clampée entre [minValue] et [maxValue]).
 * @param minValue Valeur de début d'échelle (positionnée à -135°).
 * @param maxValue Valeur de fin d'échelle (positionnée à +135°).
 * @param title Texte du label affiché en bas du cadran (ex: "OIL PRESS").
 * @param titleFontFamily Police typographique pour les labels, chiffres et la valeur centrale.
 * @param majorTickInterval Écart entre deux grandes graduations (ex: 10f pour une échelle 0-100).
 * @param minorTicksPerMajor Nombre de petits traits entre deux graduations majeures.
 * @param cardBackgroundColor Couleur du fond du cadran et du cache-pivot central.
 * @param scaleColor Couleur par défaut des graduations et des chiffres de l'échelle.
 * @param firstTickColor Couleur accentuée pour la première graduation (souvent rouge).
 * @param lastTickColor Couleur accentuée pour la dernière graduation.
 * @param needleColor Couleur de l'aiguille.
 * @param valueTextColor Couleur de la valeur numérique animée au centre.
 * @param titleColor Couleur du titre en bas du cadran.
 * @param valueTextSize Taille de la police pour la valeur centrale.
 * @param titleTextSize Taille de la police pour le titre.
 * @param labelTextSize Taille de la police pour les chiffres entourant l'échelle.
 * @param majorTickStrokeWidth Épaisseur du trait des graduations majeures.
 * @param minorTickStrokeWidth Épaisseur du trait des graduations mineures.
 * @param needleStrokeWidth Épaisseur de l'aiguille.
 * @param animationDurationMillis Durée (en ms) de la transition pour l'aiguille et le texte.
 * @param isOn État d'activation. Si false, applique un facteur d'atténuation sur les couleurs.
 * @param titleBottomMarging Décalage vertical du titre par rapport au bord inférieur.
 */
@Composable
fun CircularGauge(
    modifier: Modifier = Modifier,
    gaugeSize: Dp = 100.dp,
    currentValue: Float,
    minValue: Float = 0f,
    maxValue: Float = 100f,
    title: String? = null,
    titleFontFamily: FontFamily? = null,
    majorTickInterval: Float? = null,
    minorTicksPerMajor: Int = 4,
    cardBackgroundColor: Color = Color(0xFF181818),
    scaleColor: Color = Color(0xFFBBBBBB),
    firstTickColor: Color = Color(0xFFA40525),
    lastTickColor: Color = Color(0xFFBBBBBB),
    needleColor: Color = Color(0xFFFFA500),
    valueTextColor: Color = Color.White,
    titleColor: Color = Color(0xFFAAAAAA),
    valueTextSize: TextUnit = 20.sp,
    titleTextSize: TextUnit = 11.sp,
    labelTextSize: TextUnit = 8.sp,
    majorTickStrokeWidth: Dp = 1.5.dp,
    minorTickStrokeWidth: Dp = 0.8.dp,
    needleStrokeWidth: Dp = 2.5.dp,
    animationDurationMillis: Int = 600,
    isOn: Boolean = true,
    titleBottomMarging : Dp = 30.dp
) {
    val offLightValue = 0.5f
    val density = LocalDensity.current

    // --- Calcul des intervalles ---
    val effectiveMajorTickInterval = majorTickInterval ?: run {
        val range = maxValue - minValue
        val roughInterval = range / 8.0
        val magnitude = 10f.pow(floor(log10(roughInterval.toFloat())))
        when ((roughInterval / magnitude).roundToInt()) {
            in 0..1 -> magnitude
            in 2..3 -> 2 * magnitude
            in 4..7 -> 5 * magnitude
            else -> 10 * magnitude
        }
    }
    val range = maxValue - minValue
    val tickIntervalValue = effectiveMajorTickInterval / (minorTicksPerMajor + 1)

    // --- Conversions Pixels ---
    val labelTextSizePx = with(density) { labelTextSize.toPx() }
    val majorTickStrokeWidthPx = with(density) { majorTickStrokeWidth.toPx() }
    val minorTickStrokeWidthPx = with(density) { minorTickStrokeWidth.toPx() }
    val needleStrokeWidthPx = with(density) { needleStrokeWidth.toPx() }

    val labelTextPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        color = scaleColor.toArgb()
        textSize = labelTextSizePx
        textAlign = android.graphics.Paint.Align.CENTER
        typeface = android.graphics.Typeface.MONOSPACE
    }

    val clampedValue = currentValue.coerceIn(minValue, maxValue)

    // --- ANIMATIONS ---
    // Animation de la valeur numérique
    val animatedValue by animateFloatAsState(
        targetValue = clampedValue,
        animationSpec = tween(animationDurationMillis, easing = LinearOutSlowInEasing),
        label = "ValueAnimation"
    )

    // Animation de l'angle de l'aiguille
    val targetAngle = mapValueToCircularAngle(clampedValue, minValue, maxValue)
    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(animationDurationMillis, easing = LinearOutSlowInEasing),
        label = "AngleAnimation"
    )

    // --- LE MÉDAILLON ---
    Surface(
        modifier = modifier.size(gaugeSize).aspectRatio(1f),
        shape = androidx.compose.foundation.shape.CircleShape,
        color = cardBackgroundColor,
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {


            // le compteur
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val pivot = Offset(canvasWidth / 2f, canvasHeight / 2f)
                val gaugeRadius = min(canvasWidth, canvasHeight) / 2f * 0.88f

                // Dimensions pour l'effet "flottant"
                val floatingHoleRadius = gaugeRadius * 0.25f // Le cercle vide au centre
                val majorTickInnerRadius = gaugeRadius * 0.85f
                val minorTickInnerRadius = gaugeRadius * 0.90f
                val labelRadius = gaugeRadius * 0.72f
                val needleLength = gaugeRadius * 0.98f
                val needleStartRadius = floatingHoleRadius + 16f // L'aiguille commence juste après le cache

                // 1. INNER SHADOW (Effet de profondeur du rebord)
                drawCircle(
                    brush = Brush.radialGradient(
                        0.8f to Color.Transparent,
                        1.0f to Color.Black.copy(alpha = 0.5f),
                        center = pivot,
                        radius = gaugeRadius * 1.1f
                    ),
                    radius = gaugeRadius * 1.5f,
                    center = pivot
                )




                // 2. TICKS ET LABELS
                val totalTicks = (range / tickIntervalValue).roundToInt()
                for (i in 0..min(totalTicks, 1000)) {
                    val tickValue = minValue + i * tickIntervalValue
                    if (tickValue > maxValue + (tickIntervalValue * 0.1f)) continue

                    val angleDeg = mapValueToCircularAngle(tickValue, minValue, maxValue)
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val remainder = abs((tickValue - minValue) % effectiveMajorTickInterval)
                    val isMajorTick = remainder < (effectiveMajorTickInterval * 0.01f)

                    val tickInnerRadius = if (isMajorTick) majorTickInnerRadius else minorTickInnerRadius
                    val currentTickColor = when {
                        i == 0 -> firstTickColor
                        i == totalTicks -> lastTickColor
                        else -> scaleColor
                    }

                    drawLine(
                        color = if (isOn) currentTickColor else currentTickColor.copy(alpha = offLightValue),
                        start = Offset(pivot.x + cos(angleRad).toFloat() * tickInnerRadius, pivot.y + sin(angleRad).toFloat() * tickInnerRadius),
                        end = Offset(pivot.x + cos(angleRad).toFloat() * gaugeRadius, pivot.y + sin(angleRad).toFloat() * gaugeRadius),
                        strokeWidth = if (isMajorTick) majorTickStrokeWidthPx else minorTickStrokeWidthPx
                    )

                    if (isMajorTick) {
                        val labelText = tickValue.roundToInt().toString()
                        val labelX = pivot.x + cos(angleRad).toFloat() * labelRadius
                        val labelY = pivot.y + sin(angleRad).toFloat() * labelRadius + (labelTextSizePx / 3f)
                        drawIntoCanvas { it.nativeCanvas.drawText(labelText, labelX, labelY, labelTextPaint) }
                    }
                }

                // 3. AIGUILLE (Dessinée AVANT le cache pivot pour l'effet flottant)
                val needleAngleRad = Math.toRadians(animatedAngle.toDouble())
                val needleStartX = pivot.x + cos(needleAngleRad).toFloat() * needleStartRadius
                val needleStartY = pivot.y + sin(needleAngleRad).toFloat() * needleStartRadius
                val needleTipX = pivot.x + cos(needleAngleRad).toFloat() * needleLength
                val needleTipY = pivot.y + sin(needleAngleRad).toFloat() * needleLength

                drawLine(
                    color = if (isOn) needleColor else needleColor.copy(alpha = offLightValue),
                    start = Offset(needleStartX, needleStartY),
                    end = Offset(needleTipX, needleTipY),
                    strokeWidth = needleStrokeWidthPx,
                    cap = StrokeCap.Round
                )

                // 4. CACHE PIVOT (Le cercle qui recouvre la base de l'aiguille)
                drawCircle(
                    color = cardBackgroundColor,
                    radius = floatingHoleRadius,
                    center = pivot
                )

            }


            // 5. TEXTES
            // Valeur numérique au-dessus du centre (ANIMÉE)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format("%.1f", animatedValue),
                        style = TextStyle(
                            color = if(isOn) valueTextColor else valueTextColor.copy(alpha = offLightValue),
                            fontSize = valueTextSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = titleFontFamily
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            // Titre remonté (aligné avec le bas de l'arc)
            Box(modifier = Modifier.fillMaxSize().padding(bottom = titleBottomMarging), contentAlignment = Alignment.BottomCenter) {
                Text(
                    text = title ?: "",
                    style = TextStyle(
                        color = if(isOn) titleColor else titleColor.copy(alpha = offLightValue),
                        fontSize = titleTextSize,
                        fontFamily = titleFontFamily,
                        letterSpacing = 1.sp
                    )
                )
            }


        }
    }
}

private fun mapValueToCircularAngle(value: Float, minValue: Float, maxValue: Float): Float {
    val range = maxValue - minValue
    if (range == 0f) return 270f
    val percentage = (value.coerceIn(minValue, maxValue) - minValue) / range
    return 135f + (percentage * 270f)
}