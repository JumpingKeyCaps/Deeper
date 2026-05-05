package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

/**
 * ## État dynamique du bouton.
 *
 * Gère la donnée "vivante" (allumé ou éteint) reçue du système.
 *
 * @param initialValue État de départ du bouton (par défaut false/éteint).
 */
class GlassButtonState(initialValue: Boolean = false) : ComponentState<Boolean>(initialValue)