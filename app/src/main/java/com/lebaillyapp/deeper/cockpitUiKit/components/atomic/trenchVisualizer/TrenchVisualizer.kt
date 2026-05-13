package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import kotlin.math.*

/**
 * TrenchVisualizer : Un composant de grille 3D isométrique.
 *
 * @param depthIntensity L'intensité animée du creux (pilotée par le Node).
 * @param powerFactor Facteur d'alimentation (0.0 à 1.0) pour le ScaleY et l'Alpha.
 * @param config Configuration statique.
 */
@Composable
fun TrenchVisualizer(
    modifier: Modifier = Modifier,
    depthIntensity: Float,
    powerFactor: Float = 1f,
    config: TrenchVisualizerConfig = TrenchVisualizerConfig("DEPTH")
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TrenchRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = config.scanSpeed * 3, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Angle"
    )

    val currentAngle = if (powerFactor > 0.01f) rotationAngle else 0f

    Canvas(
        modifier = modifier
            .clipToBounds()
            .graphicsLayer {
                alpha = powerFactor
                scaleY = powerFactor
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {
        if (powerFactor < 0.01f) return@Canvas

        val w = size.width
        val h = size.height

        // --- PRE-CALCULS STATIQUES ---
        val angleRad = Math.toRadians(currentAngle.toDouble()).toFloat()
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)
        val sinIso = sin(PI / 6).toFloat()
        val cosIso = cos(PI / 6).toFloat()

        val centerCol = config.gridCols / 2f
        val centerRow = config.gridRows / 2f

        // Zoom adaptatif simplifié (on évite le tableau de points juste pour le calcul du zoom)
        val baseGridHeight = (config.gridCols + config.gridRows) * sinIso * 20f
        val zoomFactor = (h / (baseGridHeight + (config.peakHeight * depthIntensity)))
            .coerceAtMost(w / (baseGridHeight * 1.5f)) * 0.8f

        val dynamicCellSize = 20f * zoomFactor
        val adjPeak = config.peakHeight * zoomFactor
        val verticalOffset = (depthIntensity * adjPeak) * config.verticalBias
        val sigmaSq2 = 2 * config.sigma * config.sigma

        // --- FONCTION DE CALCUL Z & PROJECTION (Inline pour éviter les allocations) ---
        fun getZ(c: Float, r: Float): Float {
            val dx = c - centerCol
            val dy = r - centerRow
            return -adjPeak * depthIntensity * exp(-(dx * dx + dy * dy) / sigmaSq2)
        }

        fun project(c: Float, r: Float, z: Float): Offset {
            val tx = c - centerCol
            val ty = r - centerRow
            val rx = tx * cosA - ty * sinA
            val ry = tx * sinA + ty * cosA
            return Offset(
                x = (rx - ry) * cosIso * dynamicCellSize + w / 2,
                y = (rx + ry) * sinIso * dynamicCellSize - z - verticalOffset + h / 2
            )
        }

        // --- RENDU UNIQUE ---
        val lineAlpha = 0.8f * powerFactor
        val glowAlpha = 0.1f * powerFactor

        // On parcourt la grille. On ne stocke RIEN. On calcule et on dessine direct.
        for (row in 0..config.gridRows) {
            for (col in 0..config.gridCols) {
                val fCol = col.toFloat()
                val fRow = row.toFloat()

                val z = getZ(fCol, fRow)
                val p1 = project(fCol, fRow, z)

                // Calcul de couleur simplifié (Heatmap) sans allocation de liste
                // On simule le lerp direct sur les couleurs de la config
                val t = ((z - (-adjPeak * depthIntensity)) / (adjPeak * depthIntensity)).coerceIn(0f, 1f)
                val colorBase = if (t < 0.5f) {
                    lerp(config.heatMapScale[0], config.heatMapScale[1], t * 2f)
                } else {
                    lerp(config.heatMapScale[1], config.heatMapScale[2], (t - 0.5f) * 2f)
                }
                val finalColor = colorBase.copy(alpha = lineAlpha)

                // Dessin horizontal
                if (col < config.gridCols) {
                    val p2 = project(fCol + 1f, fRow, getZ(fCol + 1f, fRow))
                    // OPTI : drawLine avec Color au lieu de Brush (évite allocation de Brush et Gradient)
                    drawLine(color = finalColor, start = p1, end = p2, strokeWidth = 2f, cap = StrokeCap.Round)
                    drawLine(color = Color.White.copy(alpha = glowAlpha), start = p1, end = p2, strokeWidth = 4f, cap = StrokeCap.Round)
                }

                // Dessin vertical
                if (row < config.gridRows) {
                    val p2 = project(fCol, fRow + 1f, getZ(fCol, fRow + 1f))
                    drawLine(color = finalColor, start = p1, end = p2, strokeWidth = 2f, cap = StrokeCap.Round)
                    drawLine(color = Color.White.copy(alpha = glowAlpha), start = p1, end = p2, strokeWidth = 4f, cap = StrokeCap.Round)
                }
            }
        }
    }
}