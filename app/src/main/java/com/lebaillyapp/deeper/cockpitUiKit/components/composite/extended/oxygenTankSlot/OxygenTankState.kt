package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.oxygenTankSlot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.HorizontalGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.MachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph.HorizontalSegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ## OxygenTankState
 *
 * État gérant la physique du tank (Litres) et la synchronisation des Nodes enfants.
 */
class OxygenTankState(
    val maxCapacity: Float,
    val flowRate: Float,
    initialLoadLiters: Float,
    val gaugeNode: HorizontalGaugeNode,
    val barGraphNode: HorizontalSegmentedBargraphNode,
    val rockerButtonNode: RockerSwitchNode,
    private val onTransferUpdate: (Float) -> Unit,
    var scope: CoroutineScope
) : ComponentState<Float>(initialLoadLiters) {

    private var currentLiters: Float = initialLoadLiters
    var isPoweredOn by mutableStateOf(false)
    private var transferJob: Job? = null
    private var animationJob: Job? = null

    private val currentPercent: Float
        get() = if (maxCapacity > 0) (currentLiters / maxCapacity) * 100f else 0f

    /**
     * Gestion de l'alimentation électrique du module.
     */
    fun setPower(enabled: Boolean) {
        isPoweredOn = enabled
        animationJob?.cancel()

        if (enabled) {
            startPowerOnSequence()
            // Si le rocker était physiquement resté sur ON (cas rare selon logique toggle), on lance
            if (rockerButtonNode.state.value.isChecked) startTransfer()
        } else {
            stopTransfer()
            // Le switch "saute" car il n'y a plus de maintien magnétique/électrique
            rockerButtonNode.toggle(false)
            startPowerOffSequence()
        }
    }

    fun updateTankData(liters: Float) {
        currentLiters = liters.coerceIn(0f, maxCapacity)
        if (isPoweredOn && animationJob?.isActive != true) {
            val p = currentPercent
            gaugeNode.updateGauge(p, MachineState.ON)
            barGraphNode.updateValue(p, scope)
        }
    }

    private fun startTransfer() {
        transferJob?.cancel()
        if (!isPoweredOn) return

        transferJob = scope.launch {
            val tickMs = 100L
            while (isActive && currentLiters > 0 && isPoweredOn) {
                val amountPerTick = (flowRate * tickMs) / 1000f
                val realExtracted = if (currentLiters >= amountPerTick) amountPerTick else currentLiters

                currentLiters -= realExtracted
                onTransferUpdate(realExtracted)

                val p = currentPercent
                // On utilise RUNNING pour déclencher le blink de la LED sur la gauge
                gaugeNode.updateGauge(p, MachineState.RUNNING)
                barGraphNode.updateValue(p, scope)

                delay(tickMs)
            }
            // Si vide, on repasse en ON simple (plus de flux)
            if (currentLiters <= 0f) {
                gaugeNode.updateGauge(0f, MachineState.ON)
            }
        }
    }

    private fun stopTransfer() {
        transferJob?.cancel()
        transferJob = null
    }

    private fun startPowerOnSequence() {
        animationJob = scope.launch {
            val targetPercent = currentPercent
            gaugeNode.updateGauge(targetPercent, MachineState.ON)
            barGraphNode.updateValue(if (targetPercent < 1f && targetPercent > 0) 1f else targetPercent, scope)
        }
    }

    private fun startPowerOffSequence() {
        animationJob = scope.launch {
            val steps = 10
            val startPercent = currentPercent
            for (i in steps downTo 0) {
                val progress = (startPercent / steps) * i
                barGraphNode.updateValue(progress, scope)
                gaugeNode.updateGauge(progress, MachineState.OFF)
                delay(60)
            }
            barGraphNode.updateValue(0f, scope)
            gaugeNode.updateGauge(0f, MachineState.OFF)
        }
    }

    /**
     * Liaison entre l'action utilisateur sur le Rocker et la logique de transfert.
     */
    fun syncRockerAction() {
        rockerButtonNode.onStateChanged = { isConnected ->
            if (isConnected && !isPoweredOn) {
                // EFFET DISJONCTEUR :
                // On laisse le bouton passer sur ON visuellement,
                // puis on le fait sauter après un court instant.
                scope.launch {
                    delay(250) // Temps de réaction mécanique (très court pour le feeling)
                    rockerButtonNode.toggle(false)
                }
            } else if (isConnected && isPoweredOn) {
                // Cas normal : Jus ON + Bouton ON -> On lance le flux
                startTransfer()
            } else {
                // Cas : Bouton passé sur OFF manuellement
                stopTransfer()
                if (isPoweredOn) startPowerOnSequence() // Retour état de veille
            }
        }
    }

    val isConnected: Boolean get() = rockerButtonNode.state.value.isChecked
}