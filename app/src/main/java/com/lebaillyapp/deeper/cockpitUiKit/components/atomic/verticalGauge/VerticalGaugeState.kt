package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## Gestionnaire d'état pour la jauge verticale.
 *
 * @param initialValue La valeur initiale de la jauge (Float).
 */
class VerticalGaugeState(initialValue: Float = 0f) : ComponentState<Float>(initialValue)