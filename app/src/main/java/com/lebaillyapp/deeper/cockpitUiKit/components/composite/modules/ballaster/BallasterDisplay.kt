package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.ballaster

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.led.LedNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchNode
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.verticalGauge.VerticalGaugeNode

@Composable
fun BallasterDisplay(
    modifier: Modifier = Modifier,
    state: BallasterState,
    config: BallasterConfig
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = config.containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(5.dp), contentAlignment = Alignment.Center) {
            Row {
                // --- COLONNE EAU ---
                BallasterColumn(
                    ledNode = state.waterLed,
                    gaugeNode = state.waterGauge,
                    switchNode = state.waterSwitch,
                    label = "% WATER",
                    isEnabled = state.isWaterEnabled
                )

                Spacer(modifier = Modifier.width(15.dp))

                // --- COLONNE GAZ ---
                BallasterColumn(
                    ledNode = state.gasLed,
                    gaugeNode = state.gasGauge,
                    switchNode = state.gasSwitch,
                    label = "% GAZ",
                    isEnabled = state.isGasEnabled
                )
            }
        }
    }
}

@Composable
private fun BallasterColumn(
    ledNode: LedNode,
    gaugeNode: VerticalGaugeNode,
    switchNode: RockerSwitchNode,
    label: String,
    isEnabled: Boolean
) {
    val contentAlpha = if (isEnabled) 1f else 0.4f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.graphicsLayer(alpha = contentAlpha)
    ) {
        // LED
        ledNode.Render(Modifier.padding(top = 3.dp))

        Spacer(modifier = Modifier.height(8.dp))

        // JAUGE
        gaugeNode.Render(Modifier.width(45.dp).height(120.dp))

        Spacer(modifier = Modifier.height(3.dp))

        // LABEL
        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.micro_regular)),
            style = TextStyle(textAlign = TextAlign.Center),
            color = Color(0xFF3F3D3D),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(3.dp))

        // SWITCH : On laisse le Node gérer son propre rendu et ses interactions
        // Plus besoin de passer onToggle ici, le Node est déjà branché au State
        switchNode.Render(
            modifier = Modifier.size(width = 35.dp, height = 65.dp)
        )
    }
}