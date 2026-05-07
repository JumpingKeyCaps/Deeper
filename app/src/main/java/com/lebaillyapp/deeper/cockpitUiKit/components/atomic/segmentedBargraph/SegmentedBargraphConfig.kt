package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## SegmentedBargraphConfig
 *
 * Configuration statique pour l'afficheur à segments.
 * Permet de définir le look-and-feel "hardware" du composant.
 *
 * @param titleParam Titre ou label associé (ex: "BATTERY", "O2").
 * @param segments Nombre de blocs physiques à dessiner.
 * @param colorMode Le comportement des couleurs ([BargraphColorMode.FIXED] ou [DYNAMIC]).
 * @param colorScale La liste des couleurs servant de base au dégradé.
 * @param inactiveColor La couleur d'un segment éteint.
 * @param backgroundColor La couleur du boîtier.
 * @param spacing L'espace entre les segments.
 * @param barCornerRadius L'arrondi des segments individuels.
 * @param containerCornerRadius L'arrondi du boîtier externe.
 * @param glowRadius L'intensité du rayonnement néon.
 */
data class SegmentedBargraphConfig(
    val titleParam: String,
    val segments: Int = 10,
    val colorMode: BargraphColorMode = BargraphColorMode.DYNAMIC,
    val colorScale: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
    val inactiveColor: Color = Color(0xFF151515),
    val backgroundColor: Color = Color(0xFF1E1E1E),
    val spacing: Dp = 2.dp,
    val barCornerRadius: Dp = 2.dp,
    val containerCornerRadius: Dp = 4.dp,
    val glowRadius: Dp = 8.dp
) : ComponentConfig(titleParam, backgroundColor)