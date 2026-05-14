package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.oxygenTankSlot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DebugOxygenTankScreen() {
    val scope = rememberCoroutineScope()

    // --- 1. ÉTAT DU SYSTÈME GLOBAL ---
    var mainReservoirLiters by remember { mutableFloatStateOf(0f) }
    var isMainPowerOn by remember { mutableStateOf(false) }

    // --- 2. INSTANCIATION DES NODES (La Baie de Stockage) ---
    val tankNodes = remember {
        listOf(
            Triple("1", 200f, 1f),  // Label, Capacité, Débit
            Triple("2", 200f, 2f),
            Triple("3", 200f, 2f),
            Triple("4", 500f, 2f)
        ).map { (label, cap, flow) ->
            OxygenTankNode(
                id = "tank_$label",
                config = OxygenTankConfig(
                    tankLabel = "TANK-$label",
                    maxCapacityLiters = cap,
                    flowRatePerSecond = flow,
                    scope = scope,
                    isInverted = label.toInt() % 2 == 0
                ),
                onOxygenInjected = { amount -> mainReservoirLiters += amount }
            )
        }
    }

    // --- 3. RENDU ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D13)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            // AFFICHAGE DU RÉSULTAT (Main Reservoir)
            Text(text = "MAIN O2 STORAGE", color = Color.Cyan.copy(alpha = 0.6f), fontSize = 12.sp)
            Text(
                text = "${"%.2f".format(mainReservoirLiters)} L",
                color = Color.Cyan,
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(40.dp))

            // LA COLONNE DE TANKS (Alignement vertical pur)
            Column(
                modifier = Modifier
                    .weight(1f, fill = false) // Permet de ne pas pousser le panneau de contrôle hors écran
                    .verticalScroll(rememberScrollState()) // Sécurité si l'écran est petit
                .graphicsLayer(scaleX = 0.99f, scaleY = 0.99f, rotationY = 0f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                tankNodes.forEach { tank ->
                    tank.Render(modifier = Modifier)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // PANNEAU DE CONTRÔLE GLOBAL
            Column(
                modifier = Modifier
                    .width(320.dp)
                    .background(Color.White.copy(0.05f), RoundedCornerShape(12.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // MASTER POWER
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("MASTER POWER", color = if(isMainPowerOn) Color.Green else Color.Gray)
                    Switch(
                        checked = isMainPowerOn,
                        onCheckedChange = { state ->
                            isMainPowerOn = state
                            tankNodes.forEach { it.setPowerStatus(state) }
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp), color = Color.White.copy(0.1f))

                // EMERGENCY REFILL
                Text("EMERGENCY REFILL (ALL TANKS)", color = Color.Gray, fontSize = 12.sp)
                Button(
                    onClick = {
                        tankNodes.forEach { it.fillTank(it.config.maxCapacityLiters) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E2129)
                    )
                ) {
                    Text("FULL RECHARGE")
                }
            }
        }
    }
}