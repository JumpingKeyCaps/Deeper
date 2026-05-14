package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.oxygenTankSlot


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lebaillyapp.deeper.R

/**
 * ## OxygenTankDisplay
 */
@Composable
fun OxygenTankDisplay(
    modifier: Modifier = Modifier,
    label: String,
    state: OxygenTankState,
    isInverted: Boolean = false
) {
    ConstraintLayout(
        modifier = modifier.height(90.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(4.dp))
    ) {
        val (txtLabel, horizGauge, horizBarGraph, rockerButton, verticalSeparator) = createRefs()

        // 1. La Jauge
        Box(
            modifier = Modifier
                .size(100.dp, 65.dp)
                .constrainAs(horizGauge) {
                    top.linkTo(parent.top)
                    if (isInverted) end.linkTo(parent.end) else start.linkTo(parent.start)
                }
        ) {
            state.gaugeNode.Render(modifier = Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .size(100.dp, 12.dp)
                .padding(horizontal = 10.dp, vertical = 1.dp)
                .constrainAs(horizBarGraph) {
                    top.linkTo(horizGauge.bottom)
                    start.linkTo(horizGauge.start)
                    end.linkTo(horizGauge.end)
                }
        ) {
            state.barGraphNode.Render(modifier = Modifier)
        }

        // 2. Le Séparateur
        Box(modifier = Modifier
            .width(0.5.dp)
            .height(90.dp)
            .background(Color(0xFF333333))
            .constrainAs(verticalSeparator) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                if (isInverted) end.linkTo(horizGauge.start) else start.linkTo(horizGauge.end)
            }
        )

        // 3. Le bloc Contrôle (Label + Rocker)
        // On lie directement aux ancres selon l'état isInverted
        Box(
            modifier = Modifier
                .size(45.dp, 20.dp)
                .padding(top = 3.dp)
                .constrainAs(txtLabel) {
                    top.linkTo(horizGauge.top)
                    if (isInverted) {
                        start.linkTo(parent.start)
                        end.linkTo(horizGauge.start)
                    } else {
                        start.linkTo(horizGauge.end)
                        end.linkTo(parent.end)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.micro_regular))
            )
        }

        Box(
            modifier = Modifier
                .size(25.dp, 55.dp)
                .constrainAs(rockerButton) {
                    top.linkTo(txtLabel.bottom)
                    bottom.linkTo(parent.bottom)
                    if (isInverted) {
                        start.linkTo(parent.start)
                        end.linkTo(horizGauge.start)
                    } else {
                        start.linkTo(horizGauge.end)
                        end.linkTo(parent.end)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            state.rockerButtonNode.Render(modifier = Modifier)
        }
    }
}