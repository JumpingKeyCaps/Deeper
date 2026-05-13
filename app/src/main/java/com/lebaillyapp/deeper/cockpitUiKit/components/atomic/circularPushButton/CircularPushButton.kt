package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularPushButton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularPushButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
    size: Dp = 48.dp,
    activeColor: Color = Color(0xFF00E5FF),
    iconSource: Painter,
    animationDuration: Int = 150
) {
    val interactionSource = remember { MutableInteractionSource() }

    val buttonScale by animateFloatAsState(
        targetValue = if (isChecked) 0.92f else 1.0f,
        animationSpec = tween(animationDuration), label = "Scale"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isChecked) 0.6f else 0.0f,
        animationSpec = tween(animationDuration), label = "Glow"
    )
    val ringColor by animateColorAsState(
        targetValue = if (isChecked) activeColor else Color.White.copy(alpha = 0.2f),
        animationSpec = tween(animationDuration), label = "Color"
    )

    // On définit une zone de sécurité pour le flou
    val glowSize = size * 0.8f

    Box(
        modifier = modifier
            .size(size) // Le conteneur garde sa taille
            .clickable(interactionSource = interactionSource, indication = null) {
                onToggle(!isChecked)
            },
        contentAlignment = Alignment.Center
    ) {
        // 1. L'anneau lumineux (CORRIGÉ)
        if (isChecked) {
            Box(
                modifier = Modifier
                    .size(glowSize) // Plus petit pour ne pas taper les bords
                    .graphicsLayer {
                        alpha = glowAlpha
                        renderEffect = BlurEffect(20f, 20f, TileMode.Decal) // Plus propre que le .blur()
                    }
                    .background(activeColor, CircleShape)
            )
        }

        // 2. Le Bezel
        Box(
            modifier = Modifier
                .fillMaxSize(0.9f) // On réduit un peu le bouton pour laisser du padding au glow
                .border(2.dp, Color(0xFF121212), CircleShape)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF252525), Color(0xFF101010))),
                    CircleShape
                )
        )

        // 3. Le Bouton mobile
        Box(
            modifier = Modifier
                .fillMaxSize(0.75f) // Ajusté pour la hiérarchie visuelle
                .graphicsLayer {
                    scaleX = buttonScale
                    scaleY = buttonScale
                }
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF444444), Color(0xFF222222)),
                    ),
                    CircleShape
                )
                .border(1.5.dp, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = iconSource,
                contentDescription = null,
                tint = ringColor,
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}