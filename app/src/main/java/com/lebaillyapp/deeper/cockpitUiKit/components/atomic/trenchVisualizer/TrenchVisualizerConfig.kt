package com.lebaillyapp.deeper.cockpitUiKit.components.atomic.trenchVisualizer

import androidx.compose.ui.graphics.Color
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## TrenchVisualizerConfig
 *
 * Configuration structurelle et esthétique du visualiseur de fosse.
 * Définit les paramètres "hardware" fixes de la grille 3D.
 *
 * @param titleParam Titre affiché sur le module (ex: "SONAR DEPTH", "TRENCH MAP").
 * @param scanSpeed Vitesse de rotation complète en millisecondes.
 * @param sigma Largeur de la cloche de Gauss (étalement du creux).
 * @param peakHeight Profondeur maximale théorique du pic.
 * @param gridCols Nombre de colonnes de la grille.
 * @param gridRows Nombre de rangées de la grille.
 * @param verticalBias Équilibre vertical (0.35f par défaut pour éviter la remontée).
 * @param heatMapScale Liste des 3 couleurs pour le dégradé de profondeur (Surface -> Milieu -> Fond).
 */
data class TrenchVisualizerConfig(
    val titleParam: String,
    val scanSpeed: Int = 10000,
    val sigma: Float = 4f,
    val peakHeight: Float = 600f,
    val gridCols: Int = 20,
    val gridRows: Int = 20,
    val verticalBias: Float = 0.35f,
    val heatMapScale: List<Color> = listOf(
        Color.Cyan,
        Color(0xFF00BFA5),
        Color(0xFF1A237E)
    )
) : ComponentConfig( title = titleParam, primaryColor = heatMapScale.firstOrNull() ?: Color.Cyan)