package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit

object Shooter {
    private lateinit var shooterFirst: DcMotorEx
    private lateinit var shooterSecond: DcMotorEx

    var maxRPMForward = 1.0  // placeholder, will calibrate
    private var maxRPMBackward = 1.0 // placeholder, will calibrate

    fun init(hardwareMap: HardwareMap) {
        shooterFirst = hardwareMap.get(DcMotorEx::class.java, "motorShooterFirst")
        shooterSecond = hardwareMap.get(DcMotorEx::class.java, "motorShooterSecond")

        shooterFirst.direction = DcMotorSimple.Direction.REVERSE
        shooterSecond.direction = DcMotorSimple.Direction.REVERSE

        val motorConfigurationType = shooterFirst.motorType.clone()
        motorConfigurationType.achieveableMaxRPMFraction = 1.0
        shooterFirst.motorType = motorConfigurationType
        shooterSecond.motorType = motorConfigurationType
    }

    // ---- Basic control ----

    /**
     * Sets power in range [-1, 1] and scales it to the calibrated RPM.
     */
    fun setPower(power: Double) {
        val rpm = powerToRPM(power)
        val ticksPerRev = shooterFirst.motorType.ticksPerRev
        val targetVelocity = (rpm / 60.0) * ticksPerRev

        shooterFirst.velocity = targetVelocity
        shooterSecond.velocity = targetVelocity
    }

    fun stop() {
        shooterFirst.power = 0.0
        shooterSecond.power = 0.0
    }

    fun getCurrentDraw(): Double {
        val currentFirst = shooterFirst.getCurrent(CurrentUnit.AMPS)
        val currentSecond = shooterSecond.getCurrent(CurrentUnit.AMPS)
        return currentFirst + currentSecond
    }

    fun getAverageCurrent(): Double {
        val currentFirst = shooterFirst.getCurrent(CurrentUnit.AMPS)
        val currentSecond = shooterSecond.getCurrent(CurrentUnit.AMPS)
        return (currentFirst + currentSecond) / 2.0
    }

    fun getTargetRPM(distanceMeters: Double): Double {
        val d = distanceMeters.coerceIn(0.5, 3.0)

        val table = listOf(
            0.5 to 1700.0,
            1.0 to 1800.0,
            1.5 to 1900.0,
            2.0 to 2050.0,
            2.5 to 2200.0,
            3.0 to 2350.0
        )

        for (i in 0 until table.size - 1) {
            val (d1, rpm1) = table[i]
            val (d2, rpm2) = table[i + 1]
            if (d in d1..d2) {
                val t = (d - d1) / (d2 - d1)
                return rpm1 + t * (rpm2 - rpm1)
            }
        }

        return table.last().second
    }

    fun powerToRPM(power: Double): Double {
        val p = power.coerceIn(-1.0, 1.0)
        return if (p >= 0.0) {
            p * maxRPMForward
        } else {
            p * maxRPMBackward
        }
    }

    fun calibrateMaxRPM() {
        maxRPMForward = shooterFirst.velocity * 60.0 / shooterFirst.motorType.ticksPerRev
        maxRPMBackward = -maxRPMForward // reverse is symmetric here; adjust if needed
    }
}