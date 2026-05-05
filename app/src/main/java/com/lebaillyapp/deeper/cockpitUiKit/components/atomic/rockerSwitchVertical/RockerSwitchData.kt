package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## Représente les données dynamiques du switch.
 *
 * @property isChecked État de la bascule (true = On/Haut, false = Off/Bas).
 * @property isEnabled Définit si l'interaction est possible (gestion des pannes).
 */
data class RockerSwitchData(
    val isChecked: Boolean = false,
    val isEnabled: Boolean = true
)

class RockerSwitchState(initial: RockerSwitchData) : ComponentState<RockerSwitchData>(initial)