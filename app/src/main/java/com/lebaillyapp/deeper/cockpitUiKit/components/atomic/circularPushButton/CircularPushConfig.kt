package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

data class CircularPushConfig(
    val iconSource: @Composable () -> Painter,
    val size: Dp = 48.dp,
    val activeColor: Color = Color(0xFFEFEFEF),
    val animationDuration: Int = 150
) : ComponentConfig("", activeColor)