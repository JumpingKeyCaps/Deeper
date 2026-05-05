package com.lebaillyapp.deeper.cockpitUiKit.core

/**
 * ## Implémentation de base d'un [ComponentNode] qui lie une configuration et un état.
 *
 * @param C Le type de configuration (héritant de [ComponentConfig]).
 * @param S Le type d'état (héritant de [ComponentState]).
 * @property id L'identifiant unique du composant.
 * @property config Les paramètres de design statiques.
 * @property state Le gestionnaire de données dynamiques.
 */
abstract class BaseComponent<C : ComponentConfig, S : ComponentState<*>>(
    override val id: String,
    val config: C,
    val state: S
) : ComponentNode