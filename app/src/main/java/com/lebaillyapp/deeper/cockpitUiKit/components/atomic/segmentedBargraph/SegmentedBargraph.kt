package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph

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

/**
 * Définit comment les couleurs sont réparties sur le bargraph.
 */
enum class BargraphColorMode {
    /** Chaque segment a une couleur fixe basée sur sa position (look Arc-en-ciel/Thermomètre). */
    FIXED,
    /** Tous les segments actifs prennent la même couleur basée sur le niveau global (look État de charge). */
    DYNAMIC
}

/**
 * ## SegmentedBargraph
 *
 * Afficheur visuel à segments discrets conçu pour le rendu d'instruments de bord.
 * Ce composant utilise un rendu [Canvas] pur pour garantir une précision graphique
 * et un effet de rayonnement (glow) expansif dépassant les limites du conteneur.
 *
 * ### Caractéristiques :
 * - **Réactivité spatiale** : S'adapte automatiquement à la taille du parent.
 * - **Glow Industriel** : Utilise un `BlurMaskFilter` natif pour simuler l'éclairage néon.
 * - **Interpolation de couleurs** : Supporte des listes de couleurs de n'importe quelle taille
 *   avec un mélange linéaire fluide via [lerp].
 *
 * @param modifier Le modificateur de mise en page (définit la taille de l'instrument).
 * @param currentLevel Niveau actuel à afficher (clampé automatiquement entre 0.0 et 100.0).
 * @param segments Nombre total de segments physiques composant la barre.
 * @param colorMode Stratégie de coloration (voir [BargraphColorMode]).
 * @param colorScale Palette de couleurs représentant l'échelle (du bas vers le haut).
 * @param inactiveColor Teinte des segments non alimentés (état "OFF").
 * @param backgroundColor Teinte du boîtier/châssis de l'instrument.
 * @param spacing Espacement interne et entre les segments (en Dp).
 * @param barCornerRadius Rayon de courbure des coins de chaque segment.
 * @param containerCornerRadius Rayon de courbure des coins du boîtier principal.
 * @param glowRadius Intensité et portée du rayonnement lumineux (en Dp).
 */
@Composable
fun SegmentedBargraph(
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

            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(containerCornerRadiusPx, containerCornerRadiusPx)
            )

            val barAreaWidth = width - (spacingPx * 2)
            val barAreaHeight = height - (spacingPx * 2)
            val barHeight = (barAreaHeight - (spacingPx * (segments - 1))) / segments
            val barCornerRadiusPx = barCornerRadius.toPx()

            for (i in 0 until segments) {
                val drawIndex = (segments - 1) - i
                val threshold = (i.toFloat() / segments) * 100f
                val isActive = clampedLevel > threshold

                // Détermination de la couleur de ce segment précis
                val segmentColor = when (colorMode) {
                    BargraphColorMode.DYNAMIC -> globalActiveColor
                    BargraphColorMode.FIXED -> {
                        val segmentFraction = i.toFloat() / (segments - 1).coerceAtLeast(1)
                        interpolateColorScale(segmentFraction, colorScale)
                    }
                }

                val xOffset = spacingPx
                val yOffset = spacingPx + (drawIndex * (barHeight + spacingPx))

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
                            xOffset + barAreaWidth + (glowRadiusPx / 1.5f),
                            yOffset + barHeight + (glowRadiusPx / 1.5f),
                            barCornerRadiusPx * 2,
                            barCornerRadiusPx * 2,
                            frameworkPaint
                        )
                    }
                }

                drawRoundRect(
                    color = if (isActive) segmentColor else inactiveColor,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barAreaWidth, barHeight),
                    cornerRadius = CornerRadius(barCornerRadiusPx, barCornerRadiusPx)
                )
            }
        }
    }
}

/**
 * Fonction utilitaire pour interpoler une valeur (0..1) à travers une liste de couleurs.
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