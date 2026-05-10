package com.lebaillyapp.deeper.cockpitUiKit.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay.BatterySlotConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay.BatterySlotNode
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.*
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.HorizontalGaugeConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.HorizontalGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.MachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay.PowerLoadState

@Composable
fun DebugPowerSystemScreen() {
    val microFont = FontFamily(Font(R.font.micro_regular))

    // --- 1. ÉTATS DE SIMULATION ---
    var globalLoad by remember { mutableFloatStateOf(45f) }
    var isPowerOn by remember { mutableStateOf(true) }
    var maxAmpCapacity by remember { mutableFloatStateOf(82f) }
    var totalChargeAvailable by remember { mutableFloatStateOf(0f) }

    // --- 2. INSTANCIATION DES COMPOSANTS ---

    // Le Rack de 4 batteries (Approche V1 déjà propre)
    val batteryRack = remember {
        List(4) { index ->
            BatterySlotNode(
                id = "BAT_0${index + 1}",
                config = BatterySlotConfig(
                    slotLabel = "BAT-${index + 1}",
                    rockerConfig = RockerSwitchConfig(
                        iconSource = { painterResource(id = R.drawable.eclaire) },
                        width = 30.dp,
                        height = 50.dp
                    )
                )
            )
        }
    }

    // Le Module de Charge Central (Approche "Boîte Noire" fixée)
    val powerLoadNode = remember {
        PowerLoadNode(
            id = "pwr_module_01",
            config = PowerLoadConfig(
                moduleLabel = "LIVE LOAD",
            )
        )
    }

    val capacityBufferNode = remember {
        HorizontalGaugeNode(
            id = "debug_capacity_buffer",
            config = HorizontalGaugeConfig(
                titleParam = "PEAK LIMIT",
                maxValue = 100f,
                unit = "%",
                majorTickInterval = 25f,
                minorTicksPerMajor = 3,
                titleFontFamily = microFont,
                needleStrokeWidth = 1.dp,
                valueTextSize = 16.sp,
                ledColor = Color(0xFFFAFAFA),
            )
        )
    }

    // --- 3. SYNCHRONISATION GLOBALE ---
    // Regroupement des effets pour plus de clarté
    LaunchedEffect(globalLoad, isPowerOn, maxAmpCapacity) {
        powerLoadNode.sync(globalLoad, isPowerOn, maxAmpCapacity)
        capacityBufferNode.updateGauge(maxAmpCapacity, MachineState.RUNNING)
    }

    LaunchedEffect(totalChargeAvailable) {
        batteryRack.forEach { it.setLevel(totalChargeAvailable) }
    }

    // --- 4. RENDU ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D13)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(2.dp).graphicsLayer(scaleX = 0.95f, scaleY = 0.95f)
        ) {
            // ROW PRINCIPALE
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    batteryRack[0].Render(Modifier)
                    batteryRack[1].Render(Modifier)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Box(modifier = Modifier
                        .size(width = 90.dp, height = 60.dp)
                        .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFF333333), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        capacityBufferNode.Render(modifier = Modifier.padding(0.dp))
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    powerLoadNode.Render(
                        modifier = Modifier.padding(horizontal = 3.dp).height(135.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    batteryRack[2].Render(Modifier)
                    batteryRack[3].Render(Modifier)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // --- PANNEAU DE CONTRÔLE ---
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(Color.Black.copy(0.4f), RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SYSTEM MASTER CONTROL", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)

                Slider(value = globalLoad, onValueChange = { globalLoad = it }, valueRange = 0f..100f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "LOAD: ${globalLoad.toInt()}%",
                        color = if (globalLoad > 90) Color.Red else Color.Cyan,
                        fontFamily = microFont
                    )
                    androidx.compose.material3.Switch(checked = isPowerOn, onCheckedChange = { isPowerOn = it })
                }

                Text("BATTERY LEVEL", color = Color.Gray, fontSize = 8.sp)
                Slider(value = totalChargeAvailable, onValueChange = { totalChargeAvailable = it }, valueRange = 0f..99.9f)

                Text("MAX CAPACITY SETTING", color = Color.Gray, fontSize = 8.sp)
                Slider(value = maxAmpCapacity, onValueChange = { maxAmpCapacity = it }, valueRange = 0f..99.9f)
            }
        }
    }
}