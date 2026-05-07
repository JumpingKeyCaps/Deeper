package com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.batterySlotDisplay

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lebaillyapp.deeper.R
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.rockerSwitchVertical.RockerSwitchConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentDigitDisplay.SevenSegmentConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.atomic.segmentedBargraph.SegmentedBargraphConfig
import com.lebaillyapp.deeper.cockpitUiKit.components.composite.extended.multi7SegDisplay.Multi7SegConfig
import com.lebaillyapp.deeper.cockpitUiKit.core.ComponentConfig

/**
 * ## BatterySlotConfig
 *
 * Configuration agrégée pour un module BatterySlot.
 * Gère le style par défaut du label, du bargraph et de l'afficheur digital (2 digits).
 *
 * @param slotLabel Identifiant du slot (ex: "BAT-01").
 * @param rockerConfig Configuration du switch (doit être fournie car l'icône est dynamique).
 * @param segConfig Configuration de l'afficheur digital avec tes réglages "Red Flicker".
 * @param barConfig Configuration du bargraph de niveau.
 * @param backgroundColor Couleur de fond du boîtier de la tranche.
 */
data class BatterySlotConfig(
    val slotLabel: String,
    val rockerConfig: RockerSwitchConfig = RockerSwitchConfig(
            titleParam = "CONNECT",
            iconSource = { painterResource(id = R.drawable.eclaire) },
            width = 30.dp,
            height = 60.dp
        ),
    val segConfig: Multi7SegConfig = Multi7SegConfig(
        titleParam = "AH",
        numDigits = 2,
        spacing = 3.dp,
        extraSpacingStep = 0,
        extraSpacing = 0.dp,
        showZeroWhenEmpty = true,
        reversedOverride = true,
        activateReflect = false,
        baseSegmentConfig = SevenSegmentConfig(
            titleParam = "DEBUG_SEG",
            onColor = Color.Red,
            segmentLength = 8.dp,
            segmentHorizontalLength = 8.dp,
            segmentThickness = 1.dp,
            bevel = 1.dp,
            glowRadius = 10f,
            flickerAmplitude = 0.15f,
            flickerFrequency = 1f
        )
    ),
    val barConfig: SegmentedBargraphConfig = SegmentedBargraphConfig(
        titleParam = "LEVEL",
        segments = 10,
        glowRadius = 5.dp,
        barCornerRadius = 1.dp
    ),
    val backgroundColor: Color = Color(0xFF1A1A1A)
) : ComponentConfig(slotLabel, backgroundColor)