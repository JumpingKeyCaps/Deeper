package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentNode
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## Multi7SegState
 *
 * Gère l'état d'un groupe d'afficheurs 7 segments.
 *
 * Ce State orchestre une liste de [SevenSegmentNode] et distribue les caractères
 * d'une chaîne de texte entrante vers chaque digit individuel.
 */
class Multi7SegState(
    initialValue: String = "",
    val childNodes: List<SevenSegmentNode> = emptyList()
) : ComponentState<String>(initialValue) {

    /**
     * Met à jour la valeur globale de l'afficheur et distribue les caractères
     * aux Nodes enfants en respectant les règles de priorité et de placement.
     *
     * @param newValue La nouvelle chaîne de caractères à afficher.
     * @param reversed Si true, l'affichage se remplit par la droite (mode RTL).
     * @param showZero Si true, les digits vides affichent un '0'.
     */
    fun updateDisplay(
        newValue: String,
        reversed: Boolean = false,
        showZero: Boolean = false
    ) {
        update(newValue) // Mise à jour de la valeur principale dans le BaseComponent

        childNodes.forEachIndexed { index, node ->
            // 1. Extraction du caractère correspondant à ce Node précis
            val charForNode = when {
                reversed -> {
                    // Calcul de l'index depuis la fin de la chaîne
                    val charIndex = newValue.length - childNodes.size + index
                    newValue.getOrNull(charIndex)
                }
                else -> newValue.getOrNull(index)
            }

            // 2. Application de la logique d'affichage
            when {
                charForNode != null -> {
                    val digit = charForNode.digitToIntOrNull()
                    if (digit != null) {
                        node.setDigit(digit)
                    } else {
                        node.setChar(charForNode.uppercaseChar())
                    }
                }
                showZero -> {
                    node.setDigit(0)
                }
                else -> {
                    // On éteint le segment (updateDigit(null) éteint tout via ton State atomique)
                    node.state.updateDigit(null)
                }
            }
        }
    }
}