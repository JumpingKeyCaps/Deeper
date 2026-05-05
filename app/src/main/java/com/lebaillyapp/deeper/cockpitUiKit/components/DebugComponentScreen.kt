package com.lebaillyapp.deeper.cockpitUiKit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter.DualTapeAltimeterConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter.DualTapeAltimeterNode
import kotlinx.coroutines.delay

@Composable
fun DebugComponentScreen() {
    val depthNode = remember {
        DualTapeAltimeterNode(
            id = "DEPTH_INDICATOR",
            config = DualTapeAltimeterConfig(
                titleParam = "DEPTH",
                minAltitude = -30000f,
                maxAltitude = 0f,
                majorTickEvery = 10,
                minorTickEvery = 2,
                centerFontSize = 30f,
                recordDepthParam = -124.7f // On place un record à -100m
            )
        )
    }

    var simuDepth by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            simuDepth += 0.5f // Descente

            val finalValue = if (simuDepth == 0f) 0f else -simuDepth
            depthNode.updateAltitude(finalValue)

            // Si on dépasse -20000, on reset pour le test
            if (simuDepth > 1250f) simuDepth = 0f
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        depthNode.Render(
            modifier = Modifier
                .width(180.dp)
                .height(220.dp)
                .padding(3.dp)
        )
    }
}