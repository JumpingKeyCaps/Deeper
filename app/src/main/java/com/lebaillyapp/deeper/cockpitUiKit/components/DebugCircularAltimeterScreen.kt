package com.lebaillyapp.deeper.cockpitUiKit.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
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
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge.AltimeterMachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge.CircularAltimeterGauge
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge.CircularAltimeterGaugeConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge.CircularAltimeterGaugeNode

@Composable
fun DebugCircularAltimeterScreen() {
    // --- 1. INITIALISATION DU NODE (Via remember pour qu'il survive aux recompositions) ---
    val altimeterNode = remember {
        CircularAltimeterGaugeNode(
            id = "ALTI_01",
            config = CircularAltimeterGaugeConfig(
                titleParam = "DEPTH",
                gaugeSize = 120.dp,
                gaugeFontFamily = FontFamily(Font(R.font.micro_regular)),
                graduationBigColor = Color(0xFFC6FF00),
                scaleColor = Color(0xFFB6B6B6),
                needleColor = Color(0xFFFFFFFF),
                smallNeedleColor = Color(0xFFFFA500)
            )
        )
    }

    // --- 2. ÉTAT DE SIMULATION (Local à l'écran de debug) ---
    var simDepth by remember { mutableFloatStateOf(0f) }
    var isSystemOn by remember { mutableStateOf(true) }

    // --- 3. SYNCHRONISATION DU NODE ---
    // On met à jour le node dès qu'une valeur de simu change
    LaunchedEffect(simDepth, isSystemOn) {
        val status = if (isSystemOn) AltimeterMachineState.RUNNING else AltimeterMachineState.OFF
        altimeterNode.updateAltimeter(simDepth, status)
    }

    // --- 4. RENDU ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF171B26)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp)
        ) {

            // APPEL DU RENDU VIA LE NODE
            altimeterNode.Render(modifier = Modifier)

            Spacer(modifier = Modifier.height(60.dp))

            // --- PANNEAU DE CONTRÔLE DE DEBUG ---
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(Color.White.copy(0.05f), RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Switch d'alimentation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("INSTRUMENT POWER", color = Color.Gray, fontSize = 14.sp)
                    Switch(
                        checked = isSystemOn,
                        onCheckedChange = { isSystemOn = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Slider de profondeur
                Text(
                    text = "CURRENT DEPTH: ${simDepth.toInt()} m",
                    color = Color.Cyan,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )

                Slider(
                    value = simDepth,
                    onValueChange = { simDepth = it },
                    valueRange = 0f..11000f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.Cyan.copy(alpha = 0.5f)
                    )
                )

                // Boutons de paliers rapides
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(0f, 1250f, 9500f).forEach { palier ->
                        Button(
                            onClick = { simDepth = palier },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("${palier.toInt()}m", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}