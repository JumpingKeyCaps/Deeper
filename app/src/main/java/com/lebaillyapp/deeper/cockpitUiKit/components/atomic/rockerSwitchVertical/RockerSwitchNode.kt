package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * Nœud logique du Rocker Switch.
 * Gère les changements d'état et déclenche le retour haptique lors de l'interaction.
 */
class RockerSwitchNode(
    override val id: String,
    config: RockerSwitchConfig,
    state: RockerSwitchState = RockerSwitchState(RockerSwitchData()),
    var onStateChanged: ((Boolean) -> Unit)? = null
) : BaseComponent<RockerSwitchConfig, RockerSwitchState>(id, config, state) {

    /**
     * Alterne l'état du switch.
     *
     * @param forcedState Si spécifié, force l'état au lieu d'inverser l'actuel.
     */
    fun toggle(forcedState: Boolean? = null) {
        if (!state.value.isEnabled) return
        val newState = forcedState ?: !state.value.isChecked
        state.update(state.value.copy(isChecked = newState))
        onStateChanged?.invoke(newState)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val currentData = state.value
        val haptic = LocalHapticFeedback.current

        RockerSwitchVertical(
            modifier = modifier,
            isChecked = currentData.isChecked,
            ledColor = config.ledColor,
            onToggle = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                toggle(it)
            },
            width = config.width,
            height = config.height,
            iconSource = config.iconSource(),
            animationDuration = config.animationDuration
        )
    }
}