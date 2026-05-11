package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.horizontalSegmentedBargraph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * ## HorizontalSegmentedBargraphNode
 *
 * Orchestre le rendu et l'animation du bargraph horizontal.
 * Gère la transition fluide entre les valeurs pour un effet analogique.
 */
class HorizontalSegmentedBargraphNode(
    override val id: String,
    config: HorizontalSegmentedBargraphConfig,
    state: HorizontalSegmentedBargraphState = HorizontalSegmentedBargraphState()
) : BaseComponent<HorizontalSegmentedBargraphConfig, HorizontalSegmentedBargraphState>(id, config, state) {

    // Helper pour l'animation de lissage (initialisé à la valeur actuelle du state)
    private val animatedValue = Animatable(state.value)

    /**
     * #### updateValue
     * Met à jour le niveau de manière progressive.
     *
     * @param targetLevel La valeur cible à atteindre (0..100).
     * @param scope Le scope coroutine (fourni par l'écran ou le manager).
     */
    fun updateValue(targetLevel: Float, scope: CoroutineScope) {
        scope.launch {
            animatedValue.animateTo(
                targetValue = targetLevel.coerceIn(0f, 100f),
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            ) {
                // On injecte la valeur animée dans le state à chaque frame
                state.updateLevel(this.value)
            }
        }
    }

    @Composable
    override fun Render(modifier: Modifier) {
        HorizontalSegmentedBargraph(
            modifier = modifier,
            currentLevel = state.value, // Reçoit la valeur lissée par l'animatable
            segments = config.segments,
            colorMode = config.colorMode,
            colorScale = config.colorScale,
            inactiveColor = config.inactiveColor,
            backgroundColor = config.backgroundColor,
            spacing = config.spacing,
            barCornerRadius = config.barCornerRadius,
            containerCornerRadius = config.containerCornerRadius,
            glowRadius = config.glowRadius
        )
    }
}