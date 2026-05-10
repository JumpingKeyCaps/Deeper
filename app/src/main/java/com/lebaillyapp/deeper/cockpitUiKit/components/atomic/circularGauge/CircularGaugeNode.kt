package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * Nœud logique de la jauge circulaire.
 * Pilote l'affichage physique et simule l'extinction du rétro-éclairage.
 */
class CircularGaugeNode(
    override val id: String,
    config: CircularGaugeConfig,
    state: CircularGaugeState = CircularGaugeState(CircularGaugeData(config.minValue))
) : BaseComponent<CircularGaugeConfig, CircularGaugeState>(id, config, state) {

    /**
     * Met à jour la jauge.
     * Gère la chute de l'aiguille au repos si la machine est sur OFF.
     */
    fun updateGauge(newValue: Float, machineStatus: CircularMachineState) {
        // Détermination de la couleur (même si pas de LED, peut servir au futur pour les textes)
        val displayColor = calculateColor(newValue)

        // Si OFF, l'aiguille tombe au minimum, sinon elle suit newValue
        val finalValue = if (machineStatus == CircularMachineState.OFF) {
            config.minValue
        } else {
            newValue.coerceIn(config.minValue, config.maxValue)
        }

        state.update(
            CircularGaugeData(
                value = finalValue,
                machineState = machineStatus,
                currentColor = displayColor
            )
        )
    }

    /**
     * Logique de couleur basée sur la valeur physique.
     */
    private fun calculateColor(value: Float): Color {
        return config.primaryColorParam
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val currentData = state.value

        CircularGauge(
            modifier = modifier,
            gaugeSize = config.gaugeSize,
            currentValue = currentData.value,
            minValue = config.minValue,
            maxValue = config.maxValue,
            title = config.titleParam,
            titleFontFamily = config.titleFontFamily,
            majorTickInterval = config.majorTickInterval,
            minorTicksPerMajor = config.minorTicksPerMajor,
            cardBackgroundColor = config.cardBackgroundColor,
            scaleColor = config.scaleColor,
            firstTickColor = config.firstTickColor,
            lastTickColor = config.lastTickColor,
            needleColor = config.needleColor,
            valueTextColor = config.valueTextColor,
            titleColor = config.titleColor,
            valueTextSize = config.valueTextSize,
            titleTextSize = config.titleTextSize,
            labelTextSize = config.labelTextSize,
            majorTickStrokeWidth = config.majorTickStrokeWidth,
            minorTickStrokeWidth = config.minorTickStrokeWidth,
            needleStrokeWidth = config.needleStrokeWidth,
            animationDurationMillis = config.animationDurationMillis,
            // isOn pilote l'opacité globale des éléments dessinés dans le Canvas et des Textes
            isOn = currentData.machineState != CircularMachineState.OFF,
            titleBottomMarging = config.titleBottomMarging
        )
    }
}