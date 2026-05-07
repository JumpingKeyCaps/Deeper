package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.vsi

import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator.VerticalSpeedIndicatorNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## VsiModuleState
 *
 * Gère l'état dynamique du module VSI.
 * Distribue la valeur de vitesse à la jauge et calcule l'état ON/OFF des LED.
 *
 * @param initialSpeed Vitesse verticale de départ.
 * @property vsiNode Référence vers le Node de la jauge analogique.
 * @property ledUp Référence vers la LED de montée.
 * @property ledNeutral Référence vers la LED de stabilité.
 * @property ledDown Référence vers la LED de descente.
 */
class VsiModuleState(
    initialSpeed: Float = 0f,
    val vsiNode: VerticalSpeedIndicatorNode,
    val ledUp: LedNode,
    val ledNeutral: LedNode,
    val ledDown: LedNode
) : ComponentState<Float>(initialSpeed) {

    /**
     * Met à jour l'ensemble des composants du module.
     *
     * @param speed La nouvelle vitesse verticale.
     * @param threshold Le seuil de tolérance pour la zone neutre.
     */
    fun updateAll(speed: Float, threshold: Float) {
        // Mise à jour de la valeur principale du State
        update(speed)

        // Pilotage de la jauge
        vsiNode.updateValue(speed)

        // Logique métier des LED
        ledUp.state.update(speed > threshold)
        ledNeutral.state.update(speed >= -threshold && speed <= threshold)
        ledDown.state.update(speed < -threshold)
    }
}