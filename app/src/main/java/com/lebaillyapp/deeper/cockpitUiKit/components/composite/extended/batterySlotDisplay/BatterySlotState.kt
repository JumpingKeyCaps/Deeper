package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay

import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## BatterySlotState
 *
 * Gère la synchronisation des données pour un slot de batterie.
 *
 * @param initialAh Valeur de départ (0.0 à 100.0).
 * @param segNode Node composite pour l'affichage digital.
 * @param barNode Node atomique pour le bargraph.
 * @param rockNode Node atomique pour le switch de puissance.
 * @param onPowerToggled Callback métier exécuté quand le switch change d'état.
 */
import kotlinx.coroutines.*

class BatterySlotState(
    initialAh: Float = 0f,
    val segNode: Multi7SegNode,
    val barNode: SegmentedBargraphNode,
    val rockNode: RockerSwitchNode,
    private val onPowerToggled: (Boolean) -> Unit = {}
) : ComponentState<Float>(initialAh) {

    private var internalAh: Float = initialAh
    private var animationJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun updateBatteryData(newValue: Float) {
        internalAh = newValue
        if (rockNode.state.value.isChecked && animationJob?.isActive != true) {
            segNode.setValue(internalAh.toInt().toString())
            barNode.updateValue(internalAh)
        }
    }

    private fun refreshDisplay() {
        val isConnected = rockNode.state.value.isChecked
        animationJob?.cancel()

        if (isConnected) {
            startPowerOnSequence()
        } else {
            startPowerOffSequence()
        }
    }

    /**
     * Animation de montée en charge synchronisée (Bargraph + 7-Segments)
     */
    private fun startPowerOnSequence() {
        animationJob = scope.launch {
            val steps = 12
            val targetValue = internalAh

            for (i in 0..steps) {
                val progressValue = (targetValue / steps) * i

                // --- LOGIQUE DE RETOUR VISUEL ---
                // On s'assure que si on est ON, on affiche au moins 1%
                // pour que le premier segment du bargraph reste allumé
                val visualBarValue = if (progressValue < 1f && targetValue >= 0f) 1f else progressValue
                barNode.updateValue(visualBarValue)

                segNode.setValue(progressValue.toInt().toString())
                delay(60)
            }

            // Sécurité finale : valeur réelle sur les segments, mais minimum 1% sur la barre
            segNode.setValue(targetValue.toInt().toString())
            barNode.updateValue(if (targetValue < 1f) 1f else targetValue)
        }
    }

    /**
     * Animation de descente (Power Off)
     */
    private fun startPowerOffSequence() {
        animationJob = scope.launch {
            val steps = 8
            val startValue = internalAh

            for (i in steps downTo 0) {
                val progressValue = (startValue / steps) * i

                // Ici, comme on éteint physiquement le switch,
                // on autorise la barre à tomber à 0 à la toute fin.
                barNode.updateValue(progressValue)

                if (i > 0) {
                    segNode.setValue(progressValue.toInt().toString())
                } else {
                    segNode.setValue("--")
                }
                delay(30)
            }
            barNode.updateValue(0f)
            segNode.setValue("--")
        }
    }

    fun syncRockerAction() {
        rockNode.onStateChanged = { isConnected ->
            refreshDisplay()
            onPowerToggled(isConnected)
        }
    }

    val isConnected: Boolean get() = rockNode.state.value.isChecked
}