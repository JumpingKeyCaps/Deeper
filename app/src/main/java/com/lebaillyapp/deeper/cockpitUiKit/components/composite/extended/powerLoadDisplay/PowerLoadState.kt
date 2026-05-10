package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.CircularGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.CircularMachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## PowerLoadState
 *
 * Orchestre les états des composants internes du module PowerLoad.
 * La valeur principale (Float) représente la charge actuelle (Load).
 */
class PowerLoadState(
    initialLoad: Float = 0f,
    val gaugeNode: CircularGaugeNode,
    val statusLedNode: LedNode,
    val overloadVoyantNode: GlassButtonNode
) : ComponentState<Float>(initialLoad) {

    /**
     * Met à jour l'affichage complet du module.
     */
    fun update(loadValue: Float, systemIsOn: Boolean, batteryMaxAmpCapacity: Float) {
        this.value = loadValue

        var alimentationEffective = systemIsOn

        // Logique "Disjoncteur"
        if (loadValue >= (batteryMaxAmpCapacity + 10f)) {
            alimentationEffective = false
        }

        // 1. Pilotage de la Jauge
        val machineState = if (alimentationEffective) CircularMachineState.ON else CircularMachineState.OFF
        gaugeNode.updateGauge(loadValue, machineState)

        // 2. Logique de la LED (Mutation de config puisque c'est ton seul levier)
        if (!alimentationEffective) {
            // --- MODE POWER OFF / FAULT ---
            applyLedUpdate(Color.Blue, 800)
            statusLedNode.state.update(true)
        } else {
            // --- MODE POWER ON ---
            val (ledColor, blink) = when {
                loadValue >= (batteryMaxAmpCapacity - 10f) -> Color.Red to 200
                loadValue >= (batteryMaxAmpCapacity - 20f) -> Color(0xFFFFA500) to 0
                else -> Color(0xFF05CB6A) to 0
            }
            applyLedUpdate(ledColor, blink)
            statusLedNode.state.update(true)
        }

        // 3. Pilotage du voyant Overload
        val isOverloaded = alimentationEffective && loadValue >= batteryMaxAmpCapacity
        overloadVoyantNode.state.update(isOverloaded)
    }

    /**
     * Helper pour muter la config du sous-node LED.
     * On vérifie avant pour éviter des recompositions Compose inutiles.
     */
    private fun applyLedUpdate(color: Color, blink: Int) {
        if (statusLedNode.config.colorParam != color || statusLedNode.config.blinkInterval != blink) {
            statusLedNode.config = statusLedNode.config.copy(
                colorParam = color,
                blinkInterval = blink
            )
        }
    }
}