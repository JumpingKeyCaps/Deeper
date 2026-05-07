package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## VerticalSpeedIndicatorState
 *
 * Gère l'état réactif du variomètre.
 * La valeur représente la vitesse verticale actuelle.
 */
class VerticalSpeedIndicatorState(initialSpeed: Float = 0f) : ComponentState<Float>(initialSpeed) {

    /**
     * Met à jour la vitesse affichée.
     * Le bornage est géré directement par le composant via son maxSpeed,
     * mais on peut forcer une sécurité ici si nécessaire.
     */
    fun updateSpeed(newSpeed: Float) {
        update(newSpeed)
    }
}