package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## VerticalSpeedIndicatorConfig
 *
 * Configuration pour le variomètre (VSI).
 * Mappe l'intégralité des paramètres de ton composant original.
 */
data class VerticalSpeedIndicatorConfig(
    val titleParam: String,
    val maxSpeed: Float = 10f,
    val majorTickInterval: Float = 2f,
    val minorTicksPerMajor: Int = 1,
    val cardBackgroundColor: Color = Color(0xFF1A1A1A),
    val indicatorColor: Color = Color(0xFFFFA500),
    val scaleColor: Color = Color.White,
    val zeroLineColor: Color = Color.LightGray,
    val tickWidthMajor: Dp = 1.5.dp,
    val tickWidthMinor: Dp = 0.75.dp,
    val labelTextSize: Dp = 11.dp,
    val cardElevation: Dp = 4.dp,
    val cardCornerRadius: Dp = 8.dp,
    val internalPadding: Dp = 10.dp,
    val animationDurationMillis: Int = 300
) : ComponentConfig(titleParam, cardBackgroundColor)