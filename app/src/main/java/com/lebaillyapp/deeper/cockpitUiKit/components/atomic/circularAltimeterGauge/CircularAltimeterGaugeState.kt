package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularAltimeterGauge

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * État opérationnel de l'altimètre (capteur de pression/hydrostatique).
 * OFF : Système éteint, aiguilles figées ou tombées à zéro, pas de rétroéclairage.
 * ON : Initialisé, prêt à mesurer.
 * RUNNING : En cours de mesure active (fluctuations temps réel).
 */
enum class AltimeterMachineState { OFF, ON, RUNNING }

/**
 * Données dynamiques pour l'altimètre.
 *
 * @property depth Profondeur actuelle en mètres.
 * @property machineState État électrique/logiciel du capteur.
 * @property currentScaleColor Permet de faire varier la couleur de l'échelle (ex: rouge si pression critique).
 */
data class AltimeterGaugeData(
    val depth: Float,
    val machineState: AltimeterMachineState = AltimeterMachineState.OFF,
    val currentScaleColor: Color = Color(0xFFBBBBBB)
)

/**
 * Implémentation du State pour le CircularAltimeterGauge.
 */
class CircularAltimeterGaugeState(initialData: AltimeterGaugeData) : ComponentState<AltimeterGaugeData>(initialData)