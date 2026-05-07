package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun VerticalGauge(
    modifier: Modifier = Modifier,
    currentValue: Float,
    maxCapacity: Float,
    stepValue: Float,
    majorGraduationStep: Int = 5,
    needleColor: Color = Color(0xFFFF8800), // Orange par défaut
    graduationColor: Color = Color.White,
    backgroundColor: Color = Color(0xFF2D2D2D), // Gris foncé par défaut
    textColor: Color = Color.White,
    cornerRadius: Dp = 0.dp, // Rayon des coins arrondis
    paddingTop: Dp = 0.dp,    // Padding supérieur personnalisable
    paddingBottom: Dp = 0.dp,  // Padding inférieur personnalisable
    zeroGraduationColor: Color = Color(0xFF980924), // Couleur de la graduation zéro
) {
    // Vérifier et ajuster les valeurs
    val adjustedCurrentValue = currentValue.coerceIn(0f, maxCapacity)
    val safeStepValue = stepValue.coerceAtLeast(0.1f) // Éviter les steps trop petits ou négatifs

    // Calculer automatiquement le nombre total de graduations basé sur la capacité et le step
    val totalGraduations = calculateTotalGraduations(maxCapacity, safeStepValue)

    // Calculer la position de l'aiguille selon la valeur actuelle
    val needlePosition = remember { Animatable(0f) }
    val targetPosition = 1f - (adjustedCurrentValue / maxCapacity)

    // Animation pour le déplacement de l'aiguille
    LaunchedEffect(adjustedCurrentValue) {
        needlePosition.animateTo(
            targetValue = targetPosition,
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessLow,
                visibilityThreshold = 0.001f
            )
        )
    }

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Convertir les paddings Dp en pixels
            val paddingTopPx = paddingTop.toPx()
            val paddingBottomPx = paddingBottom.toPx()

            // Calculer les dimensions en fonction de la taille du composant et des paddings personnalisés
            val usableHeight = height - (paddingTopPx + paddingBottomPx)

            // Définir la zone des graduations et la longueur des lignes
            val graduationStartX = width * 0.3f
            val minorGraduationEndX = width * 0.50f
            val majorGraduationEndX = width * 0.55f
            val needleLength = width * 0.35f // Longueur de l'aiguille adaptée à la largeur
            val needleThickness = (height / 100).coerceAtLeast(1.dp.toPx()) // Épaisseur de l'aiguille adaptative

            // Calculer la valeur médiane
            val medianValue = maxCapacity / 2

            // Dessiner les graduations
            val graduationSpacing = usableHeight / (totalGraduations - 1).coerceAtLeast(1)

            for (i in 0 until totalGraduations) {
                val y = paddingTopPx + i * graduationSpacing
                // Calculer la valeur réelle de chaque graduation
                val graduationValue = maxCapacity - (i * safeStepValue)
                val isMajor = i % majorGraduationStep == 0

                // Déterminer la couleur de la graduation
                val currentGraduationColor = when {
                    graduationValue <= 0.001f -> zeroGraduationColor // Graduation zéro (avec une petite marge d'erreur)
                    else -> graduationColor // Autres graduations
                }

                // Ligne de graduation
                drawLine(
                    color = currentGraduationColor,
                    start = Offset(graduationStartX, y),
                    end = Offset(if (isMajor) majorGraduationEndX else minorGraduationEndX, y),
                    strokeWidth = if (isMajor) (height / 200).coerceAtLeast(1.dp.toPx()) else (height / 300).coerceAtLeast(0.5.dp.toPx()),
                    cap = StrokeCap.Round
                )

                // Texte pour les graduations majeures
                if (isMajor) {
                    val displayValue = if (graduationValue < 0) 0f else graduationValue // Éviter les valeurs négatives
                    val text = displayValue.roundToInt().toString()
                    val fontSize = (height / 33).coerceAtLeast(8.sp.toPx())

                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = TextStyle(
                            color = currentGraduationColor, // Utiliser la même couleur que la graduation
                            fontSize = fontSize.toSp()
                        ),
                        topLeft = Offset(
                            majorGraduationEndX + width * 0.03f,
                            y - fontSize / 2
                        )
                    )
                }
            }

            // Dessiner l'aiguille par-dessus les graduations
            val needleY = paddingTopPx + needlePosition.value * usableHeight
            drawLine(
                color = needleColor,
                start = Offset(0f, needleY),
                end = Offset(needleLength, needleY),
                strokeWidth = needleThickness,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * Calcule le nombre total de graduations en fonction de la capacité maximale et du step
 */
private fun calculateTotalGraduations(maxCapacity: Float, stepValue: Float): Int {
    // Le nombre de graduations est (capacité max / step) + 1
    // +1 car nous incluons également la graduation zéro
    return ceil(maxCapacity / stepValue).toInt() + 1
}