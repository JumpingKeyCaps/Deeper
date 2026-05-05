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





/**
 * Un altimètre de style Boeing avec deux rubans défilants et affichage central (en mètres)
 *
 * @param altitude L'altitude actuelle à afficher (en mètres)
 * @param modifier Modifier pour configurer la taille et l'apparence
 * @param minAltitude Altitude minimale à afficher sur le ruban (par défaut 0 mètres)
 * @param maxAltitude Altitude maximale à afficher sur le ruban (par défaut 10000 mètres)
 * @param majorTickEvery Intervalle entre les graduations principales en mètres (par défaut 10)
 * @param minorTickEvery Intervalle entre les graduations secondaires en mètres (par défaut 2)
 * @param animationSpec Spécification pour l'animation de changement d'altitude
 */
@Composable
fun DualTapeAltimeter2(
    altitude: Float,
    modifier: Modifier = Modifier,
    minAltitude: Float = 0f,
    maxAltitude: Float = 10000f,
    majorTickEvery: Int = 10,
    minorTickEvery: Int = 2,
    deepCountSize: Float = 0f,
    customFont: Typeface? = null,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
) {
    // Couleurs inspirées des tableaux de bord d'avion
    val backgroundColor = Color(0xFF181818)
    val tapeColor = Color(0xFF1E1F22)
    val majorTickColor = Color.White
    val minorTickColor = Color(0xFFCCCCCC)
    val textColor = Color.White
    val windowBorderColor = Color(0xFFE0E0E0)
    val windowBackgroundColor = Color(0x66000000)
    val displayBackgroundColor = Color(0xFF181818)
    val displayTextColor = Color(0xFFFFFFFF)

    // Animation de l'altitude
    val animatedAltitude by animateFloatAsState(
        targetValue = altitude.coerceIn(minAltitude, maxAltitude),
        animationSpec = animationSpec,
        label = "altitude"
    )
    // Contexte local requis pour charger la police
    val context = LocalContext.current
    // Chargement de la police personnalisée si elle n'est pas fournie
    val fontTypeface = customFont ?: remember {
        ResourcesCompat.getFont(context, R.font.micro_regular)
    }


    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height

            // Calculer les dimensions des rubans
            val tapeWidth = width * 0.25f
            val windowHeight = height * 0.2f
            val centerAreaWidth = width - (tapeWidth * 2)

            // Dessiner les fonds des rubans
            // Ruban gauche
            drawRect(
                color = tapeColor,
                topLeft = Offset(0f, 0f),
                size = Size(tapeWidth, height)
            )

            // Ruban droit
            drawRect(
                color = tapeColor,
                topLeft = Offset(width - tapeWidth, 0f),
                size = Size(tapeWidth, height)
            )

            // Fond central pour l'affichage numérique
            drawRect(
                color = displayBackgroundColor,
                topLeft = Offset(tapeWidth, height / 2 - height * 0.15f),
                size = Size(centerAreaWidth, height * 0.3f)
            )

            // Zone pour dessiner le ruban gauche
            clipRect(
                left = 0f,
                top = 0f,
                right = tapeWidth,
                bottom = height
            ) {
                drawAltitudeTape(
                    altitude = animatedAltitude,
                    tapeWidth = tapeWidth,
                    isLeftTape = true,
                    majorTickEvery = majorTickEvery,
                    minorTickEvery = minorTickEvery,
                    majorTickColor = majorTickColor,
                    minorTickColor = minorTickColor,
                    textColor = textColor
                )
            }

            // Zone pour dessiner le ruban droit
            clipRect(
                left = width - tapeWidth,
                top = 0f,
                right = width,
                bottom = height
            ) {
                drawAltitudeTape(
                    altitude = animatedAltitude,
                    tapeWidth = tapeWidth,
                    isLeftTape = false,
                    majorTickEvery = majorTickEvery,
                    minorTickEvery = minorTickEvery,
                    majorTickColor = majorTickColor,
                    minorTickColor = minorTickColor,
                    textColor = textColor
                )
            }

            // Dessiner la fenêtre centrale (pour mettre en évidence l'altitude actuelle)
            val windowY = height / 2 - windowHeight / 2

            // Lignes de bordure de la fenêtre pour toute la largeur
            drawLine(
                color = windowBorderColor,
                start = Offset(0f, windowY),
                end = Offset(width, windowY),
                strokeWidth = 1f
            )
            drawLine(
                color = windowBorderColor,
                start = Offset(0f, windowY + windowHeight),
                end = Offset(width, windowY + windowHeight),
                strokeWidth = 1f
            )

            // Pointeurs triangulaires (MODIFIÉS: flèches inversées)
            val pointerSize = windowHeight * 0.2f

            // Pointeur gauche (direction modifiée: pointant vers la droite)
            val leftPath = Path().apply {
                moveTo(tapeWidth, height / 2)
                lineTo(tapeWidth + pointerSize / 2, height / 2 - pointerSize / 2)
                lineTo(tapeWidth + pointerSize / 2, height / 2 + pointerSize / 2)
                close()
            }
            drawPath(
                path = leftPath,
                color = Color.Yellow
            )

            // Pointeur droit (direction modifiée: pointant vers la gauche)
            val rightPath = Path().apply {
                moveTo(width - tapeWidth, height / 2)
                lineTo(width - tapeWidth - pointerSize / 2, height / 2 - pointerSize / 2)
                lineTo(width - tapeWidth - pointerSize / 2, height / 2 + pointerSize / 2)
                close()
            }
            drawPath(
                path = rightPath,
                color = Color.Yellow
            )

            // Afficher l'altitude actuelle en texte numérique au centre
            val textPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                textSize = deepCountSize.sp.toPx()
                color = android.graphics.Color.WHITE
                textAlign = android.graphics.Paint.Align.CENTER
                typeface = fontTypeface

            }

            val altText = String.format("%.1f", animatedAltitude)
            val textX = width / 2
            val textY = height / 2 + textPaint.textSize / 4

            drawContext.canvas.nativeCanvas.drawText(
                altText,
                textX,
                textY,
                textPaint
            )

            // Unité de mesure (mètres)
            textPaint.textSize = (deepCountSize-14).sp.toPx()
            drawContext.canvas.nativeCanvas.drawText(
                "METERS",
                textX,
                textY + textPaint.textSize * 1.8f,
                textPaint
            )
        }
    }
}

