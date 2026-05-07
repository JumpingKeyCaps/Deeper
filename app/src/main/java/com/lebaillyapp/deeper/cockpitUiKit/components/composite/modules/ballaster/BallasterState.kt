package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.ballaster

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchData
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge.VerticalGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## BallasterState
 *
 * Orchestre les états de remplissage et assure la synchronisation entre les composants
 * atomiques (Gauges, LEDs, Switches) du module de ballast.
 *
 * Cette classe gère l'interdépendance entre le flux d'eau et de gaz, ainsi que
 * les états de verrouillage (isEnabled) spécifiques à chaque système.
 *
 * @param initialLevel Niveau de remplissage initial (0.0f à 100.0f).
 * @property waterLed Nœud contrôlant la LED d'état du flux d'eau.
 * @property gasLed Nœud contrôlant la LED d'état du flux de gaz.
 * @property waterGauge Jauge affichant le niveau d'eau.
 * @property gasGauge Jauge affichant le niveau de gaz (complémentaire à l'eau).
 * @property waterSwitch Interrupteur de commande pour l'eau.
 * @property gasSwitch Interrupteur de commande pour le gaz.
 */
class BallasterState(
    initialLevel: Float = 0f,
    val waterLed: LedNode,
    val gasLed: LedNode,
    val waterGauge: VerticalGaugeNode,
    val gasGauge: VerticalGaugeNode,
    val waterSwitch: RockerSwitchNode,
    val gasSwitch: RockerSwitchNode
) : ComponentState<Float>(initialLevel) {

    /** État de disponibilité du circuit d'eau (gestion des pannes/locks). */
    var isWaterEnabled by mutableStateOf(true)

    /** État de disponibilité du circuit de gaz (gestion des pannes/locks). */
    var isGasEnabled by mutableStateOf(true)

    /**
     * Synchronise l'ensemble du système avec les données provenant du matériel ou de la simulation.
     *
     * @param level Niveau d'eau actuel (0..100).
     * @param isWaterActive Définit si le remplissage d'eau est en cours.
     * @param isGasActive Définit si l'injection de gaz est en cours.
     */
    fun sync(
        level: Float,
        isWaterActive: Boolean,
        isGasActive: Boolean
    ) {
        // 1. Mise à jour de la valeur racine
        update(level)

        // --- LOGIQUE D'AUTO-CUTOFF ---
        // On détermine si les switches DOIVENT s'éteindre (sécurité niveau plein/vide)
        val shouldWaterBeOff = level >= 100f && isWaterActive
        val shouldGasBeOff = level <= 0f && isGasActive

        // On calcule l'état final réel (si cutoff, on force à false)
        val finalWaterActive = if (shouldWaterBeOff) false else isWaterActive
        val finalGasActive = if (shouldGasBeOff) false else isGasActive

        // 2. Synchronisation des LEDs avec l'état final
        waterLed.state.update(finalWaterActive)
        gasLed.state.update(finalGasActive)

        // 3. Mise à jour des Gauges
        waterGauge.updateValue(level)
        gasGauge.updateValue(100f - level)

        // 4. Synchronisation des Switches
        // Si finalWaterActive est différent de isWaterActive, le switch bascule tout seul !
        waterSwitch.state.update(
            RockerSwitchData(isChecked = finalWaterActive, isEnabled = isWaterEnabled)
        )
        gasSwitch.state.update(
            RockerSwitchData(isChecked = finalGasActive, isEnabled = isGasEnabled)
        )
    }

    /**
     * Verrouille ou déverrouille les contrôles du circuit d'eau.
     * Utile pour simuler une panne moteur ou une valve bloquée.
     */
    fun setWaterLock(locked: Boolean) {
        isWaterEnabled = !locked
        // Update immédiat du state atomique du switch
        val currentData = waterSwitch.state.value
        waterSwitch.state.update(currentData.copy(isEnabled = isWaterEnabled))
    }

    /**
     * Verrouille ou déverrouille les contrôles du circuit de gaz.
     */
    fun setGasLock(locked: Boolean) {
        isGasEnabled = !locked
        val currentData = gasSwitch.state.value
        gasSwitch.state.update(currentData.copy(isEnabled = isGasEnabled))
    }
}