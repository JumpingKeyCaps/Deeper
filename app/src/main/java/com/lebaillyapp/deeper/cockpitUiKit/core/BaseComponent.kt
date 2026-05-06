package com.lebaillyapp.deeper.cockpitUiKit.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

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
) : ComponentNode {

    // Le moteur pour les animations et timers du composant
    protected val componentScope: CoroutineScope = MainScope()

    /**
     * À appeler lors de la destruction du composant pour stopper les coroutines en cours.
     */
    open fun dispose() {
        componentScope.cancel()
    }
}