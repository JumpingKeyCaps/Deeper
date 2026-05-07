package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.vsi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator.VerticalSpeedIndicatorNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## VsiModuleNode
 *
 * Composant composite représentant un instrument de vitesse verticale complet.
 * Il encapsule une jauge verticale, trois LED de statut et un affichage numérique.
 */
class VsiModuleNode(
    override val id: String,
    config: VsiModuleConfig
) : BaseComponent<VsiModuleConfig, VsiModuleState>(
    id,
    config,
    VsiModuleState(
        vsiNode = VerticalSpeedIndicatorNode("${id}_gauge", config.vsiConfig),
        ledUp = LedNode("${id}_led_up", LedConfig("UP", config.ledUpColor, size = 15f, haloSpacer = 1, blinkInterval = 1000)),
        ledNeutral = LedNode("${id}_led_mid", LedConfig("MID", config.ledNeutralColor, size = 20f, haloSpacer = 1)),
        ledDown = LedNode("${id}_led_down", LedConfig("DOWN", config.ledDownColor, size = 15f, haloSpacer = 1, blinkInterval = 1000))
    )
) {
    /**
     * Déclenche le rendu du module complet.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        VsiModuleDisplay(
            modifier = modifier,
            state = state,
            config = config
        )
    }

    /**
     * Interface simplifiée pour injecter la donnée de vol.
     */
    fun setVerticalSpeed(speed: Float) {
        state.updateAll(speed, config.neutralThreshold)
    }
}