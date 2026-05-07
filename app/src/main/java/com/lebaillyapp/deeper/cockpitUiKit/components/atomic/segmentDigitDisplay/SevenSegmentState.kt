package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## SevenSegmentState
 *
 * Gère l'état de l'affichage pour un composant à 7 segments.
 * Ce state est exclusif : la mise à jour d'un type de donnée (Digit, Char ou Manual)
 * réinitialise automatiquement les autres pour maintenir la cohérence visuelle.
 *
 * @param initialDigit Chiffre initial (0-9). Correspond à la `value` principale du [ComponentState].
 * @param initialChar Caractère initial (A-U, etc. selon compatibilité).
 * @param initialManual État manuel initial pour chaque segment (liste de 7 booléens).
 */
class SevenSegmentState(
    initialDigit: Int? = null,
    initialChar: Char? = null,
    initialManual: List<Boolean>? = null
) : ComponentState<Int?>(initialDigit) {

    /**
     * Valeur de type caractère actuellement stockée.
     * Prioritaire sur la valeur numérique si non nulle.
     */
    var charValue by mutableStateOf(initialChar)

    /**
     * Configuration manuelle des segments (A, B, C, D, E, F, G).
     * Si définie, elle outrepasse les logiques de mapping digit/char.
     */
    var manualSegments by mutableStateOf(initialManual)

    /**
     * Définit un chiffre (0-9) à afficher.
     * Réinitialise [charValue] et [manualSegments] à null.
     *
     * @param digit Le chiffre cible ou null pour éteindre l'afficheur.
     */
    fun updateDigit(digit: Int?) {
        update(digit)
        charValue = null
        manualSegments = null
    }

    /**
     * Définit un caractère à afficher.
     * Réinitialise la valeur numérique principale et [manualSegments] à null.
     *
     * @param char Le caractère cible (ex: 'A', 'E', 'P') ou null.
     */
    fun updateChar(char: Char?) {
        charValue = char
        update(null)
        manualSegments = null
    }
}