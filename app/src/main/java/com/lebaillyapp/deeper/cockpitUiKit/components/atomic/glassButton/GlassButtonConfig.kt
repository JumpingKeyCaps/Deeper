package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig
/**
 * ## Configuration structurelle pour un bouton d'aspect "verre".
 *
 * Définit l'esthétique statique du bouton qui ne change pas pendant l'exécution.
 *
 * @param titleParam Le texte affiché sur le bouton.
 * @param colorParam La couleur de base du bouton et de son halo.
 * @param textColor La couleur du texte (gère l'opacité automatiquement selon l'état).
 * @param width Largeur fixe du bouton.
 * @param height Hauteur fixe du bouton.
 * @param cornerRadius Rayon des arrondis pour l'aspect plastique et verre.
 * @param textSize Taille de la police de caractère.
 * @param glowRadius Facteur d'étalement du halo lumineux (0.0f à 1.0f).
 **/
data class GlassButtonConfig(
    val titleParam: String,
    val colorParam: Color = Color.Blue,
    val textColor: Color = Color.White,
    val width: Dp = 120.dp,
    val height: Dp = 60.dp,
    val cornerRadius: Dp = 12.dp,
    val textSize: TextUnit = 16.sp,
    val glowRadius: Float = 0.5f
) : ComponentConfig(titleParam, colorParam)