package com.lebaillyapp.deeper.ShaderUiKit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.ShaderUiKit.config.CrtControlPanel
import com.lebaillyapp.deeper.ShaderUiKit.config.CrtSettings
import com.lebaillyapp.deeper.ShaderUiKit.config.LcdControlPanel
import com.lebaillyapp.deeper.ShaderUiKit.config.LcdSettings

/**
 * Ecran de test R&D pour calibrer les Shaders du projet Deeper.
 */
@Composable
fun DebugShaderScreen() {
    // --- ÉTATS DES RÉGLAGES (Source de vérité) ---
    var crtSettings by remember { mutableStateOf(CrtSettings()) }
    var lcdSettings by remember { mutableStateOf(LcdSettings()) }

    var showCrtMenu by remember { mutableStateOf(false) }
    var showLcdMenu by remember { mutableStateOf(false) }

    // On peut basculer les shaders pour voir la différence
    var activateCRT by remember { mutableStateOf(true) }
    var activateLCD by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // --- LE COMPONENT MASTER (La greffe qu'on vient de faire) ---
        DeeperShaderLayout(
            activateCRT = activateCRT,
            activateLCD = activateLCD,
            settingsCRT = crtSettings,
            settingsLCD = lcdSettings
        ) {
            // CONTENU DE TEST : Une image qui permet de bien voir la distorsion
            // Utilise une grille ou un paysage contrasté
            Image(
                painter = painterResource(id = R.drawable.compositex2b),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // --- INTERFACE DE DEBUG (Hors Shaders pour rester lisible) ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Toggle Rapide CRT
            FilterChip(
                selected = activateCRT,
                onClick = { activateCRT = !activateCRT },
                label = { Text("CRT ${if(activateCRT) "ON" else "OFF"}") },
                colors = FilterChipDefaults.filterChipColors(labelColor = Color.White, selectedContainerColor = Color.Cyan.copy(0.5f))
            )
            // Toggle Rapide LCD
            FilterChip(
                selected = activateLCD,
                onClick = { activateLCD = !activateLCD },
                label = { Text("LCD ${if(activateLCD) "ON" else "OFF"}") },
                colors = FilterChipDefaults.filterChipColors(labelColor = Color.White, selectedContainerColor = Color(0xFFD6E86A).copy(0.5f))
            )
        }

        // --- BOUTONS D'OUVERTURE DES PANNEAUX ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Bouton Settings CRT
            LargeIconButton(
                onClick = {
                    showCrtMenu = !showCrtMenu
                    if (showCrtMenu) showLcdMenu = false
                },
                icon = Icons.Default.Settings,
                color = Color.Cyan,
                isActive = showCrtMenu
            )

            // Bouton Settings LCD
            LargeIconButton(
                onClick = {
                    showLcdMenu = !showLcdMenu
                    if (showLcdMenu) showCrtMenu = false
                },
                icon = Icons.Default.Build,
                color = Color(0xFFD6E86A),
                isActive = showLcdMenu
            )
        }

        // --- AFFICHAGE DES PANNEAUX DE CONTRÔLE ---
        if (showCrtMenu) {
            CrtControlPanel(
                settings = crtSettings,
                onSettingsChange = { crtSettings = it },
                onClose = { showCrtMenu = false },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        if (showLcdMenu) {
            LcdControlPanel(
                settings = lcdSettings,
                onSettingsChange = { lcdSettings = it },
                onClose = { showLcdMenu = false },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun LargeIconButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isActive: Boolean
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isActive) color.copy(0.4f) else Color.White.copy(0.15f)
        ),
        shape = CircleShape
    ) {
        Icon(icon, contentDescription = null, tint = if (isActive) Color.White else color)
    }
}