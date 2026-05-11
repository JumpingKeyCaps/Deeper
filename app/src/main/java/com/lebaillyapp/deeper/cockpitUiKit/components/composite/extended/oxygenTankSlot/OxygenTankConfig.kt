package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.oxygenTankSlot

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.HorizontalGaugeConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph.HorizontalSegmentedBargraphConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig
import kotlinx.coroutines.CoroutineScope

data class OxygenTankConfig(
    val tankLabel: String,
    val maxCapacityLiters: Float = 100f,     // Capacité totale
    val flowRatePerSecond: Float = 1.5f,    // Débit de transfert vers le Main
    val rockerButtonConfig: RockerSwitchConfig = RockerSwitchConfig(
        titleParam = "CONNECT",
        iconSource = { painterResource(id = R.drawable.power) },
        width = 30.dp,
        height = 60.dp,
        ledColor = Color(0xFF651FFF)
    ),
    val hGaugeConfig: HorizontalGaugeConfig = HorizontalGaugeConfig(
        titleParam = "% Oxy ",
        maxValue = 100f,
        unit = "%",
        majorTickInterval = 25f,
        minorTicksPerMajor = 3,
        titleFontFamily = FontFamily(Font(R.font.micro_regular)),
        needleStrokeWidth = 1.dp,
        valueTextSize = 16.sp,
        ledColor = Color(0xFFFAFAFA),
    ),
    val barGraphConfig: HorizontalSegmentedBargraphConfig = HorizontalSegmentedBargraphConfig(
        titleParam = "LEVEL",
        segments = 10,
        glowRadius = 5.dp,
        barCornerRadius = 1.dp
    ),
    val backgroundColor: Color = Color(0xFF1A1A1A),
    val scope: CoroutineScope
) : ComponentConfig(tankLabel, backgroundColor)