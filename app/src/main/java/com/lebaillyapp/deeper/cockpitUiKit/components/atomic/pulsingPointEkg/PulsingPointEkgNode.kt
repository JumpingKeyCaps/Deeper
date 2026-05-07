package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.pulsingPointEkg

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## PulsingPointEkgNode
 *
 * Node gérant le moniteur EKG.
 */
class PulsingPointEkgNode(
    override val id: String,
    config: PulsingPointEkgConfig,
    state: PulsingPointEkgState = PulsingPointEkgState()
) : BaseComponent<PulsingPointEkgConfig, PulsingPointEkgState>(id, config, state) {

    @Composable
    override fun Render(modifier: Modifier) {
        // Appel strict de ton composant original
        PulsingPointEkg(
            modifier = modifier,
            oxygenLevel = state.value,
            pointRadius = config.pointRadius,
            trailThickness = config.trailThickness,
            glowRadius = config.glowRadius,
            normalColor = config.normalColor,
            warningColor = config.warningColor,
            dangerColor = config.dangerColor,
            bpmState = state.bpmState
        )
    }

    fun updateValue(oxygenLevel: Int) {
        state.updateOxygen(oxygenLevel)
    }
}