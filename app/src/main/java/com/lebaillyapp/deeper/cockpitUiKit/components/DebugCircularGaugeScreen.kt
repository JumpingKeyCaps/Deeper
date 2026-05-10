package com.lebaillyapp.deeper.cockpitUiKit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.*
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadState

@Composable
fun DebugCircularGaugeScreen() {
    val microFont = FontFamily(Font(R.font.micro_regular))

    // 1. Initialisation du Node Composite et de ses sous-éléments
    val powerLoadNode = remember {
        // Le Node Composite final
        PowerLoadNode(
            id = "pwr_module_01",
            config = PowerLoadConfig(
                moduleLabel = "PWR-LOAD",
            ),
        )
    }




    // État de simulation
    var simulatedValue by remember { mutableFloatStateOf(45f) }
    var isPowerOn by remember { mutableStateOf(true) }
    var batteryMaxAmpCapacity by remember { mutableFloatStateOf(82f) } // 4 bat de 15 amp

    var scalerRender by remember { mutableFloatStateOf(1.0f) }



    // 2. Synchronisation via la méthode sync du Node
    LaunchedEffect(simulatedValue, isPowerOn) {
        powerLoadNode.sync(simulatedValue, isPowerOn,batteryMaxAmpCapacity)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0F14)), // Un peu plus sombre pour le cockpit
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 3. RENDER DU COMPOSITE
            powerLoadNode.Render(modifier = Modifier.padding(10.dp).graphicsLayer(scaleX = scalerRender, scaleY = scalerRender))

            Spacer(modifier = Modifier.height(60.dp))

            // --- Panneau de Simulation ---
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .background(Color.Black.copy(0.3f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "SIMULATION CONTROL",
                    color = Color.DarkGray,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )

                Slider(
                    value = simulatedValue,
                    onValueChange = { simulatedValue = it },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = Color.White)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "LOAD: ${simulatedValue.toInt()}%",
                        color = if (simulatedValue > 90) Color.Red else Color.Cyan,
                        fontFamily = microFont,
                        fontSize = 14.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("PWR", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.padding(end = 8.dp))
                        androidx.compose.material3.Switch(
                            checked = isPowerOn,
                            onCheckedChange = { isPowerOn = it }
                        )
                    }
                }

                Slider(
                    value = scalerRender,
                    onValueChange = { scalerRender = it },
                    valueRange = 0.0f..1.5f,
                    colors = SliderDefaults.colors(thumbColor = Color.White)
                )

            }
        }
    }
}