package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.trenchSonar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.lebaillyapp.deeper.R

/**
 * ## TrenchSonarModuleDisplay
 * Layout optimisé : Label + Visualizer en haut,
 * puis une Row contenant [Bargraph | Afficheurs | Bouton Power].
 */
@Composable
fun TrenchSonarModuleDisplay(
    modifier: Modifier = Modifier,
    label: String,
    state: TrenchSonarModuleState
) {
    ConstraintLayout(
        modifier = modifier
            .width(90.dp)

            .background(Color(0xFF131313), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF282828), RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        val (visualizer, barGraph, pingButton) = createRefs()
        val (txtFloor, depthDisplay) = createRefs() // rowMaxRange est supprimé des refs principales

        // 1. Visualiseur avec Overlays (Top & Bottom)
        Box(
            modifier = Modifier
                .size(92.dp)
                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
                .constrainAs(visualizer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.Center
        ) {
            state.visualizerNode.Render(modifier = Modifier.fillMaxSize())

            // Overlay Titre (Top)
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.micro_regular)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 2.dp)
            )

            // Overlay Max Range (Bottom)
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MAX RANGE ",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 7.sp,
                    fontFamily = FontFamily(Font(R.font.micro_regular))
                )
                Text(
                    text = "${state.config.maxDetectionRange.toInt()}M",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 7.sp,
                    fontFamily = FontFamily(Font(R.font.micro_regular))
                )
            }
        }

        // 2. Zone Contrôle (Bargraph + Power)
        Box(
            modifier = Modifier
                .height(16.dp)
                .constrainAs(barGraph) {
                    top.linkTo(visualizer.bottom, margin = 4.dp)
                    start.linkTo(visualizer.start, margin = 6.dp)
                    end.linkTo(pingButton.start, margin = 6.dp)
                    width = Dimension.fillToConstraints
                }
        ) {
            state.barGraphNode.Render(modifier = Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .constrainAs(pingButton) {
                    top.linkTo(barGraph.top)
                    bottom.linkTo(barGraph.bottom)
                    end.linkTo(visualizer.end, margin = 4.dp)
                }
        ) {
            state.pingButtonNode.Render(modifier = Modifier.fillMaxSize())
        }

        // 3. Bloc Mesure Final (Ultra compact)
        Text(
            text = "FLOOR PROXIMITY",
            color = Color.Gray,
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.micro_regular)),
            modifier = Modifier.constrainAs(txtFloor) {
                top.linkTo(barGraph.bottom, margin = 0.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Box(
            modifier = Modifier
                .width(75.dp)
                .height(26.dp)
                .constrainAs(depthDisplay) {
                    top.linkTo(txtFloor.bottom, margin = 0.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 2.dp)
                }
        ) {
            state.depthDisplayNode.Render(modifier = Modifier.fillMaxSize())
        }
    }
}