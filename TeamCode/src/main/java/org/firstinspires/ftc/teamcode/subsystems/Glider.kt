package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PwmControl.PwmRange
import com.qualcomm.robotcore.hardware.ServoImplEx

object Glider {

    var offset = 0.0
    var HIGHER_LIMIT = 0.69 //~53 degrees
    var LOWER_LIMIT = 0.0   //~33 degrees

    private val distanceAngleTable = listOf(
        0.5 to 53.0,
        1.0 to 49.0,
        1.5 to 44.0,
        2.0 to 38.0,
        2.5 to 33.0
    )

    private lateinit var servoGlider: ServoImplEx

    fun init(hardwareMap: HardwareMap) {
        servoGlider = hardwareMap.get(ServoImplEx::class.java, "servoGlider")
        servoGlider.pwmRange = PwmRange(500.0, 2500.0)
    }

    fun setPosition(position: Double) {
        servoGlider.position = position.coerceIn(LOWER_LIMIT, HIGHER_LIMIT)
    }

    fun setPositionDeg(degrees: Double) {
        val minDeg = 33.0
        val maxDeg = 53.0
        val clampedDeg = degrees.coerceIn(minDeg, maxDeg)
        val position = LOWER_LIMIT + (clampedDeg - minDeg) * (HIGHER_LIMIT - LOWER_LIMIT) / (maxDeg - minDeg)
        servoGlider.position = position.coerceIn(LOWER_LIMIT, HIGHER_LIMIT)
    }

    fun getPosition(): Double {
        return servoGlider.position
    }
    fun autoAim(distance: Double) {
        val clampedDistance = distance.coerceIn(
            distanceAngleTable.first().first,
            distanceAngleTable.last().first
        )

        val lower = distanceAngleTable.lastOrNull { it.first <= clampedDistance } ?: distanceAngleTable.first()
        val upper = distanceAngleTable.firstOrNull { it.first >= clampedDistance } ?: distanceAngleTable.last()

        val angle = if (lower == upper) {
            lower.second
        } else {
            val (d1, a1) = lower
            val (d2, a2) = upper
            a1 + (clampedDistance - d1) * (a2 - a1) / (d2 - d1)
        }

        val adjustedAngle = angle + offset

        setPositionDeg(adjustedAngle)
    }
}