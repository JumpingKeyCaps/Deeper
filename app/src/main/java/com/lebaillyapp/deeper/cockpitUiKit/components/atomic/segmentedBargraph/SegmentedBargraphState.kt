package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## SegmentedBargraphState
 *
 * Gère l'état réactif du Bargraph.
 * La valeur [value] représente le pourcentage de remplissage (0f à 100f).
 */
class SegmentedBargraphState(initialLevel: Float = 0f) : ComponentState<Float>(initialLevel) {

    /**
     * Met à jour le niveau avec une sécurité de bornage.
     */
    fun updateLevel(newLevel: Float) {
        val clamped = newLevel.coerceIn(0f, 100f)
        update(clamped)
    }
}