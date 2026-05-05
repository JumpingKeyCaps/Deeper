package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## État de l'altimètre.
 *
 * @property currentAltitude L'altitude en mètres.
 */
data class DualTapeAltimeterData(
    val currentAltitude: Float = 0f,
    val recordDepth: Float = 0f
)

class DualTapeAltimeterState(initial: DualTapeAltimeterData) : ComponentState<DualTapeAltimeterData>(initial)