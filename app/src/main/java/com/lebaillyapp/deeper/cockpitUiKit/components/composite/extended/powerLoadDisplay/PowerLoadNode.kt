package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge.CircularGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## PowerLoadNode
 *
 * Nœud composite orchestrant le module de charge d'alimentation.
 * Encapsule une jauge circulaire, une LED de statut et un voyant d'overload.
 *
 * @param id Identifiant unique du module.
 * @param config Configuration agrégée contenant les styles des sous-composants.
 */
class PowerLoadNode(
    override val id: String,
    config: PowerLoadConfig
) : BaseComponent<PowerLoadConfig, PowerLoadState>(
    id,
    config,
    // Initialisation interne du State et des sous-Nodes
    PowerLoadState(
        gaugeNode = CircularGaugeNode("${id}_gauge", config.gaugeConfig),
        statusLedNode = LedNode("${id}_led", config.ledConfig),
        overloadVoyantNode = GlassButtonNode("${id}_ovl", config.overloadConfig)
    )
) {

    /**
     * #### sync
     * Point d'entrée unique pour piloter le module.
     * Met à jour la logique métier et redistribue les états aux sous-nodes.
     *
     * @param currentLoad Charge actuelle (Amp/s).
     * @param isPowered État de l'alimentation système.
     * @param batMaxAmpCapacity Capacité max avant surcharge.
     */
    fun sync(currentLoad: Float, isPowered: Boolean, batMaxAmpCapacity: Float) {
        state.update(currentLoad, isPowered, batMaxAmpCapacity)
    }

    /**
     * #### Render
     * Délègue le dessin au composable structurel.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        PowerLoadDisplay(
            modifier = modifier,
            label = config.moduleLabel,
            state = state
        )
    }
}