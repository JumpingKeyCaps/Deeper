package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalGauge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * Nœud logique de la jauge.
 * Pilote l'affichage physique et le clignotement de la LED par l'intervalle.
 */
class HorizontalGaugeNode(
    override val id: String,
    config: HorizontalGaugeConfig,
    state: HorizontalGaugeState = HorizontalGaugeState(GaugeData(config.minValue))
) : BaseComponent<HorizontalGaugeConfig, HorizontalGaugeState>(id, config, state) {

    /**
     * Met à jour la jauge.
     * Gère la chute de l'aiguille si OFF.
     */
    fun updateGauge(newValue: Float, machineStatus: MachineState) {
        // On calcule la couleur sur la valeur réelle
        val displayColor = calculateColor(newValue)

        // Simulation : Si OFF, l'aiguille tombe au minimum, sinon elle suit newValue
        val finalValue = if (machineStatus == MachineState.OFF) {
            config.minValue
        } else {
            newValue.coerceIn(config.minValue, config.maxValue)
        }

        state.update(
            GaugeData(
                value = finalValue,
                machineState = machineStatus,
                currentColor = displayColor
            )
        )
    }

    private fun calculateColor(value: Float): Color {
        val green = Color(0xFF00E676)
        val orange = Color(0xFFFF9100)
        val red = Color(0xFFFF1744)

        return if (config.alertIsBelow) {
            when {
                value <= config.criticalThreshold -> red
                value <= config.warningThreshold -> orange
                else -> green
            }
        } else {
            when {
                value >= config.criticalThreshold -> red
                value >= config.warningThreshold -> orange
                else -> green
            }
        }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val currentData = state.value

        HorizontalGauge(
            modifier = modifier,
            currentValue = currentData.value,
            minValue = config.minValue,
            maxValue = config.maxValue,
            title = config.title,
            scaleColor = config.primaryColor,
            needleColor = config.needleColor,
            firstTickColor = config.firstTickColor,
            lastTickColor = config.lastTickColor,
            cardBackgroundColor = config.cardBackgroundColor,
            majorTickInterval = config.majorTickInterval,
            minorTicksPerMajor = config.minorTicksPerMajor,
            needleStrokeWidth = config.needleStrokeWidth,
            titleFontFamily = config.titleFontFamily,
            valueTextSize = config.valueTextSize,
            // --- LOGIQUE LED ---
            ledColor = currentData.currentColor,
            ledIsOn = currentData.machineState != MachineState.OFF,
            // Mode Blink : 500ms si RUNNING, sinon 0 (ce qui coupe le blink selon ta logique)
            ledBlinkInterval = if (currentData.machineState == MachineState.RUNNING) 500 else 0
        )
    }
}