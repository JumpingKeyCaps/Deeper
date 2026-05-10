package com.lebaillyapp.deeper.cockpitUiKit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay.BatterySlotConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay.BatterySlotNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegConfig

@Composable
fun DebugComponentScreen() {
    // --- 1. ÉTAT DE SIMULATION ---
    var testLevel by remember { mutableFloatStateOf(95f) }

    // --- 2. INSTANCIATION DES 4 NODES ---
    // On utilise un remember sur une liste pour garder nos instances
    val batteryRack = remember {
        List(4) { index ->
            BatterySlotNode(
                id = "BAT_0${index + 1}",
                config = BatterySlotConfig(
                    slotLabel = "BAT-${index + 1}",
                    rockerConfig = RockerSwitchConfig(
                        iconSource = { painterResource(id = R.drawable.eclaire) },
                        width = 30.dp,
                        height = 60.dp
                    )
                ),
                onPowerToggled = { isConnected ->
                    println("Rack Slot ${index + 1} is ${if (isConnected) "ONLINE" else "OFFLINE"}")
                }
            )
        }
    }

    // --- 3. MISE À JOUR DE LA DATA (Global Sync) ---
    LaunchedEffect(testLevel) {
        batteryRack.forEach { it.setLevel(testLevel) }
    }

    // --- 4. RENDU ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D13))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // AFFICHAGE DU RACK
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
                .graphicsLayer(rotationX = -0f, rotationY = -0f, scaleX = 0.8f, scaleY = 0.8f),
            horizontalArrangement = Arrangement.spacedBy(2.dp) // Espacement entre les tranches
        ) {
            batteryRack.forEach { node ->
                node.Render(Modifier)
            }
        }

        Spacer(modifier = Modifier.height(250.dp))

        // CONTRÔLE DE DEBUG (Pour tester la synchro et le 1% minimum)
        Slider(
            value = testLevel,
            onValueChange = { testLevel = it },
            valueRange = 0f..100f,
            modifier = Modifier.width(150.dp)
        )


    }
}