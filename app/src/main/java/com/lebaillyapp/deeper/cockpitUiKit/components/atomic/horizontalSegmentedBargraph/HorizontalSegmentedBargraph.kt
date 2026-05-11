package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.BargraphColorMode

/**
 * ## HorizontalSegmentedBargraph
 *
 * Version horizontale du bargraph à segments.
 * Idéal pour les indicateurs de consommation instantanée ou les barres de progression système.
 */
@Composable
fun HorizontalSegmentedBargraph(
    modifier: Modifier = Modifier,
    currentLevel: Float,
    segments: Int = 5,
    colorMode: BargraphColorMode = BargraphColorMode.FIXED,
    colorScale: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
    inactiveColor: Color = Color(0xFF1A1A1A),
    backgroundColor: Color = Color(0xFF2D2D2D),
    spacing: Dp = 2.dp,
    barCornerRadius: Dp = 2.dp,
    containerCornerRadius: Dp = 4.dp,
    glowRadius: Dp = 12.dp
) {
    val clampedLevel = currentLevel.coerceIn(0f, 100f)

    // Calcul de la couleur globale (utilisée uniquement en mode DYNAMIC)
    val globalActiveColor = if (colorMode == BargraphColorMode.DYNAMIC && colorScale.size > 1) {
        interpolateColorScale(clampedLevel / 100f, colorScale)
    } else {
        colorScale.first()
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val spacingPx = spacing.toPx()
            val glowRadiusPx = glowRadius.toPx()
            val containerCornerRadiusPx = containerCornerRadius.toPx()

            // 1. Dessin du châssis (Fond)
            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(containerCornerRadiusPx, containerCornerRadiusPx)
            )

            // 2. Calcul des dimensions des segments
            val barAreaWidth = width - (spacingPx * 2)
            val barAreaHeight = height - (spacingPx * 2)
            // On divise la largeur disponible par le nombre de segments
            val barWidth = (barAreaWidth - (spacingPx * (segments - 1))) / segments
            val barCornerRadiusPx = barCornerRadius.toPx()

            for (i in 0 until segments) {
                val threshold = (i.toFloat() / segments) * 100f

                // --- LOGIQUE DE SÉCURITÉ O2 (STRICT 0%) ---
                val isFirstSegment = i == 0
                // Toujours active si premier segment OU si le niveau dépasse le threshold
                val isActive = clampedLevel > threshold || isFirstSegment

                // Détermination de la couleur
                val segmentColor = if (isFirstSegment && clampedLevel == 0f) {
                    // ROUGE UNIQUEMENT À 0% PILE
                    Color.Red
                } else {
                    // Dès que c'est > 0f, on prend la couleur du dégradé (Bleu/Cyan)
                    when (colorMode) {
                        BargraphColorMode.DYNAMIC -> globalActiveColor
                        BargraphColorMode.FIXED -> {
                            val segmentFraction = i.toFloat() / (segments - 1).coerceAtLeast(1)
                            interpolateColorScale(segmentFraction, colorScale)
                        }
                    }
                }

                // Calcul de la position
                val xOffset = spacingPx + (i * (barWidth + spacingPx))
                val yOffset = spacingPx

                // 3. Dessin du Glow (Utilise maintenant la couleur corrigée)
                if (isActive && glowRadiusPx > 0f) {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = segmentColor.copy(alpha = 0.45f)
                        }
                        val frameworkPaint = paint.asFrameworkPaint()
                        frameworkPaint.maskFilter = android.graphics.BlurMaskFilter(
                            glowRadiusPx,
                            android.graphics.BlurMaskFilter.Blur.NORMAL
                        )

                        canvas.nativeCanvas.drawRoundRect(
                            xOffset - (glowRadiusPx / 1.5f),
                            yOffset - (glowRadiusPx / 1.5f),
                            xOffset + barWidth + (glowRadiusPx / 1.5f),
                            yOffset + barAreaHeight + (glowRadiusPx / 1.5f),
                            barCornerRadiusPx * 2,
                            barCornerRadiusPx * 2,
                            frameworkPaint
                        )
                    }
                }

                // 4. Dessin du segment physique
                drawRoundRect(
                    color = if (isActive) segmentColor else inactiveColor,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barWidth, barAreaHeight),
                    cornerRadius = CornerRadius(barCornerRadiusPx, barCornerRadiusPx)
                )
            }
        }
    }
}

/**
 * Fonction utilitaire (copie locale ou importée) pour l'interpolation des couleurs.
 */
private fun interpolateColorScale(fraction: Float, colorScale: List<Color>): Color {
    if (colorScale.isEmpty()) return Color.Gray
    if (colorScale.size == 1) return colorScale.first()

    val scaledFraction = fraction.coerceIn(0f, 1f) * (colorScale.size - 1)
    val firstColorIndex = scaledFraction.toInt().coerceAtMost(colorScale.size - 2)
    val secondColorIndex = firstColorIndex + 1
    val remainder = scaledFraction - firstColorIndex

    return lerp(colorScale[firstColorIndex], colorScale[secondColorIndex], remainder)
}