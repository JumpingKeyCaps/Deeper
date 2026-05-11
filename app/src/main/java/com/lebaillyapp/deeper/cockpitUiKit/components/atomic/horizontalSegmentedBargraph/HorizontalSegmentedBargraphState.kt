package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## HorizontalSegmentedBargraphState
 *
 * Gère l'état réactif du Bargraph Horizontal.
 * La valeur [value] représente le pourcentage de remplissage (0f à 100f).
 */
class HorizontalSegmentedBargraphState(initialLevel: Float = 0f) : ComponentState<Float>(initialLevel) {

    /**
     * Met à jour le niveau d'affichage de la barre.
     *
     * @param newLevel Nouvelle valeur (sera clampée automatiquement entre 0.0 et 100.0).
     */
    fun updateLevel(newLevel: Float) {
        val clamped = newLevel.coerceIn(0f, 100f)
        update(clamped)
    }
}