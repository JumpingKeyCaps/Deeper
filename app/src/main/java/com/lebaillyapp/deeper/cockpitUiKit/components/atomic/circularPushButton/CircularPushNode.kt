package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent


class CircularPushNode(
    override val id: String,
    config: CircularPushConfig,
    state: CircularPushState = CircularPushState(CircularPushData())
) : BaseComponent<CircularPushConfig, CircularPushState>(id, config, state) {

    var onStateChanged: ((Boolean) -> Unit)? = null

    fun toggle(forcedState: Boolean? = null) {
        val newState = forcedState ?: !state.value.isChecked
        state.update(state.value.copy(isChecked = newState))
        onStateChanged?.invoke(newState)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val haptic = LocalHapticFeedback.current
        CircularPushButton(
            modifier = modifier,
            isChecked = state.value.isChecked,
            iconSource = config.iconSource(),
            activeColor = config.activeColor,
            size = config.size,
            animationDuration = config.animationDuration,
            onToggle = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                toggle(it)
            }
        )
    }
}