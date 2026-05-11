package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.BargraphColorMode

/**
 * ## HorizontalSegmentedBargraphConfig
 *
 * Configuration statique pour la version horizontale de l'afficheur à segments.
 * Définit l'apparence physique et la palette de couleurs de l'instrument.
 *
 * @param titleParam Label identifiant (ex: "FUEL", "THRUST").
 * @param segments Nombre de divisions horizontales.
 * @param colorMode Stratégie de coloration ([BargraphColorMode.FIXED] ou [DYNAMIC]).
 * @param colorScale Palette de couleurs pour le gradient (de gauche à droite).
 * @param inactiveColor Couleur des segments non alimentés.
 * @param backgroundColor Couleur de fond du châssis.
 * @param spacing Espacement entre chaque segment horizontal.
 * @param barCornerRadius Rayon de courbure des segments.
 * @param containerCornerRadius Rayon de courbure du boîtier.
 * @param glowRadius Intensité de l'effet de rayonnement lumineux.
 */
data class HorizontalSegmentedBargraphConfig(
    val titleParam: String,
    val segments: Int = 10,
    val colorMode: BargraphColorMode = BargraphColorMode.DYNAMIC,
    val colorScale: List<Color> = listOf(
        Color(0xFF044F9A),
        Color(0xFF0088FF),
        Color(0xFF00EAFF)
    ),
    val inactiveColor: Color = Color(0xFF0A121A),
    val backgroundColor: Color = Color(0xFF181C25),
    val spacing: Dp = 2.dp,
    val barCornerRadius: Dp = 2.dp,
    val containerCornerRadius: Dp = 4.dp,
    val glowRadius: Dp = 8.dp
) : ComponentConfig(titleParam, backgroundColor)