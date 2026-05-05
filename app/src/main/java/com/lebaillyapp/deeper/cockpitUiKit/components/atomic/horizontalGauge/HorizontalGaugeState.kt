package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * Définit l'état opérationnel de la machine liée au composant.
 */
enum class MachineState { OFF, ON, RUNNING }

/**
 * Conteneur de données pour l'état de la jauge.
 * @property value La valeur physique actuelle (ex: 0.0 à 100.0).
 * @property machineState L'état d'activité (gère si la LED est fixe, clignotante ou éteinte).
 * @property currentColor La couleur calculée selon les paliers de sécurité.
 */
data class GaugeData(
    val value: Float,
    val machineState: MachineState = MachineState.OFF,
    val currentColor: Color = Color.Gray
)

/**
 * Implémentation du state pour la jauge horizontale.
 */
class HorizontalGaugeState(initialValue: GaugeData) : ComponentState<GaugeData>(initialValue)