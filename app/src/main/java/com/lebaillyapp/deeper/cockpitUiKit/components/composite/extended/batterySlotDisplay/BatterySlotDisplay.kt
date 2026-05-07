package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ## BatterySlotDisplay
 *
 * Fragment visuel représentant un emplacement de batterie individuel.
 * Ce composant est piloté par le [BatterySlotState] qui contient les sous-nodes.
 *
 * ### Composition :
 * - **Label** : Identifiant du slot (ex: "BAT-01").
 * - **SevenSegNode** : Affichage numérique de la capacité (Ah).
 * - **BargraphNode** : Visualisation analogique du niveau.
 * - **RockerNode** : Commande physique de connexion au bus.
 *
 * @param modifier Modificateur de mise en page.
 * @param label Texte affiché en haut du slot.
 * @param state État orchestrateur contenant les instances des Nodes enfants.
 */
@Composable
fun BatterySlotDisplay(
    modifier: Modifier = Modifier,
    label: String,
    state: BatterySlotState
) {
    Column(
        modifier = modifier
            .width(55.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF333333), RoundedCornerShape(4.dp))
            .padding(vertical = 10.dp, horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Identification du module
        Text(
            text = label.uppercase(),
            color = Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Affichage Digital (Dispatch vers le Node composite)
        state.segNode.Render(
            modifier = Modifier.fillMaxWidth()
        )

        // 3. Visualisation du niveau (Dispatch vers le Node atomique)
        state.barNode.Render(
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(0.4f)
        )

        // 4. Commande de puissance (Dispatch vers le Node atomique)
        state.rockNode.Render(
            modifier = Modifier.size(30.dp, 60.dp)
        )
    }
}