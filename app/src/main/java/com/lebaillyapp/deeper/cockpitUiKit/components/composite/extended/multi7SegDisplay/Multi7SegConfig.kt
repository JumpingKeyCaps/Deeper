package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## Multi7SegConfig
 *
 * Configuration globale pour un groupe d'afficheurs 7 segments.
 *
 * @property titleParam Titre ou identifiant du groupe.
 * @property numDigits Nombre de digits (Nodes atomiques) à générer pour ce groupe.
 * @property spacing Espacement standard entre chaque digit.
 * @property extraSpacingStep Intervalle d'insertion d'un espacement additionnel (ex: 3 pour les milliers).
 * @property extraSpacing Valeur de l'espacement additionnel.
 * @property showZeroWhenEmpty Si true, affiche '0' sur les digits non utilisés par la valeur.
 * @property reversedOverride Si true, remplit les digits de droite à gauche.
 * @property activateReflect Active l'effet de réflexion sous l'afficheur.
 * @property reflectConfig Paramètres spécifiques à la réflexion (alpha, angle, etc.).
 * @property baseSegmentConfig Configuration visuelle par défaut appliquée à chaque segment du groupe.
 */
data class Multi7SegConfig(
    val titleParam: String,
    val numDigits: Int = 4,
    val spacing: Dp = 8.dp,
    val extraSpacingStep: Int = 0,
    val extraSpacing: Dp = 0.dp,
    val showZeroWhenEmpty: Boolean = false,
    val reversedOverride: Boolean = false,
    val activateReflect: Boolean = false,
    val reflectConfig: ReflectConfig = ReflectConfig(),
    val baseSegmentConfig: SevenSegmentConfig = SevenSegmentConfig(titleParam = "INTERNAL_SEG")
) : ComponentConfig(titleParam, baseSegmentConfig.onColor)

/**
 * ## ReflectConfig
 *
 * Paramètres de transformation pour le rendu de la réflexion (effet miroir).
 *
 * @property reflectAlpha Opacité de la réflexion.
 * @property reflectSpacing Distance entre l'afficheur réel et sa réflexion.
 * @property reflectAngle Inclinaison (rotationX) pour simuler la perspective au sol.
 * @property reflectGlowRadius Intensité du flou lumineux spécifique à la réflexion.
 * @property reflectCameraAdjustment Ajustement de la perspective (cameraDistance).
 */
data class ReflectConfig(
    val reflectAlpha: Float = 0.3f,
    val reflectSpacing: Dp = 0.dp,
    val reflectAngle: Float = 245f,
    val reflectGlowRadius: Float = 20f,
    val reflectCameraAdjustment: Float = 6f
)