package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.trenchSonar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchMachineState
import kotlinx.coroutines.delay

@Composable
fun DebugTrenchSonarScreen() {
    val scope = rememberCoroutineScope()

    val sonarConfig = remember {
        TrenchSonarModuleConfig(
            moduleLabel = "SONAR UNIT AX-1",
            maxDetectionRange = 1500f,
            pingCooldownMs = 2000L, // Cooldown un peu plus rapide pour le debug
            scope = scope
        )
    }

    val sonarNode = remember {
        TrenchSonarModuleNode(
            id = "debug_sonar_01",
            config = sonarConfig
        )
    }

    // Simulation du moteur de jeu ################################
    LaunchedEffect(Unit) {
        var subDepth = 1f
        var floorDepth = 1800f // Le fond est à 1800m

        // On simule une descente constante vers les abysses
        while (true) {
            val isPowerOn = sonarNode.state.pingButtonNode.state.value.isChecked

            if (isPowerOn) {
                if (!sonarNode.state.isRecharging) {
                    sonarNode.ping(subDepth, floorDepth)
                }
            }

            // --- LOGIQUE DE MOUVEMENT ---
            // On descend de 0.5m toutes les 100ms
            // On s'arrête à 1800m pour tester le mode "DEEP" (hors range de 1500m)
            if (subDepth < 1800f) {
                subDepth += 0.5f
            }

            // Si tu veux tester la remontée manuellement plus tard,
            // il suffira de modifier subDepth ailleurs.

            delay(100)
        }
    }

    //#################################################
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117)),
        contentAlignment = Alignment.Center
    ) {
        sonarNode.Render(modifier = Modifier)
    }
}