package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.ballaster

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge.VerticalGaugeConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## BallasterConfig
 *
 * Configuration pour le module de gestion des ballasts.
 *
 * @property titleParam Identifiant du module.
 * @property maxLevel Capacité maximale des ballasts.
 * @property waterIcon Ressource de l'icône pour le switch eau.
 * @property gasIcon Ressource de l'icône pour le switch gaz.
 * @property containerColor Couleur de fond de la Card.
 * @property gaugeConfig Configuration commune pour les deux jauges verticales.
 */
data class BallasterConfig(
    val titleParam: String,
    val maxLevel: Float = 100f,
    val waterIcon: Int,
    val gasIcon: Int,
    val containerColor: Color = Color(0xFF27282C),
    // Utilisation de ton constructeur exact
    val gaugeConfig: VerticalGaugeConfig = VerticalGaugeConfig(
        titleParam = "BALLAST_GAUGE",
        maxCapacity = 100f,
        stepValue = 5f,
        majorGraduationStep = 5,
        needleColor = Color(0xFFFF8800),
        graduationColor = Color.White,
        backgroundColor = Color(0xFF1E1E1E),
        textColor = Color.White,
        cornerRadius = 5.dp,
        paddingTop = 10.dp,
        paddingBottom = 10.dp,
        zeroGraduationColor = Color(0xFF980924)
    )
) : ComponentConfig(titleParam, containerColor)