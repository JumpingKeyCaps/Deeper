package com.lebaillyapp.deeper.cockpitUiKit.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * ## Gestionnaire d'état générique pour un composant.
 *
 * Utilise le mécanisme de "Snapshot State" de Compose pour déclencher des
 * recompositions locales et performantes.
 *
 * @param T Le type de donnée géré par ce state (Float, Boolean, String, etc.).
 * @property initialValue La valeur par défaut au démarrage du composant.
 */
abstract class ComponentState<T>(initialValue: T) {
    /**
     * #### La valeur actuelle observée par l'UI.
     *
     * Toute modification de cette variable déclenche la mise à jour du composant.
     */
    var value by mutableStateOf(initialValue)
        private set // On empêche la modification directe sans passer par update()

    /**
     * #### Met à jour la valeur du state.
     *
     * Cette méthode doit être appelée par le moteur de données à chaque "tick".
     * @param newValue La nouvelle donnée brute reçue.
     */
    fun update(newValue: T) {
        if (value != newValue) {
            value = newValue
        }
    }
}