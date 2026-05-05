package com.lebaillyapp.deeper.cockpitUiKit.core

import androidx.compose.ui.graphics.Color

/**
 * ## Classe de base pour la configuration structurelle d'un composant.
 *
 * Contient les paramètres qui ne changent pas pendant le cycle de vie du composant.
 *
 * @property title Titre ou label affiché sur le composant.
 * @property primaryColor Couleur dominante pour le rendu (aiguille, bordure, etc.).
 */
abstract class ComponentConfig(
    val title: String,
    val primaryColor: Color
)