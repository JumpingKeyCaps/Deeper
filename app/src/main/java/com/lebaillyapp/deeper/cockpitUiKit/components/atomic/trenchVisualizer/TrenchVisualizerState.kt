package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * Définit l'état opérationnel du sonar de profondeur.
 */
enum class TrenchMachineState { OFF, ON, RUNNING }

/**
 * Conteneur de données pour l'état dynamique du TrenchVisualizer.
 * @property scanTrigger Compteur incrémenté pour forcer une séquence de scan.
 */
data class TrenchVisualizerData(
    val depthIntensity: Float = 0f,
    val animatedDepth: Float = 0f,
    val powerFactor: Float = 0f,
    val machineState: TrenchMachineState = TrenchMachineState.OFF,
    val scanTrigger: Long = 0L
)

class TrenchVisualizerState(initialData: TrenchVisualizerData) : ComponentState<TrenchVisualizerData>(initialData) {

    fun updateTargetIntensity(newIntensity: Float) {
        update(value.copy(depthIntensity = newIntensity.coerceIn(0f, 1f)))
    }

    /**
     * Incrémente le trigger pour notifier le Node qu'un nouveau scan est demandé.
     */
    fun triggerManualScan() {
        update(value.copy(scanTrigger = value.scanTrigger + 1))
    }

    fun updateAnimationValues(animDepth: Float, power: Float) {
        update(value.copy(animatedDepth = animDepth, powerFactor = power))
    }

    fun setMachineState(newState: TrenchMachineState) {
        update(value.copy(machineState = newState))
    }

    val isPoweredOn: Boolean
        get() = value.machineState != TrenchMachineState.OFF

    val isRunning: Boolean
        get() = value.machineState == TrenchMachineState.RUNNING
}