package com.lebaillyapp.deeper.cockpitUiKit.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * ## Interface de base pour tout élément graphique du cockpit.
 *
 * Chaque composant "Plug & Play" doit implémenter cette interface pour être
 * rendu de manière dynamique dans un layout.
 */
interface ComponentNode {
    /**
     * #### Identifiant unique du composant
     * (ex: "ALT_01", "ENGINE_TEMP").
     * Utilisé pour l'injection de données et le tracking de la composition.
     */
    val id: String

    /**
     * #### Fonction de rendu principale du composant.
     * @param modifier Le [Modifier] passé par le layout parent (souvent pour le placement/taille).
     */
    @Composable
    fun Render(modifier: Modifier)
}