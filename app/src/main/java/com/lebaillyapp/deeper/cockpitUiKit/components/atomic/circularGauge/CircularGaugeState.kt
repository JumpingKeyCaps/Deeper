package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge


import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * Définit l'état opérationnel de la machine ou du capteur lié à la jauge circulaire.
 * OFF : Tout est éteint (LED éteinte, aiguille potentiellement figée ou à zéro).
 * ON : Système sous tension (LED fixe).
 * RUNNING : Système en opération active (LED clignotante ou animée).
 */
enum class CircularMachineState { OFF, ON, RUNNING }

/**
 * Conteneur de données pour l'état dynamique de la jauge circulaire.
 *
 * @property value La valeur physique actuelle mesurée (doit être comprise entre minValue et maxValue de la config).
 * @property machineState État d'activité impactant le rendu visuel (LED, opacité).
 * @property currentColor Couleur dynamique de la jauge (ex: passe du vert au rouge selon les seuils).
 */
data class CircularGaugeData(
    val value: Float,
    val machineState: CircularMachineState = CircularMachineState.OFF,
    val currentColor: Color = Color.Gray
)

/**
 * Implémentation du State pour la CircularGauge.
 * Gère la réactivité de l'UI via le pattern ComponentState.
 *
 * @param initialData Les données initiales au chargement du composant.
 */
class CircularGaugeState(initialData: CircularGaugeData) : ComponentState<CircularGaugeData>(initialData)