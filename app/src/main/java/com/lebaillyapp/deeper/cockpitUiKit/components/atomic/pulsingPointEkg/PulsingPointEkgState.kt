package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.pulsingPointEkg

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## PulsingPointEkgState
 *
 * État gérant le niveau d'oxygène et le calcul du rythme cardiaque associé.
 */
class PulsingPointEkgState(initialOxygen: Int = 100) : ComponentState<Int>(initialOxygen) {

    // Ton composant a besoin d'un MutableState<Int> en entrée
    val bpmState: MutableState<Int> = mutableIntStateOf(0)

    fun updateOxygen(level: Int) {
        update(level.coerceIn(0, 100))
    }
}