/**
 * Dessine un ruban d'altitude avec graduations et chiffres
 */
private fun DrawScope.drawAltitudeTape(
    altitude: Float,
    tapeWidth: Float,
    isLeftTape: Boolean,
    majorTickEvery: Int,
    minorTickEvery: Int,
    majorTickColor: Color,
    minorTickColor: Color,
    textColor: Color,

    ) {
    // Échelle adaptée aux mètres: nous voulons voir environ 50m de visibilité verticale
    val pxPerMeter = size.height / 50f

    // Trouver les graduations visibles
    val visibleRange = (size.height / pxPerMeter).toInt()
    val lowestVisible = (altitude - visibleRange / 2).toInt()
    val highestVisible = (altitude + visibleRange / 2).toInt()

    // Arrondir au plus proche minorTickEvery en dessous
    val tickStart = (floor(lowestVisible.toFloat() / minorTickEvery) * minorTickEvery).toInt()

    for (tickAltitude in tickStart..highestVisible step minorTickEvery) {
        val isMajorTick = tickAltitude % majorTickEvery == 0
        val isSpecialTick = tickAltitude % 100 == 0

        // Position Y de la graduation (inversée car l'altitude augmente vers le haut)
        val y = size.height / 2 + (altitude - tickAltitude) * pxPerMeter

        // Ne dessiner que ce qui est visible
        if (y < 0 || y > size.height) continue

        // Longueur de la graduation (plus longue pour les ticks spéciaux)
        val tickLength = when {
            isSpecialTick -> tapeWidth * 0.3f
            isMajorTick -> tapeWidth * 0.2f
            else -> tapeWidth * 0.1f
        }

        val tickColor = when {
            isSpecialTick -> Color.Yellow
            isMajorTick -> majorTickColor
            else -> minorTickColor
        }

        val tickStrokeWidth = when {
            isSpecialTick -> 2.0f
            isMajorTick -> 1.5f
            else -> 1f
        }

        if (isLeftTape) {
            // Dessiner la graduation à gauche
            drawLine(
                color = tickColor,
                start = Offset(tapeWidth - tickLength, y),
                end = Offset(tapeWidth, y),
                strokeWidth = tickStrokeWidth
            )

            // Pour les graduations majeures et spéciales, ajouter le texte
            if (isMajorTick || isSpecialTick) {
                val textPaint = Paint().asFrameworkPaint().apply {
                    isAntiAlias = true
                    textSize = if (isSpecialTick) 10.sp.toPx() else 9.sp.toPx()
                    color = if (isSpecialTick) android.graphics.Color.YELLOW else android.graphics.Color.WHITE
                    textAlign = android.graphics.Paint.Align.RIGHT
                }

                // MODIFIÉ: Affichage complet du texte pour toutes les graduations
                val text = tickAltitude.toString()

                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    tapeWidth - tickLength - 2.dp.toPx(),
                    y + textPaint.textSize / 3,
                    textPaint
                )
            }
        } else {
            // Dessiner la graduation à droite
            val startX = size.width - tapeWidth

            drawLine(
                color = tickColor,
                start = Offset(startX, y),
                end = Offset(startX + tickLength, y),
                strokeWidth = tickStrokeWidth
            )

            // Pour les graduations majeures et spéciales, ajouter le texte
            if (isMajorTick || isSpecialTick) {
                val textPaint = Paint().asFrameworkPaint().apply {
                    isAntiAlias = true
                    textSize = if (isSpecialTick) 10.sp.toPx() else 9.sp.toPx()
                    color = if (isSpecialTick) android.graphics.Color.YELLOW else android.graphics.Color.WHITE
                    textAlign = android.graphics.Paint.Align.LEFT
                }

                // MODIFIÉ: Affichage complet du texte pour toutes les graduations
                val text = tickAltitude.toString()

                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    startX + tickLength + 2.dp.toPx(),
                    y + textPaint.textSize / 3,
                    textPaint
                )
            }
        }
    }
}