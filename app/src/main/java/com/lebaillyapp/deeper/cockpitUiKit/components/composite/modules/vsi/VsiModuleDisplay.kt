package com.lebaillyapp.deeper.cockpitUiKit.components.composite.modules.vsi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.R

/**
 * ## VsiModuleDisplay
 *
 * Composable de présentation pour le module VSI.
 * Gère le layout (Card, Column, Box) et le placement des sous-composants.
 */
@Composable
fun VsiModuleDisplay(
    modifier: Modifier = Modifier,
    state: VsiModuleState,
    config: VsiModuleConfig
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = config.containerColor)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Aligne les textes et la Box au centre
        ) {
            // ERREUR ICI : On enlève le Alignment.Center pour laisser le comportement par défaut
            Box {
                // La jauge : elle prend sa place naturelle
                state.vsiNode.Render(
                    Modifier
                        .height(180.dp)
                        .width(75.dp)
                )

                // La colonne de LED : alignée à droite de la Box, avec son padding de 12.dp
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd) // Aligne la colonne verticalement au centre à droite
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.Center // Pour que les LED soient bien groupées verticalement
                ) {
                    state.ledUp.Render(Modifier.align(Alignment.CenterHorizontally))
                    state.ledNeutral.Render(Modifier.align(Alignment.CenterHorizontally))
                    state.ledDown.Render(Modifier.align(Alignment.CenterHorizontally))
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "V-Speed (${config.unitLabel})",
                color = Color(0xFF3F3D3D),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.micro_regular)),
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(3.dp))

            val absValue = if (state.value >= 0f) state.value else state.value * -1f
            Text(
                text = String.format("%.1f", absValue),
                color = Color(0xFF696666),
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.micro_regular)),
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}