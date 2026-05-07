package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.ballaster

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchData
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge.VerticalGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## BallasterNode
 *
 * Nœud composite pilotant le système de ballast.
 * Assure la synchronisation entre les commandes (Switches) et les retours (LEDs, Jauges).
 */
class BallasterNode(
    override val id: String,
    config: BallasterConfig,
    val onWaterToggle: () -> Unit,
    val onGasToggle: () -> Unit
) : BaseComponent<BallasterConfig, BallasterState>(
    id,
    config,
    BallasterState(
        waterLed = LedNode(
            id = "${id}_led_w",
            config = LedConfig("W_LED", Color.White, size = 10f, haloSpacer = 1, blinkInterval = 250)
        ),
        gasLed = LedNode(
            id = "${id}_led_g",
            config = LedConfig("G_LED", Color.White, size = 10f, haloSpacer = 1, blinkInterval = 250)
        ),
        waterGauge = VerticalGaugeNode("${id}_gauge_w", config.gaugeConfig),
        gasGauge = VerticalGaugeNode("${id}_gauge_g", config.gaugeConfig),
        waterSwitch = RockerSwitchNode(
            id = "${id}_sw_w",
            config = RockerSwitchConfig(
                titleParam = "SW_W",
                iconSource = { painterResource(config.waterIcon) },
                width = 35.dp,
                height = 64.dp,
                animationDuration = 500
            )
        ),
        gasSwitch = RockerSwitchNode(
            id = "${id}_sw_g",
            config = RockerSwitchConfig(
                titleParam = "SW_G",
                iconSource = { painterResource(config.gasIcon) },
                width = 35.dp,
                height = 64.dp,
                animationDuration = 500
            )
        )
    )
) {

    init {
        // Connexion vitale : on écoute les changements d'états internes des switches
        state.waterSwitch.onStateChanged = { handleWaterToggle() }
        state.gasSwitch.onStateChanged = { handleGasToggle() }
    }

    /**
     * Gère l'activation du circuit d'eau.
     * En mode actif, coupe automatiquement le circuit de gaz (Interlock).
     */
    private fun handleWaterToggle() {
        if (!state.isWaterEnabled) return

        // On récupère l'état depuis le switch qui vient de basculer
        val isActive = state.waterSwitch.state.value.isChecked

        // Mise à jour de la LED d'état
        state.waterLed.state.update(isActive)

        // Logique d'exclusion mutuelle (Interlock)
        if (isActive) {
            state.gasLed.state.update(false)
            state.gasSwitch.toggle(false) // Force l'extinction visuelle/logique du gaz
        }

        onWaterToggle()
    }

    /**
     * Gère l'activation du circuit de gaz.
     * En mode actif, coupe automatiquement le circuit d'eau (Interlock).
     */
    private fun handleGasToggle() {
        if (!state.isGasEnabled) return

        val isActive = state.gasSwitch.state.value.isChecked

        // Mise à jour de la LED d'état
        state.gasLed.state.update(isActive)

        // Logique d'exclusion mutuelle (Interlock)
        if (isActive) {
            state.waterLed.state.update(false)
            state.waterSwitch.toggle(false) // Force l'extinction visuelle/logique de l'eau
        }

        onGasToggle()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        BallasterDisplay(
            modifier = modifier,
            state = state,
            config = config,

        )
    }
}