package com.lebaillyapp.deeper.ParallaxKit

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultShadowColor

@Composable
fun DebugParallaxScreen(context: Context = LocalContext.current) {
    val provider = remember { ParallaxProvider(context) }
    val parallaxState by provider.state.collectAsState()

    // Note : j'ai remonté un peu le smoothing (0.1f) car 0.005f risque d'être
    // trop lent pour percevoir la réactivité immédiate.
    DisposableEffect(Unit) {
        provider.start(smoothing = 0.25f, frictionValue = 0.2f)
        onDispose { provider.stop() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .clickable { provider.calibrate() },
        contentAlignment = Alignment.Center
    ) {
        // --- COUCHE 1 : LE FOND (L'ancrage) ---
        Box(
            modifier = Modifier
                .size(220.dp)
                .graphicsLayer {
                    translationX = parallaxState.roll * 1.2f
                    translationY = parallaxState.pitch * 1.2f
                    alpha = 0.3f
                }
                .background(Color.DarkGray, RoundedCornerShape(16.dp))
        )

        // --- COUCHE 2 : LE CHASSIS (Milieu) ---
        Box(
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer {
                    translationX = parallaxState.roll * 2.5f
                    translationY = parallaxState.pitch * 2.5f

                    // Rotation : on pivote pour faire face à l'utilisateur
                    rotationY = -parallaxState.roll * 0.15f
                    rotationX = parallaxState.pitch * 0.15f

                    cameraDistance = 12f * density
                    shadowElevation = 10f
                    shape = RoundedCornerShape(12.dp)
                    clip = true
                }
                .background(Color(0xFF444444))
        )

        // --- COUCHE 3 : LE BOUTON (Sommet de la pyramide) ---
        Box(
            modifier = Modifier
                .size(70.dp)
                .graphicsLayer {
                    // Translation plus forte pour l'effet "proche"
                    translationX = parallaxState.roll * 5f
                    translationY = parallaxState.pitch * 5f

                    // Contre-rotation plus marquée
                    rotationY = -parallaxState.roll * 0.4f
                    rotationX = parallaxState.pitch * 0.4f

                    // L'ombre est la clé :
                    // On force une Shape pour qu'elle apparaisse
                    shadowElevation = 30f
                    shape = RoundedCornerShape(8.dp)

                    // Optionnel : colorer l'ombre pour le style
                    spotShadowColor = Color.Black
                    ambientShadowColor = Color.Black
                }
                .background(Color.Gray, RoundedCornerShape(8.dp))
        )

        // Debug info
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Roll: ${parallaxState.roll.toInt()} | Pitch: ${parallaxState.pitch.toInt()}",
                color = Color.White.copy(alpha = 0.6f)
            )
            Text(
                text = "Clique pour calibrer",
                color = Color.Cyan.copy(alpha = 0.6f)
            )
        }
    }
}