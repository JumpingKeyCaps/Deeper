package com.lebaillyapp.deeper.ShaderUiKit

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.ParallaxKit.ParallaxState
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.ShaderUiKit.config.CrtSettings
import com.lebaillyapp.deeper.ShaderUiKit.config.LcdSettings
import com.lebaillyapp.deeper.ShaderUiKit.config.updateCrtUniforms
import com.lebaillyapp.deeper.ShaderUiKit.config.updateLcdUniforms

/**
 * DeeperShaderLayout est le moteur de rendu principal du cockpit.
 * Il empile deux couches de shaders AGSL (LCD + CRT) et applique une transformation
 * de parallaxe physique et de scaling dynamique.
 *
 * @param parallaxState L'état courant du gyroscope (roll/pitch) fourni par le ParallaxProvider.
 * @param activateCRT Active/Désactive l'effet de lentille bombée et de tube cathodique.
 * @param activateLCD Active/Désactive le rendu rétro-éclairé pixélisé (Dithering).
 * @param settingsCRT Configuration des effets visuels du tube (Fisheye, Scanlines, Glitch).
 * @param settingsLCD Configuration du rendu LCD (Palette, Dithering, Grille).
 * @param content Le contenu UI de l'instrumentation à afficher dans le cockpit.
 */
@Composable
fun DeeperShaderLayout(
    activateCRT: Boolean = true,
    activateLCD: Boolean = true,
    settingsCRT: CrtSettings = CrtSettings(),
    settingsLCD: LcdSettings = LcdSettings(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current

    // Scaling dynamique pour matcher le ratio de référence (412dp)
    val referenceWidth = 412f
    val scaleFactor = config.screenWidthDp / referenceWidth

    // Chargement des sources shaders depuis les ressources raw
    val crtSource = remember {
        context.resources.openRawResource(R.raw.crt_lens).use { it.bufferedReader().readText() }
    }
    val lcdSource = remember {
        context.resources.openRawResource(R.raw.retroboy_shader_opti_ds).use { it.bufferedReader().readText() }
    }

    val crtShader = remember(crtSource) { RuntimeShader(crtSource) }
    val lcdShader = remember(lcdSource) { RuntimeShader(lcdSource) }

    val time by rememberInfiniteTransition(label = "GlobalTime").animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing)),
        label = "Time"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // --- COUCHE SUPÉRIEURE : CRT LENS ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    if (activateCRT) {
                        // Setup des uniforms standards
                        crtShader.setFloatUniform("size", size.width, size.height)
                        crtShader.setFloatUniform("time", time)

                        // Update via tes fonctions d'extension
                        crtShader.updateCrtUniforms(settingsCRT)

                        renderEffect = RenderEffect.createRuntimeShaderEffect(
                            crtShader, "composable"
                        ).asComposeRenderEffect()
                    }
                }
        ) {
            // --- COUCHE INFÉRIEURE : LCD DITHERING + SCALING ---
            Box(
                modifier = Modifier
                    .size(width = 412.dp, height = 915.dp) // Zone de travail fixe
                    .graphicsLayer {
                        // On scale le contenu pour qu'il occupe l'écran réel
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        if (activateLCD) {
                            lcdShader.setFloatUniform("res", size.width, size.height)
                            lcdShader.setFloatUniform("imgRes", size.width, size.height)

                            // Update via tes fonctions d'extension
                            lcdShader.updateLcdUniforms(settingsLCD)

                            renderEffect = RenderEffect.createRuntimeShaderEffect(
                                lcdShader, "inputFrame"
                            ).asComposeRenderEffect()
                        }
                    }
            ) {
                // Ton UI, Image ou instrumentation
                content()
            }
        }
    }
}