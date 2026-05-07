package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## Configuration statique exhaustive pour la [VerticalGauge].
 *
 * Regroupe absolument tous les paramètres de design et de structure qui définissent
 * l'apparence et l'échelle de la jauge.
 *
 * @property titleParam Le titre de l'indicateur.
 * @property maxCapacity La valeur maximale affichable.
 * @property stepValue L'intervalle entre chaque petite graduation.
 * @property majorGraduationStep Le nombre de steps entre deux graduations majeures (avec texte).
 * @property needleColor La couleur de l'aiguille (utilisée comme couleur primaire).
 * @property graduationColor Couleur des graduations standard.
 * @property backgroundColor Couleur de fond du boîtier.
 * @property textColor Couleur des textes de graduations.
 * @property cornerRadius Rayon des coins arrondis.
 * @property paddingTop Padding supérieur personnalisable.
 * @property paddingBottom Padding inférieur personnalisable.
 * @property zeroGraduationColor Couleur spécifique pour la graduation zéro.
 */
data class VerticalGaugeConfig(
    val titleParam: String,
    val maxCapacity: Float,
    val stepValue: Float,
    val majorGraduationStep: Int = 5,
    val needleColor: Color = Color(0xFFFF8800),
    val graduationColor: Color = Color.White,
    val backgroundColor: Color = Color(0xFF2D2D2D),
    val textColor: Color = Color.White,
    val cornerRadius: Dp = 0.dp,
    val paddingTop: Dp = 0.dp,
    val paddingBottom: Dp = 0.dp,
    val zeroGraduationColor: Color = Color(0xFF980924)
) : ComponentConfig(titleParam, needleColor)