package com.lebaillyapp.deeper.ParallaxKit

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Représente l'état de la parallaxe prêt à l'emploi pour Jetpack Compose.
 */
@Immutable
data class ParallaxState(
    val roll: Float = 0f,   // Inclinaison latérale (X)
    val pitch: Float = 0f,  // Inclinaison avant/arrière (Y)
    val isCalibrating: Boolean = false
)

class ParallaxProvider(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    // Paramètres de lissage et physique
    private var smoothingFactor = 0.15f
    private var friction = 0.92f // Taux de décélération pour l'inertie
    private var velocityThreshold = 0.001f

    // États internes
    private val _state = MutableStateFlow(ParallaxState())
    val state = _state.asStateFlow()

    private var offsetRoll = 0f
    private var offsetPitch = 0f

    private var targetRoll = 0f
    private var targetPitch = 0f
    private var velocityRoll = 0f
    private var velocityPitch = 0f

    private var physicsJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    fun start(smoothing: Float = 0.15f, frictionValue: Float = 0.92f) {
        this.smoothingFactor = smoothing
        this.friction = frictionValue

        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }

        // Lance la boucle physique pour l'inertie
        startPhysicsLoop()
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        physicsJob?.cancel()
    }

    /**
     * Définit la position actuelle du téléphone comme le point "zéro".
     */
    fun calibrate() {
        _state.update { it.copy(isCalibrating = true) }
        offsetRoll = targetRoll
        offsetPitch = targetPitch
        _state.update { it.copy(isCalibrating = false, roll = 0f, pitch = 0f) }
        velocityRoll = 0f
        velocityPitch = 0f
    }

    private fun startPhysicsLoop() {
        physicsJob?.cancel()
        physicsJob = scope.launch {
            while (true) {
                // 1. Calcul de la différence entre la cible (capteur) et l'état actuel (parallaxe)
                val diffRoll = (targetRoll - offsetRoll) - _state.value.roll
                val diffPitch = (targetPitch - offsetPitch) - _state.value.pitch

                // 2. Application de l'accélération (basée sur le smoothingFactor)
                velocityRoll += diffRoll * smoothingFactor
                velocityPitch += diffPitch * smoothingFactor

                // 3. Application de la friction (Inertie)
                velocityRoll *= friction
                velocityPitch *= friction

                // 4. Mise à jour de la position
                val newRoll = _state.value.roll + velocityRoll
                val newPitch = _state.value.pitch + velocityPitch

                // 5. Update du flow si le mouvement est significatif
                if (abs(velocityRoll) > velocityThreshold || abs(velocityPitch) > velocityThreshold) {
                    _state.update { it.copy(roll = newRoll, pitch = newPitch) }
                }

                delay(16) // ~60 FPS
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ROTATION_VECTOR) return

        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

        // Remappage pour l'orientation Portrait standard (ton approche V2)
        val remappedMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(
            rotationMatrix,
            SensorManager.AXIS_X,
            SensorManager.AXIS_Z,
            remappedMatrix
        )

        val orientation = FloatArray(3)
        SensorManager.getOrientation(remappedMatrix, orientation)

        // Mise à jour des cibles pour la boucle physique
        targetRoll = Math.toDegrees(orientation[2].toDouble()).toFloat()
        targetPitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}