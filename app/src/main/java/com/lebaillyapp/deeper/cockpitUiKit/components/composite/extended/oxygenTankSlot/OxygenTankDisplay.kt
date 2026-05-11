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
    state: OxygenTankState
) {
    ConstraintLayout(
        modifier = modifier.height(90.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(4.dp))
    ) {
        // Création des références pour chaque élément
        val (txtLabel, horizGauge, horizBarGraph, rockerButton, verticalSeparator) = createRefs()

        Box(
            modifier = Modifier
                .size(100.dp, 65.dp)
                .padding(0.dp)
                .constrainAs(horizGauge) {
                    top.linkTo(
                        parent.top,
                        margin = 0.dp
                    )
                    start.linkTo(parent.start)
                }
        ) {
            state.gaugeNode.Render(modifier = Modifier.fillMaxSize())
        }


        Box(modifier =
            Modifier.width(0.5.dp)
                .height(90.dp)
                .background(Color(0xFF333333))
            .constrainAs(verticalSeparator) {
                top.linkTo(parent.top)
                start.linkTo(horizGauge.end)
                bottom.linkTo(parent.bottom)
            },

       )


        Box(
            modifier = Modifier
                .size(45.dp,20.dp)
                .padding(top = 3.dp)
                .constrainAs(txtLabel) {
                    top.linkTo(
                        horizGauge.top,
                        margin = 0.dp
                    )
                    start.linkTo(horizGauge.end)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.micro_regular)),
                modifier = Modifier.padding(bottom = 0.dp)
            )
        }


        Box(
            modifier = Modifier
                .size(25.dp,55.dp)
                .padding(top = 0.dp)
                .constrainAs(rockerButton) {
                    top.linkTo(
                        txtLabel.bottom)
                    start.linkTo(horizGauge.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            contentAlignment = Alignment.Center

        ) {
            state.rockerButtonNode.Render(modifier = Modifier)
        }


        Box(
            modifier = Modifier
                .size(100.dp,12.dp)
                .padding(start = 10.dp, end = 10.dp, top = 1.dp, bottom = 1.dp)
                .constrainAs(horizBarGraph) {
                    top.linkTo(horizGauge.bottom)
                    start.linkTo(horizGauge.start)
                    end.linkTo(horizGauge.end)
                }
        ) {
            state.barGraphNode.Render(modifier = Modifier)
        }
    }
}