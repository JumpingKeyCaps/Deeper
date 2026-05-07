package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.vsi

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator.VerticalSpeedIndicatorConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## VsiModuleConfig
 *
 * Configuration complète pour le module indicateur de vitesse verticale.
 * Regroupe les paramètres de la jauge, des LED et du conteneur visuel.
 *
 * @property titleParam Titre du module (ex: "PRIMARY_VSI").
 * @property unitLabel Libellé de l'unité affiché (ex: "m/s" ou "ft/min").
 * @property neutralThreshold Seuil de vitesse (en valeur absolue) pour déterminer l'état "neutre" (LED Orange).
 * @property vsiConfig Configuration détaillée de l'atome [VerticalSpeedIndicatorNode].
 * @property ledUpColor Couleur de la LED supérieure (vitesse positive).
 * @property ledNeutralColor Couleur de la LED centrale (vitesse nulle/stable).
 * @property ledDownColor Couleur de la LED inférieure (vitesse négative).
 * @property containerColor Couleur de fond de la carte principale.
 */
data class VsiModuleConfig(
    val titleParam: String,
    val unitLabel: String = "m/s",
    val neutralThreshold: Float = 0.2f,
    val vsiConfig: VerticalSpeedIndicatorConfig,
    val ledUpColor: Color = Color(0xFF1DE9B6),
    val ledNeutralColor: Color = Color(0xFFFF9100),
    val ledDownColor: Color = Color(0xFF1DE9B6),
    val containerColor: Color = Color(0xFF27282C)
) : ComponentConfig(titleParam, containerColor)