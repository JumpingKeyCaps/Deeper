package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentNode
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## Multi7SegNode
 *
 * Node Composite gérant un groupe d'afficheurs 7 segments.
 * Il génère automatiquement ses enfants atomiques en fonction de la configuration.
 */
class Multi7SegNode(
    override val id: String,
    config: Multi7SegConfig,
    state: Multi7SegState? = null
) : BaseComponent<Multi7SegConfig, Multi7SegState>(
    id,
    config,
    state ?: Multi7SegState(childNodes = List(config.numDigits) { index ->
        // On crée les enfants avec une ID dérivée et la config de base
        SevenSegmentNode(
            id = "${id}_seg_$index",
            config = config.baseSegmentConfig
        )
    })
) {

    /**
     * #### Rendu du Composite
     * Délègue le placement et le rendu visuel au composable structurel.
     */
    @Composable
    override fun Render(modifier: Modifier) {
        // On utilise le composable Multi7SegDisplay qu'on a "node-ifié" précédemment
        Multi7SegDisplay(
            modifier = modifier,
            nodes = state.childNodes,
            spacing = config.spacing,
            extraSpacingStep = config.extraSpacingStep,
            extraSpacing = config.extraSpacing,
            activateReflect = config.activateReflect,
            reflectConfig = config.reflectConfig
        )
    }

    /**
     * #### Pilotage du groupe
     * Définit la chaîne de caractères à afficher sur l'ensemble des segments.
     *
     * @param value Le texte ou les chiffres à afficher (ex: "12.04").
     */
    fun setValue(value: String) {
        state.updateDisplay(
            newValue = value,
            reversed = config.reversedOverride,
            showZero = config.showZeroWhenEmpty
        )
    }
}