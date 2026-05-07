package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentNode

/**
 * ### Multi7SegDisplay
 *
 * Version adaptée pour piloter des [SevenSegmentNode].
 * Conserve toute la logique originale de placement et d'override.
 */
@Composable
fun Multi7SegDisplay(
    modifier: Modifier = Modifier,
    nodes: List<SevenSegmentNode>, // On reçoit des Nodes au lieu de Configs
    overrideValue: String? = null,
    reversedOverride: Boolean = false,
    spacing: Dp = 8.dp,
    showZeroWhenEmpty: Boolean = false,
    extraSpacingStep: Int = 0,
    extraSpacing: Dp = 0.dp,
    activateReflect: Boolean = false,
    reflectConfig: ReflectConfig
) {
    Column(modifier = modifier) {
        // --- Affichage des chiffres ---
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            nodes.forEachIndexed { index, node ->
                if (index > 0) {
                    val addExtra = extraSpacingStep > 0 && index % extraSpacingStep == 0
                    val totalSpacing = spacing + if (addExtra) extraSpacing else 0.dp
                    Spacer(modifier = Modifier.width(totalSpacing))
                }

                // Logique originale conservée pour déterminer quoi afficher sur ce digit précis
                val overrideChar = when {
                    overrideValue == null -> null
                    reversedOverride -> {
                        val charIndex = overrideValue.length - nodes.size + index
                        overrideValue.getOrNull(charIndex)
                    }
                    else -> overrideValue.getOrNull(index)
                }

                val digitOverride = overrideChar?.digitToIntOrNull()
                val charOverride = if (digitOverride == null) overrideChar else null

                // On applique la logique de priorité au Node
                // Note: On utilise ici les méthodes de confort du Node ou on tape dans son state
                when {
                    overrideChar != null -> {
                        if (digitOverride != null) node.setDigit(digitOverride)
                        else node.setChar(charOverride!!)
                    }
                    showZeroWhenEmpty -> node.setDigit(0)
                    else -> {
                        // Si pas d'override, on laisse le node dans son état initial
                        // ou on le reset (selon ton besoin)
                    }
                }

                // Rendu final via le Node
                node.Render(modifier = Modifier)
            }
        }

        // --- REFLEXION ---
        if (activateReflect) {
            Spacer(modifier = Modifier.height(reflectConfig.reflectSpacing))
            Row(
                modifier = Modifier
                    .alpha(reflectConfig.reflectAlpha)
                    .graphicsLayer {
                        rotationY = 0f
                        rotationX = reflectConfig.reflectAngle
                        cameraDistance = reflectConfig.reflectCameraAdjustment * density
                    }
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pour la réflexion, on répète la logique de placement
                nodes.forEachIndexed { index, node ->
                    if (index > 0) {
                        val addExtra = extraSpacingStep > 0 && index % extraSpacingStep == 0
                        val totalSpacing = spacing + if (addExtra) extraSpacing else 0.dp
                        Spacer(modifier = Modifier.width(totalSpacing))
                    }

                    // On appelle le Render du Node.
                    // Comme le state a déjà été mis à jour dans la Row du haut,
                    // le rendu sera synchronisé.
                    node.Render(modifier = Modifier)
                }
            }
        }
    }
}