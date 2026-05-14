package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.oxygenTankSlot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge.HorizontalGaugeNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph.HorizontalSegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## OxygenTankNode
 *
 * Node composite représentant un réservoir "tank" d'oxygène complet.
 */
class OxygenTankNode(
    override val id: String,
    config: OxygenTankConfig,
    val onOxygenInjected: (Float) -> Unit = {} // Litres envoyés au Main par seconde
) : BaseComponent<OxygenTankConfig, OxygenTankState>(
    id,
    config,
    OxygenTankState(
        maxCapacity = config.maxCapacityLiters,
        flowRate = config.flowRatePerSecond,
        initialLoadLiters = config.maxCapacityLiters, // Full par défaut
        gaugeNode = HorizontalGaugeNode("${id}_seg", config.hGaugeConfig),
        barGraphNode = HorizontalSegmentedBargraphNode("${id}_bar", config.barGraphConfig),
        rockerButtonNode = RockerSwitchNode("${id}_rock", config.rockerButtonConfig),
        onTransferUpdate = onOxygenInjected,
        scope = config.scope
    )
) {

    init {
        state.syncRockerAction()
    }

    /**
     * Pilote l'alimentation électrique du module (via un bouton de panel externe).
     */
    fun setPowerStatus(enabled: Boolean) {
        state.setPower(enabled)
    }

    /**
     * Remplissage forcé du tank (ex: via le moteur de jeu en début de partie).
     */
    fun fillTank(liters: Float) {
        state.updateTankData(liters)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        OxygenTankDisplay(
            modifier = modifier,
            label = config.tankLabel,
            state = state,
            isInverted = config.isInverted
        )
    }

    /**
     * Indique si le tank est actuellement en train de déverser dans le réseau.
     */
    fun isTransferring(): Boolean = state.isConnected && state.isPoweredOn
}