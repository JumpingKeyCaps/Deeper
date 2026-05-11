package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * Nœud logique de l'altimètre circulaire.
 * Pilote la double aiguille et gère l'état électrique du système hydrostatique.
 */
class CircularAltimeterGaugeNode(
    override val id: String,
    config: CircularAltimeterGaugeConfig,
    state: CircularAltimeterGaugeState = CircularAltimeterGaugeState(AltimeterGaugeData(depth = 0f))
) : BaseComponent<CircularAltimeterGaugeConfig, CircularAltimeterGaugeState>(id, config, state) {

    /**
     * Met à jour l'altimètre.
     *
     * @param newDepth La profondeur actuelle issue de la simulation.
     * @param machineStatus L'état du système (OFF, ON, RUNNING).
     */
    fun updateAltimeter(newDepth: Float, machineStatus: AltimeterMachineState) {

        // Si OFF, on simule une chute des aiguilles ou un blocage à 0
        // (On peut aussi garder la dernière valeur si on veut simuler une panne sèche,
        // mais ici on suit ta logique : OFF = 0m)
        val finalDepth = if (machineStatus == AltimeterMachineState.OFF) {
            0f
        } else {
            newDepth
        }

        // Mise à jour de l'état via la méthode update de ton ComponentState
        state.update(
            AltimeterGaugeData(
                depth = finalDepth,
                machineState = machineStatus,
                currentScaleColor = config.scaleColor // On peut mapper ici une couleur de warning si besoin
            )
        )
    }

    @Composable
    override fun Render(modifier: Modifier) {
        // Le composable que nous avons finalisé ensemble
        CircularAltimeterGauge(
            modifier = modifier,
            config = config,
            state = state
        )
    }
}