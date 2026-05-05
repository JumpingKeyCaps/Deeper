package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## Configuration de l'altimètre à double ruban.
 *
 * @property titleParam Nom du système (ex: "DEPTH" ou "ALT").
 * @property primaryColorParam Couleur principale (utilisée pour les pointeurs/alertes).
 * @property minAltitude Limite basse du ruban.
 * @property maxAltitude Limite haute du ruban.
 * @property majorTickEvery Intervalle des grandes graduations (ex: 10m).
 * @property minorTickEvery Intervalle des petites graduations (ex: 2m).
 * @property centerFontSize Taille de la police pour l'affichage numérique central.
 */
data class DualTapeAltimeterConfig(
    val titleParam: String = "ALTITUDE",
    val primaryColorParam: Color = Color.Yellow,
    val minAltitude: Float = 0f,
    val maxAltitude: Float = 10000f,
    val majorTickEvery: Int = 10,
    val minorTickEvery: Int = 2,
    val recordDepthParam: Float = 0f,
    val centerFontSize: Float = 24f,
    val customFont: android.graphics.Typeface? = null
) : ComponentConfig(titleParam, primaryColorParam)