package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.trenchSonar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton.CircularPushConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchVisualizerConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.BargraphColorMode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig
import kotlinx.coroutines.CoroutineScope

/**
 * ## TrenchSonarModuleConfig
 *
 * Configuration pour le module Sonar en mode AUTO-PING.
 * Le bouton Rocker devient l'interrupteur d'alimentation (Power).
 *
 * @param moduleLabel Libellé de façade.
 * @param maxDetectionRange Portée max du sonar.
 * @param pingCooldownMs Intervalle entre les pings automatiques (ms).
 * @param isAutoPingEnabled Si true, le module ping en boucle quand il est allumé.
 */
data class TrenchSonarModuleConfig(
    val moduleLabel: String = "TERRAIN ANALYZER",
    val maxDetectionRange: Float = 1000f,
    val pingCooldownMs: Long = 2000L,
    val isAutoPingEnabled: Boolean = true, // On rajoute ça pour la flexibilité

    // 1. Radar 3D (Tes réglages Peak/Sigma sont gardés)
    val visualizerConfig: TrenchVisualizerConfig = TrenchVisualizerConfig(
        titleParam = "TRENCH ANALYZER",
        scanSpeed = 2000,
        peakHeight = 1000f,
        sigma = 3.5f,
        gridCols = 14,
        gridRows = 14,
        heatMapScale = listOf(
            Color(0xFFF50057),
            Color(0xFFFFEA00),
            Color(0xFF2979FF)
        )
    ),

    // 2. Multi-7 Segments - DEPTH
    val depthMultiSegConfig: Multi7SegConfig = Multi7SegConfig(
        titleParam = "DEPTH",
        numDigits = 4,
        spacing = 1.dp,
        showZeroWhenEmpty = false,
        reversedOverride = true,
        baseSegmentConfig = SevenSegmentConfig(
            segmentHorizontalLength = 8.dp,
            segmentLength = 8.dp,
            segmentThickness = 2.dp,
            titleParam = "DEPTH_SEG",
            onColor = Color(0xFFF50057),
            offColor = Color(0xFF44031B)
        )
    ),


    // 4. Bargraph (Charge de l'Auto-Ping)
    val barGraphConfig: SegmentedBargraphConfig = SegmentedBargraphConfig(
        titleParam = "SONAR_CHARGE",
        segments = 5,
        colorMode = BargraphColorMode.DYNAMIC,
        colorScale = listOf(Color(0xFF2979FF), Color(0xFFFFC400), Color(0xFFF50057)),
        glowRadius = 1.dp
    ),

    // 5. Bouton Rocker - Devient l'interrupteur POWER
    val powerButtonConfig: CircularPushConfig = CircularPushConfig(
        size = 50.dp,
        iconSource = { painterResource(id = R.drawable.power) },
        activeColor = Color(0xFFE5E5E5),
        animationDuration = 300
    ),

    val scope: CoroutineScope
) : ComponentConfig(moduleLabel, Color(0xFF1A1A1A))