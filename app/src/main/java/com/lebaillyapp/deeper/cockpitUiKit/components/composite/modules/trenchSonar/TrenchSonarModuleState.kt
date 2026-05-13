package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.trenchSonar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton.CircularPushNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchVisualizerNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchMachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs

/**
 * ## TrenchSonarModuleState
 * Logiciel interne du module : gère le cycle de vie (Power ON/OFF)
 * et la gestion automatique du ping.
 */
class TrenchSonarModuleState(
    val config: TrenchSonarModuleConfig,
    val visualizerNode: TrenchVisualizerNode,
    val barGraphNode: SegmentedBargraphNode,
    val depthDisplayNode: Multi7SegNode,
    val pingButtonNode: CircularPushNode
) : ComponentState<Boolean>(false) { // Initialement à FALSE (éteint)

    private var rechargeJob: Job? = null
    var isRecharging by mutableStateOf(false)
        private set

    init {
        // État initial : tout éteint
        shutdownSystems()

        // Le bouton rocker devient l'interrupteur général
        pingButtonNode.onStateChanged = { isPowered ->
            update(isPowered) // On met à jour le state global du module
            if (isPowered) {
                bootSystems()
            } else {
                shutdownSystems()
            }
        }
    }

    private fun bootSystems() {
        visualizerNode.state.setMachineState(TrenchMachineState.ON)
        barGraphNode.updateValue(100f)
        // Les 7-segs attendront le premier ping ou la prochaine update de vitesse
    }

    private fun shutdownSystems() {
        rechargeJob?.cancel()
        isRecharging = false

        // Extinction visuelle de tous les sous-composants
        visualizerNode.state.setMachineState(TrenchMachineState.OFF)
        visualizerNode.state.updateTargetIntensity(0f)

        barGraphNode.updateValue(0f)

        // 7segment update
        depthDisplayNode.setValue("")
    }


    /**
     * Déclenche un Ping unique.
     */
    fun triggerPing(subDepth: Float, floorDepth: Float) {
        if (!value || isRecharging) return

        val distance = abs(floorDepth - subDepth)
        val inRange = distance <= config.maxDetectionRange

        config.scope.launch {
            if (inRange) {
                val scanValue = (distance / config.maxDetectionRange).coerceIn(0f, 1f)
                visualizerNode.state.updateTargetIntensity(scanValue)
                depthDisplayNode.setValue(distance.toInt().toString())
            } else {
                visualizerNode.state.updateTargetIntensity(1f)
                depthDisplayNode.setValue("deep")
            }

            visualizerNode.state.triggerManualScan()
            startCooldownCycle()
        }
    }

    private fun startCooldownCycle() {
        rechargeJob?.cancel()
        rechargeJob = config.scope.launch {
            isRecharging = true
            barGraphNode.updateValue(0f)

            val startTime = System.currentTimeMillis()
            val duration = config.pingCooldownMs.toFloat()

            while (isActive && value) { // On vérifie aussi que le module est toujours ON
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed / duration) * 100f

                if (progress >= 100f) {
                    barGraphNode.updateValue(100f)
                    break
                }

                barGraphNode.updateValue(progress)
                delay(30)
            }
            isRecharging = false

            // NOTE : Le auto-ping sera géré par ton Screen/Node
            // via la vérification de !isRecharging && value
        }
    }
}