package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState


/**
 * État dynamique d'une LED (Allumée ou Éteinte).
 */
class LedState(initialValue: Boolean = false) : ComponentState<Boolean>(initialValue)