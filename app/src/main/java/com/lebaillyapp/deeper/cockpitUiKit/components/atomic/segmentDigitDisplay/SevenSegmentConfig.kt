package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## SevenSegmentConfig
 *
 * Définit les paramètres structurels et visuels d'un afficheur à 7 segments.
 * Permet de simuler un rendu LED/LCD rétro avec gestion du relief et de la luminescence.
 *
 * @property titleParam Identifiant ou titre associé au paramètre affiché.
 * @property segmentLength Longueur des segments verticaux (côtés F, E, C, B).
 * @property segmentHorizontalLength Longueur des segments horizontaux (A, D, G).
 * @property segmentThickness Épaisseur (largeur) du tracé de chaque segment.
 * @property bevel Taille du biseau appliqué aux extrémités des segments pour un rendu matériel.
 * @property onColor Couleur d'émission des segments lorsqu'ils sont alimentés (état "ON").
 * @property offColor Couleur des segments à l'état de repos (état "OFF").
 * @property alpha Opacité globale appliquée à l'ensemble du composant (0.0 à 1.0).
 * @property glowRadius Rayon de l'effet de halo lumineux (Bloom) entourant les segments actifs.
 * @property flickerAmplitude Intensité de la variation aléatoire de luminosité (scintillement).
 * @property flickerFrequency Fréquence du cycle de scintillement en Hertz (Hz).
 * @property idleMode Active une séquence d'animation de rotation cyclique (mode attente).
 * @property idleSpeed Délai de rafraîchissement entre chaque étape de l'animation idle (en ms).
 */
data class SevenSegmentConfig(
    val titleParam: String,
    val segmentLength: Dp = 15.dp,
    val segmentHorizontalLength: Dp = 15.dp,
    val segmentThickness: Dp = 3.dp,
    val bevel: Dp = 1.dp,
    val onColor: Color = Color.Red,
    val offColor: Color = Color.DarkGray,
    val alpha: Float = 1f,
    val glowRadius: Float = 15f,
    val flickerAmplitude: Float = 0.25f,
    val flickerFrequency: Float = 1f,
    var idleMode: Boolean = false,
    val idleSpeed: Long = 100
) : ComponentConfig(titleParam, onColor)