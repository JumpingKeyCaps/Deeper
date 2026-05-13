package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DebugTrenchVisualizerScreen() {
    // 1. Initialisation du Node
    val trenchNode = remember {
        TrenchVisualizerNode(
            id = "DEBUG_SONAR_01",
            config = TrenchVisualizerConfig(
                titleParam = "TRENCH ANALYZER",
                scanSpeed = 15000,
                peakHeight = 500f,
                sigma = 3.5f,
                gridCols = 15,
                gridRows = 15,
                heatMapScale = listOf(
                    Color(0xFFF50057),
                    Color(0xFFFFEA00),
                    Color(0xFF2979FF)
                )
            ),
            state = TrenchVisualizerState(
                initialData = TrenchVisualizerData(
                    depthIntensity = 0.0f, // On part à plat
                    machineState = TrenchMachineState.ON
                )
            )
        )
    }

    // 2. État LOCAL au screen pour le sélecteur (ne pilote pas le Node en direct)
    var selectorDepth by remember { mutableStateOf(0.5f) }

    val currentData = trenchNode.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp, 120.dp)
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            trenchNode.Render(modifier = Modifier.fillMaxSize())
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp, start = 32.dp, end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Affiche la valeur du sélecteur local
            Text(
                text = "PRESET DEPTH: ${(selectorDepth * 100).toInt()}%",
                color = Color.Cyan.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Le slider modifie selectorDepth, PAS le node
            Slider(
                value = selectorDepth,
                onValueChange = { selectorDepth = it },
                valueRange = 0f..1f,
                enabled = trenchNode.state.isPoweredOn,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Cyan,
                    activeTrackColor = Color.Cyan.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bouton TRIGGER SCAN : Met à jour le node ET lance l'anim
                Button(
                    onClick = {
                        trenchNode.updateDepth(selectorDepth) // On envoie la valeur
                        trenchNode.triggerScan()             // On lance le "Reset & Scan"
                    },
                    modifier = Modifier.weight(1.5f),
                    enabled = trenchNode.state.isPoweredOn,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    )
                ) {
                    Text("LAUNCH SONAR SCAN", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val nextStatus = if (trenchNode.state.isPoweredOn)
                            TrenchMachineState.OFF else TrenchMachineState.ON
                        trenchNode.setStatus(nextStatus)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (trenchNode.state.isPoweredOn)
                            Color(0xFFB71C1C) else Color(0xFF1B5E20)
                    )
                ) {
                    Text(if (trenchNode.state.isPoweredOn) "POWER OFF" else "POWER ON", fontSize = 12.sp)
                }
            }
        }
    }
}