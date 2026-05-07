package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## Composant Node pour la Jauge Verticale.
 *
 * Assure la liaison entre le moteur de données et l'UI du composable [VerticalGauge]
 * en utilisant l'intégralité des paramètres définis dans la configuration.
 *
 * @param id Identifiant unique du composant.
 * @param config Paramètres de structure complets.
 * @param state État dynamique contenant la valeur actuelle.
 */
class VerticalGaugeNode(
    override val id: String,
    config: VerticalGaugeConfig,
    state: VerticalGaugeState = VerticalGaugeState()
) : BaseComponent<VerticalGaugeConfig, VerticalGaugeState>(id, config, state) {

    /**
     * #### Rendu de la jauge.
     * Injection de tous les paramètres de la config et du state vers le composable.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        VerticalGauge(
            modifier = modifier,
            currentValue = state.value,
            maxCapacity = config.maxCapacity,
            stepValue = config.stepValue,
            majorGraduationStep = config.majorGraduationStep,
            needleColor = config.needleColor,
            graduationColor = config.graduationColor,
            backgroundColor = config.backgroundColor,
            textColor = config.textColor,
            cornerRadius = config.cornerRadius,
            paddingTop = config.paddingTop,
            paddingBottom = config.paddingBottom,
            zeroGraduationColor = config.zeroGraduationColor
        )
    }

    /**
     * #### Met à jour la valeur de la jauge.
     * @param newValue La nouvelle donnée brute.
     */
    fun updateValue(newValue: Float) {
        state.update(newValue)
    }
}