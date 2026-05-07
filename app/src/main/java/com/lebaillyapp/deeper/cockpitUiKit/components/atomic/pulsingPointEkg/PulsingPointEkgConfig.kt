package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.pulsingPointEkg

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## PulsingPointEkgConfig
 *
 * Configuration pour le moniteur de signes vitaux.
 */
data class PulsingPointEkgConfig(
    val titleParam: String,
    val pointRadius: Dp = 2.dp,
    val trailThickness: Dp = 0.5.dp,
    val glowRadius: Dp = 4.dp,
    val normalColor: Color = Color(0xFF1DE9B6),
    val warningColor: Color = Color(0xFFF1AB2B),
    val dangerColor: Color = Color(0xFFFC4242)
) : ComponentConfig(titleParam, normalColor)