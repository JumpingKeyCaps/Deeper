package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.CircularGaugeConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonConfig

/**
 * ## PowerLoadConfig
 *
 * Configuration agrégée pour le module composite de charge (PowerLoad).
 * Cette classe encapsule les configurations de tous les sous-nodes atomiques.
 *
 * @param slotLabel Identifiant textuel du module (ex: "PWR-LOAD").
 * @param gaugeConfig Configuration de l'instrument circulaire (Jauge Amp/s).
 * @param ledConfig Configuration de la LED de statut (couleur, clignotement).
 * @param overloadConfig Configuration du voyant de surcharge (Overload).
 * @param backgroundColor Couleur de fond du boîtier composite.
 */
data class PowerLoadConfig(
    val moduleLabel: String = "PWR-LOAD",
    val gaugeConfig: CircularGaugeConfig = CircularGaugeConfig(
        titleParam = "AMP",
        maxValue = 100f,
        majorTickInterval = 20f ,
        titleTextSize = 10.sp,
        labelTextSize = 7.sp,
        valueTextSize = 15.sp,
        titleBottomMarging = 25.dp,
        titleFontFamily = FontFamily(Font(R.font.micro_regular)),
        animationDurationMillis = 2000
    ),
    val ledConfig: LedConfig = LedConfig(
        titleParam = "STATUS",
        colorParam = Color.Green,
        size = 10f,
        haloSpacer = 4
    ),
    val overloadConfig: GlassButtonConfig = GlassButtonConfig(
        titleParam = "OVERLOAD",
        colorParam = Color(0xFFD00831),
        textColor = Color.White,
        textSize = 12.sp,
        cornerRadius = 3.dp,
        glowRadius = 2.0f
    ),
    val backgroundColor: Color = Color(0xFF1A1A1A)
) : ComponentConfig(moduleLabel, backgroundColor)