package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * Configuration statique pour une LED réaliste.
 */
data class LedConfig(
    val titleParam: String,
    val colorParam: Color = Color.Red,
    val size: Float = 20f,
    val haloSpacer: Int = 3,
    val blinkInterval: Int = 0
) : ComponentConfig(titleParam, colorParam)