package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * Configuration exhaustive pour la jauge horizontale 180°.
 */
data class HorizontalGaugeConfig(
    val titleParam: String,
    val minValue: Float = 0f,
    val maxValue: Float = 100f,
    val unit: String = "",
    // --- Styles ---
    val primaryColorParam: Color = Color(0xFFBBBBBB),
    val needleColor: Color = Color(0xFFFFA500),
    val firstTickColor: Color = Color(0xFFA40525),
    val lastTickColor: Color = Color(0xFFBBBBBB),
    val cardBackgroundColor: Color = Color(0xFF181818),
    // --- Dimensions & Échelles ---
    val majorTickInterval: Float? = null,
    val minorTicksPerMajor: Int = 4,
    val needleStrokeWidth: Dp = 1.dp,
    val titleFontFamily: FontFamily? = null,
    val valueTextSize: TextUnit = 18.sp,
    // --- LED intégrée ---
    val ledColor: Color = Color(0xFFFF9100),
    val ledBlinkInterval: Int = 0,
    val warningThreshold: Float = 40f,  // Seuil pour passage à l'Orange
    val criticalThreshold: Float = 20f, // Seuil pour passage au Rouge
    val alertIsBelow: Boolean = true // True si l'alerte est "en dessous", False si "au dessus"
) : ComponentConfig(titleParam, primaryColorParam)