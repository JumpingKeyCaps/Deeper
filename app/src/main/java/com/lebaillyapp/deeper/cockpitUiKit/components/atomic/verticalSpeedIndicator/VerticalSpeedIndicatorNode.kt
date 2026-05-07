package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalSpeedIndicator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## VerticalSpeedIndicatorNode
 *
 * Le cerveau du variomètre. Il lie les paramètres de ton composant original
 * (via la Config) à la donnée dynamique (via le State).
 */
class VerticalSpeedIndicatorNode(
    override val id: String,
    config: VerticalSpeedIndicatorConfig,
    state: VerticalSpeedIndicatorState = VerticalSpeedIndicatorState()
) : BaseComponent<VerticalSpeedIndicatorConfig, VerticalSpeedIndicatorState>(id, config, state) {

    /**
     * #### Rendu du composant
     * Injection de tous les paramètres de la config dans ton composant VerticalSpeedIndicator.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        VerticalSpeedIndicator(
            modifier = modifier,
            verticalSpeed = state.value,
            maxSpeed = config.maxSpeed,
            majorTickInterval = config.majorTickInterval,
            minorTicksPerMajor = config.minorTicksPerMajor,
            cardBackgroundColor = config.cardBackgroundColor,
            indicatorColor = config.indicatorColor,
            scaleColor = config.scaleColor,
            zeroLineColor = config.zeroLineColor,
            tickWidthMajor = config.tickWidthMajor,
            tickWidthMinor = config.tickWidthMinor,
            labelTextSize = config.labelTextSize,
            cardElevation = config.cardElevation,
            cardCornerRadius = config.cardCornerRadius,
            internalPadding = config.internalPadding,
            animationDurationMillis = config.animationDurationMillis
        )
    }

    /**
     * #### Mise à jour de la vitesse
     * Permet de piloter l'aiguille de l'instrument depuis les systèmes de navigation.
     */
    fun updateValue(speed: Float) {
        state.updateSpeed(speed)
    }
}