package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * Composant Node pour la LED. Prêt à être injecté dans le layout.
 */
class LedNode(
    override val id: String,
    config: LedConfig,
    state: LedState = LedState()
) : BaseComponent<LedConfig, LedState>(id, config, state) {

    @Composable
    override fun Render(modifier: Modifier) {
        // On appelle le composant ui
        RealisticLED(
            modifier = modifier,
            isOn = state.value,         // Provient du State (Dynamique)
            color = config.primaryColor, // Provient de la Config (Statique)
            size = config.size,
            haloSpacer = config.haloSpacer,
            blinkInterval = config.blinkInterval
        )
    }
}