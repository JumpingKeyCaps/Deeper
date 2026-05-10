package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.powerLoadDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * ## PowerLoadDisplay - Version Precision Layout
 */
@Composable
fun PowerLoadDisplay(
    modifier: Modifier = Modifier,
    label: String,
    state: PowerLoadState
) {
    ConstraintLayout(
        modifier = modifier
            .width(90.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(4.dp))
            .padding(bottom = 4.dp)
    ) {
        // Création des références pour chaque élément
        val (txtLabel, gauge, led, overload) = createRefs()

        // 1. LABEL (En haut, centré)
        Text(
            text = label.uppercase(),
            color = Color.Gray,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(txtLabel) {
                top.linkTo(overload.bottom, margin = -4.dp)
                start.linkTo(overload.start)
                end.linkTo(overload.end)
            }
        )

        // 2. GAUGE (Élément central, ancré sous le label)
        Box(
            modifier = Modifier
                .size(90.dp)
                .padding(4.dp)
                .constrainAs(gauge) {
                    top.linkTo(
                        overload.bottom,
                        margin = 12.dp
                    )
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            state.gaugeNode.Render(modifier = Modifier.fillMaxSize())
        }

        // 3. LED (Positionnée par-dessus la jauge ou juste en dessous du centre)
        Box(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(led) {
                    bottom.linkTo(gauge.bottom, margin = 8.dp)
                    start.linkTo(gauge.start)
                    end.linkTo(gauge.end)
                }
        ) {
            state.statusLedNode.Render(modifier = Modifier.fillMaxSize())
        }

        // 4. VOYANT OVERLOAD (En bas du module)
        Box(
            modifier = Modifier
                .width(65.dp)
                .height(28.dp)
                .constrainAs(overload) {
                    top.linkTo(parent.top, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            state.overloadVoyantNode.Render(modifier = Modifier.fillMaxSize())
        }
    }
}