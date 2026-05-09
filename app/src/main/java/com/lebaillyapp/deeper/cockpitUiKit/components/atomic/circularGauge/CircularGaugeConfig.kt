package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.circularGauge

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * Configuration exhaustive pour la jauge circulaire 270° (Style Médaillon Cockpit).
 *
 * @property titleParam Nom de l'indicateur affiché en bas du médaillon.
 * @property primaryColorParam Couleur principale (utilisée par défaut pour l'échelle).
 * @property gaugeSize Diamètre total du médaillon circulaire.
 * @property minValue Valeur minimale de l'échelle (-135°).
 * @property maxValue Valeur maximale de l'échelle (+135°).
 * @property majorTickInterval Intervalle entre les grands ticks. Calculé automatiquement si null.
 * @property minorTicksPerMajor Nombre de graduations mineures entre deux majeures.
 * @property cardBackgroundColor Couleur de fond du médaillon et du cache-pivot central.
 * @property scaleColor Couleur des graduations et des chiffres de l'échelle.
 * @property firstTickColor Couleur spécifique pour la première graduation (alerte basse).
 * @property lastTickColor Couleur spécifique pour la dernière graduation (alerte haute).
 * @property needleColor Couleur de l'aiguille flottante.
 * @property valueTextColor Couleur de la valeur numérique centrale.
 * @property titleColor Couleur du texte du titre.
 * @property valueTextSize Taille de la police pour la valeur centrale.
 * @property titleTextSize Taille de la police pour le titre.
 * @property labelTextSize Taille de la police pour les chiffres de l'échelle.
 * @property majorTickStrokeWidth Épaisseur des graduations majeures.
 * @property minorTickStrokeWidth Épaisseur des graduations mineures.
 * @property needleStrokeWidth Épaisseur de l'aiguille.
 * @property animationDurationMillis Durée de l'animation de l'aiguille en millisecondes.
 * @property titleBottomMarging Espacement vertical du titre par rapport au bas du cercle.
 * @property titleFontFamily Famille de police optionnelle pour l'ensemble des textes.
 */
data class CircularGaugeConfig(
    val titleParam: String,
    val primaryColorParam: Color = Color(0xFFBBBBBB),
    // --- Dimensions & Comportement ---
    val gaugeSize: Dp = 100.dp,
    val minValue: Float = 0f,
    val maxValue: Float = 100f,
    val majorTickInterval: Float? = null,
    val minorTicksPerMajor: Int = 4,
    val animationDurationMillis: Int = 600,
    val titleBottomMarging: Dp = 30.dp,
    // --- Couleurs ---
    val cardBackgroundColor: Color = Color(0xFF181818),
    val scaleColor: Color = Color(0xFFBBBBBB),
    val firstTickColor: Color = Color(0xFFA40525),
    val lastTickColor: Color = Color(0xFFBBBBBB),
    val needleColor: Color = Color(0xFFFFA500),
    val valueTextColor: Color = Color.White,
    val titleColor: Color = Color(0xFFAAAAAA),
    // --- Typographies ---
    val valueTextSize: TextUnit = 20.sp,
    val titleTextSize: TextUnit = 11.sp,
    val labelTextSize: TextUnit = 8.sp,
    val titleFontFamily: FontFamily? = null,
    // --- Tracés ---
    val majorTickStrokeWidth: Dp = 1.5.dp,
    val minorTickStrokeWidth: Dp = 0.8.dp,
    val needleStrokeWidth: Dp = 2.5.dp
) : ComponentConfig(titleParam, primaryColorParam)