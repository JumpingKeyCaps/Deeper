package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## SevenSegmentNode
 *
 * Composant de contrôle pour l'afficheur à 7 segments.
 * Ce Node fait le pont entre la configuration visuelle statique ([SevenSegmentConfig])
 * et l'état dynamique de l'affichage ([SevenSegmentState]).
 *
 * Il permet une manipulation simplifiée de l'affichage numérique ou alphanumérique
 * via des méthodes dédiées, tout en encapsulant la logique de rendu Compose.
 */
class SevenSegmentNode(
    override val id: String,
    config: SevenSegmentConfig,
    state: SevenSegmentState = SevenSegmentState()
) : BaseComponent<SevenSegmentConfig, SevenSegmentState>(id, config, state) {

    /**
     * #### Rendu du composant
     *
     * Injecte l'état réactif du [state] et les paramètres de style de la [config]
     * dans l'implémentation graphique [SevenSegmentDigitDisplayExtended].
     *
     * @param modifier Le modificateur de mise en page pour ajuster la taille ou la position.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        SevenSegmentDigitDisplayExtended(
            modifier = modifier,
            digit = state.value,
            char = state.charValue,
            manualSegments = state.manualSegments,
            segmentLength = config.segmentLength,
            segmentHorizontalLength = config.segmentHorizontalLength,
            segmentThickness = config.segmentThickness,
            bevel = config.bevel,
            onColor = config.onColor,
            offColor = config.offColor,
            alpha = config.alpha,
            glowRadius = config.glowRadius,
            flickerAmplitude = config.flickerAmplitude,
            flickerFrequency = config.flickerFrequency,
            idleMode = config.idleMode,
            idleSpeed = config.idleSpeed
        )
    }

    /**
     * #### Pilotage numérique
     * Met à jour l'afficheur pour présenter un chiffre unique.
     *
     * @param digit Le chiffre (0 à 9) à afficher.
     */
    fun setDigit(digit: Int) = state.updateDigit(digit)

    /**
     * #### Pilotage alphanumérique
     * Met à jour l'afficheur pour présenter un caractère spécifique.
     *
     * @param char Le caractère à afficher (ex: 'A', 'E', 'P', 'H', 'L', 'U').
     */
    fun setChar(char: Char) = state.updateChar(char)
}