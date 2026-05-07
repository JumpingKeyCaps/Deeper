package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## SegmentedBargraphNode
 *
 * Le "cerveau" du bargraph. Il encapsule la logique métier, l'état
 * et la configuration pour piloter le rendu visuel.
 */
class SegmentedBargraphNode(
    override val id: String,
    config: SegmentedBargraphConfig,
    state: SegmentedBargraphState = SegmentedBargraphState()
) : BaseComponent<SegmentedBargraphConfig, SegmentedBargraphState>(id, config, state) {

    /**
     * #### Rendu du composant
     * Lie les propriétés de la config et la valeur du state au composant UI.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        SegmentedBargraph(
            modifier = modifier,
            currentLevel = state.value,
            segments = config.segments,
            colorMode = config.colorMode,
            colorScale = config.colorScale,
            inactiveColor = config.inactiveColor,
            backgroundColor = config.backgroundColor,
            spacing = config.spacing,
            barCornerRadius = config.barCornerRadius,
            containerCornerRadius = config.containerCornerRadius,
            glowRadius = config.glowRadius
        )
    }

    /**
     * #### Mise à jour de la valeur
     * Permet aux systèmes externes (Capteurs, Simulateur) d'envoyer
     * une nouvelle donnée au bargraph.
     */
    fun updateValue(level: Float) {
        state.updateLevel(level)
    }
}