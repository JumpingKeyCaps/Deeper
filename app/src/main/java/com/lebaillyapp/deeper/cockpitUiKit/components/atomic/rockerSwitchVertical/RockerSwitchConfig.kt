package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## Configuration visuelle et comportementale du Rocker Switch.
 *
 * @property title Label optionnel pour l'identification du switch.
 * @property iconSource Lambda fournissant le Painter de l'icône (On/Upper part).
 * @property width Largeur du composant (Défaut: 45.dp).
 * @property height Hauteur du composant (Défaut: 80.dp).
 * @property ledColor Couleur de la LED d'état (Lower part).
 * @property animationDuration Durée de la bascule en ms.
 */
data class RockerSwitchConfig(
    val titleParam: String = "",
    val primaryColorParam: Color = Color(0xFF7CD0D0D0),
    val iconSource: @Composable () -> Painter,
    val width: Dp = 35.dp,
    val height: Dp = 64.dp,
    val ledColor: Color = Color(0xFFAB0020),
    val animationDuration: Int = 500
): ComponentConfig(titleParam, primaryColorParam)