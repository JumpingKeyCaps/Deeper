package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## BatterySlotNode
 *
 * Node composite représentant une unité de batterie complète.
 * Il orchestre un afficheur 7-segments, un bargraph et un switch de puissance.
 *
 * @param id Identifiant unique du node.
 * @param config Configuration agrégée (labels, styles des sous-nodes).
 * @param onPowerToggled Callback déclenché lors de la connexion/déconnexion au bus.
 */
class BatterySlotNode(
    override val id: String,
    config: BatterySlotConfig,
    val onPowerToggled: (Boolean) -> Unit = {}
) : BaseComponent<BatterySlotConfig, BatterySlotState>(
    id,
    config,
    // On initialise le State en créant les Nodes enfants à la volée
    BatterySlotState(
        segNode = Multi7SegNode("${id}_seg", config.segConfig),
        barNode = SegmentedBargraphNode("${id}_bar", config.barConfig),
        rockNode = RockerSwitchNode("${id}_rock", config.rockerConfig),
        onPowerToggled = onPowerToggled
    )
) {

    init {
        // Au démarrage, on lie l'action physique du rocker au callback du State
        state.syncRockerAction()
    }

    /**
     * #### Render
     * Délègue le dessin au composable structurel en lui passant le State interne.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        BatterySlotDisplay(
            modifier = modifier,
            label = config.slotLabel,
            state = state
        )
    }

    /**
     * #### setLevel
     * Méthode de pilotage principale pour mettre à jour l'affichage de la batterie.
     *
     * @param level Valeur de charge (0.0 à 100.0).
     */
    fun setLevel(level: Float) {
        state.updateBatteryData(level)
    }

    /**
     * #### isOnline
     * Indique si la batterie est actuellement connectée au bus de puissance.
     */
    fun isOnline(): Boolean = state.isConnected
}