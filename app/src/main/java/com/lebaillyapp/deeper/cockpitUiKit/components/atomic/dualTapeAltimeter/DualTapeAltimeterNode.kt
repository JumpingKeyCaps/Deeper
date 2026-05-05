package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.dualTapeAltimeter

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lebaillyapp.deeper.cockpitUiKit.core.BaseComponent

/**
 * ## Nœud logique de l'altimètre.
 *
 * Gère la profondeur et maintient le record comme une cible statique pour
 * permettre l'effet visuel de franchissement.
 */
class DualTapeAltimeterNode(
    override val id: String,
    config: DualTapeAltimeterConfig,
    state: DualTapeAltimeterState = DualTapeAltimeterState(
        DualTapeAltimeterData(recordDepth = config.recordDepthParam)
    )
) : BaseComponent<DualTapeAltimeterConfig, DualTapeAltimeterState>(id, config, state) {

    /**
     * Met à jour uniquement l'altitude actuelle.
     * Le record (recordDepth) reste fixe pour que le composant puisse calculer
     * l'écart visuel et changer la couleur des pointeurs.
     */
    fun updateAltitude(value: Float) {
        state.update(state.value.copy(currentAltitude = value))
    }

    /**
     * Permet de définir un nouveau record à battre.
     * À appeler au début d'une session ou pour valider manuellement un palier.
     */
    fun updateRecord(value: Float) {
        state.update(state.value.copy(recordDepth = value))
    }

    /**
     * Optionnel : valide la profondeur actuelle comme étant le nouveau record définitif.
     */
    fun commitCurrentAsRecord() {
        val current = state.value
        state.update(current.copy(recordDepth = current.currentAltitude))
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val currentData = state.value

        DualTapeAltimeter(
            altitude = currentData.currentAltitude,
            recordDepth = currentData.recordDepth,
            modifier = modifier,
            minAltitude = config.minAltitude,
            maxAltitude = config.maxAltitude,
            majorTickEvery = config.majorTickEvery,
            minorTickEvery = config.minorTickEvery,
            deepCountSize = config.centerFontSize,
            customFont = config.customFont
        )
    }
}