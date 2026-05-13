package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## TrenchVisualizerNode
 * Orchestre les animations. Réagit maintenant au [scanTrigger] pour le "One-Shot Scan".
 */
class TrenchVisualizerNode(
    override val id: String,
    config: TrenchVisualizerConfig,
    state: TrenchVisualizerState = TrenchVisualizerState(TrenchVisualizerData())
) : BaseComponent<TrenchVisualizerConfig, TrenchVisualizerState>(id, config, state) {

    fun updateDepth(intensity: Float) = state.updateTargetIntensity(intensity)

    fun setStatus(status: TrenchMachineState) = state.setMachineState(status)

    /**
     * Déclenche manuellement la séquence visuelle du scan.
     */
    fun triggerScan() {
        state.triggerManualScan()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val currentData = state.value

        val animDepth = remember { Animatable(currentData.animatedDepth) }
        val animPower = remember { Animatable(currentData.powerFactor) }

        // Gestion ON/OFF (ScaleY / Alpha)
        LaunchedEffect(currentData.machineState) {
            val targetPower = if (state.isPoweredOn) 1f else 0f
            animPower.animateTo(
                targetValue = targetPower,
                animationSpec = if (targetPower > 0) {
                    spring(dampingRatio = 0.2f, stiffness = Spring.StiffnessLow)
                } else {
                    tween(500, easing = FastOutSlowInEasing)
                }
            )
        }

        // Gestion du Scan : Réagit au changement de valeur OU au trigger manuel
        LaunchedEffect(currentData.depthIntensity, currentData.scanTrigger) {
            if (state.isPoweredOn) {
                // Séquence de scan : Reset puis déploiement
                animDepth.animateTo(0f, tween(
                    config.scanSpeed/3,
                    easing = LinearOutSlowInEasing))
                animDepth.animateTo(
                    targetValue = currentData.depthIntensity,
                    animationSpec = tween(
                        config.scanSpeed,
                        easing = LinearOutSlowInEasing)
                )
            }
        }

        // Sync des valeurs animées vers le State
        LaunchedEffect(animDepth.value, animPower.value) {
            state.updateAnimationValues(
                animDepth = animDepth.value,
                power = animPower.value
            )
        }

        TrenchVisualizer(
            modifier = modifier,
            depthIntensity = currentData.animatedDepth,
            powerFactor = currentData.powerFactor,
            config = config
        )
    }
}