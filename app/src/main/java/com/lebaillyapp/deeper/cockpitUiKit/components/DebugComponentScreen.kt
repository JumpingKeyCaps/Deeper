package com.lebaillyapp.deeper.cockpitUiKit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.glassButton.GlassButtonNode

@Composable
fun DebugComponentScreen() {
    // Instance du bouton
    val testNode = remember {
        GlassButtonNode(
            id = "BTN_GEAR",
            config = GlassButtonConfig(
                titleParam = "GEAR DOWN",
                colorParam = Color(0xFFF84367), // Vert cockpit
                width = 90.dp,
                height = 30.dp,
                cornerRadius = 8.dp,
                glowRadius = 4.9f
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Rendu du bouton
        testNode.Render(modifier = Modifier.wrapContentSize())

        // Bouton de simulation (Moteur de jeu)
        Button(
            onClick = { testNode.state.update(!testNode.state.value) },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp)
        ) {
            Text(if (testNode.state.value) "SYSTEM: ON" else "SYSTEM: OFF")
        }
    }
}