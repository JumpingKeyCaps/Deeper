package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## Nœud logique du GlassButton.
 * Fait le lien entre la configuration statique, l'état dynamique et la fonction de rendu Compose.
 *
 * @property id Identifiant unique du composant dans le cockpit.
 * @property config Les paramètres visuels fixes du bouton.
 * @property state L'état On/Off actuel.
 * @property onClick Action déclenchée lors d'une interaction utilisateur avec le bouton.
 */
class GlassButtonNode(
    override val id: String,
    config: GlassButtonConfig,
    state: GlassButtonState = GlassButtonState(),
    val onClick: () -> Unit = {}
) : BaseComponent<GlassButtonConfig, GlassButtonState>(id, config, state) {

    @Composable
    override fun Render(modifier: Modifier) {
        GlassButton(
            modifier = modifier,
            text = config.title,          // Titre de la config
            isOn = state.value,           // État dynamique
            color = config.primaryColor,  // Couleur de la config
            textColor = config.textColor,
            width = config.width,
            height = config.height,
            cornerRadius = config.cornerRadius,
            textSize = config.textSize,
            glowRadius = config.glowRadius,
            onClick = onClick
        )
    }
}