package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.trenchSonar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton.CircularPushNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchMachineState
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchVisualizerData
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchVisualizerNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer.TrenchVisualizerState
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## TrenchSonarModuleNode
 *
 * Orchestrateur du module Sonar.
 * En mode Auto-Ping, ce Node reçoit les ordres du moteur de jeu et les délègue
 * au State qui gère l'alimentation et les cycles de scan.
 */
class TrenchSonarModuleNode(
    override val id: String,
    config: TrenchSonarModuleConfig
) : BaseComponent<TrenchSonarModuleConfig, TrenchSonarModuleState>(
    id,
    config,
    TrenchSonarModuleState(
        config = config,
        visualizerNode = TrenchVisualizerNode(
            id = "${id}_visualizer",
            config = config.visualizerConfig,
            state = TrenchVisualizerState(
                TrenchVisualizerData(machineState = TrenchMachineState.OFF)
            )
        ),
        barGraphNode = SegmentedBargraphNode(
            id = "${id}_charge_bar",
            config = config.barGraphConfig,
            state = SegmentedBargraphState(0f) // Start empty (OFF)
        ),
        depthDisplayNode = Multi7SegNode(
            id = "${id}_depth_seg",
            config = config.depthMultiSegConfig
        ),
        pingButtonNode = CircularPushNode(
            id = "${id}_power_btn",
            config = config.powerButtonConfig
        )
    )
) {

    init {
        // Au démarrage, le State s'occupe déjà de shutdownSystems() via son init.
        // Le bouton Rocker pilotera le boot/shutdown via son onStateChanged interne au State.
    }


    /**
     * #### Commande de Ping
     * En mode Auto-Ping, cette méthode est appelée par la loop du screen
     * dès que le module est ON et non-rechargeant.
     */
    fun ping(subDepth: Float, floorDepth: Float) {
        state.triggerPing(subDepth, floorDepth)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        TrenchSonarModuleDisplay(
            modifier = modifier,
            label = config.moduleLabel,
            state = state
        )
    }
}