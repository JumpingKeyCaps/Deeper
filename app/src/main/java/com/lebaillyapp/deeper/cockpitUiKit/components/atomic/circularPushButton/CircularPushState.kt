package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton

import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentState

data class CircularPushData(val isChecked: Boolean = false)
class CircularPushState(initial: CircularPushData) : ComponentState<CircularPushData>(initial)