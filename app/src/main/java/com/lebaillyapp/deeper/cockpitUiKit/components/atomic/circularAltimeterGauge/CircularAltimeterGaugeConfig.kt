package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * Configuration dédiée à l'altimètre circulaire à double aiguille.
 */
data class CircularAltimeterGaugeConfig(
    val titleParam: String,
    val primaryColorParam: Color = Color(0xFFBBBBBB),
    val gaugeSize: Dp = 150.dp,
    val animationDurationMillis: Int = 2000,
    val titleBottomMarginRatio: Float = 0.35f,
    val cardBackgroundColor: Color = Color(0xFF181818),
    val scaleColor: Color = Color(0xFFBBBBBB),
    val graduationBigColor: Color = Color(0xFFC6FF00),
    val needleColor: Color = Color(0xFFE8E8E8),
    val smallNeedleColor: Color = Color(0xFFFFA500),
    val valueTextColor: Color = Color.White,
    val gaugeFontFamily: FontFamily? = null,
    val majorTickStrokeWidth: Dp = 2.dp,
    val minorTickStrokeWidth: Dp = 1.dp,
    val smallNeedleStrokeWidth: Dp = 5.dp,
    val bigNeedleStrokeWidth: Dp = 3.dp,
    val pivotHoleRatio: Float = 0.45f
) : ComponentConfig(titleParam, primaryColorParam)