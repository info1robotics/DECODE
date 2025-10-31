package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.max

object Shooter {
    lateinit var shooterFirst: DcMotorEx
    private lateinit var shooterSecond: DcMotorEx
    private lateinit var voltageSensor: VoltageSensor

    private const val TICKS_PER_REV = 28.0
    const val MAX_RPM = 6000.0

    // 🔧 PIDF constants — tune these experimentally
    private var kP = 0.0015
    private var kI = 0.0001
    private var kD = 0.0002
    private var kF = 12.0 / (MAX_RPM * TICKS_PER_REV / 60.0) // Feedforward based on max voltage

    private val distanceRpmTable = listOf(
        0.5 to 3120.0,
        1.0 to 3300.0,
        1.5 to 3650.0,
        2.0 to 3900.0,
        2.5 to 4000.0,
        3.0 to 4100.0
    )

    fun init(hardwareMap: HardwareMap) {
        shooterFirst = hardwareMap.get(DcMotorEx::class.java, "motorShooterFirst")
        shooterSecond = hardwareMap.get(DcMotorEx::class.java, "motorShooterSecond")

        shooterFirst.direction = DcMotorSimple.Direction.REVERSE
        shooterSecond.direction = DcMotorSimple.Direction.REVERSE

        val motorConfig = shooterFirst.motorType.clone()
        motorConfig.achieveableMaxRPMFraction = 1.0
        shooterFirst.motorType = motorConfig
        shooterSecond.motorType = motorConfig

        shooterFirst.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooterSecond.mode = DcMotor.RunMode.RUN_USING_ENCODER

        voltageSensor = hardwareMap.voltageSensor.iterator().next()

        // ✅ Apply PIDF coefficients
        setPIDFCoefficients()
    }

    private fun setPIDFCoefficients() {
        val pidf = PIDFCoefficients(kP, kI, kD, kF)
        shooterFirst.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf)
        shooterSecond.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf)
    }

    fun setVelocityFromRPM(rpm: Double) {
        // Adjust F term for current voltage to keep output consistent
        val voltageComp = 12.0 / max(1.0, getVoltage())
        val adjustedF = kF * voltageComp

        val pidf = PIDFCoefficients(kP, kI, kD, adjustedF)
        shooterFirst.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf)
        shooterSecond.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf)

        val targetVelocityTicksPerSec = (rpm * TICKS_PER_REV) / 60.0
        shooterFirst.velocity = targetVelocityTicksPerSec
        shooterSecond.velocity = targetVelocityTicksPerSec
    }

    fun getCurrentRPM(): Double {
        val currentTicksPerSec = shooterFirst.velocity
        return (currentTicksPerSec / TICKS_PER_REV) * 60.0
    }

    fun stop() {
        shooterFirst.power = 0.0
        shooterSecond.power = 0.0
    }

    fun setPower(power: Double) {
        shooterFirst.power = power
        shooterSecond.power = power
    }

    fun getPower(): Double = shooterFirst.power

    fun getVoltage(): Double = voltageSensor.voltage

    fun getAverageCurrent(): Double {
        val current1 = shooterFirst.getCurrent(CurrentUnit.AMPS)
        val current2 = shooterSecond.getCurrent(CurrentUnit.AMPS)
        return (current1 + current2) / 2.0
    }

    fun getRpmForDistance(distance: Double): Double {
        val clampedDistance = distance.coerceIn(
            distanceRpmTable.first().first,
            distanceRpmTable.last().first
        )

        val lower = distanceRpmTable.lastOrNull { it.first <= clampedDistance } ?: distanceRpmTable.first()
        val upper = distanceRpmTable.firstOrNull { it.first >= clampedDistance } ?: distanceRpmTable.last()

        return if (lower == upper) {
            lower.second
        } else {
            val (d1, rpm1) = lower
            val (d2, rpm2) = upper
            rpm1 + (clampedDistance - d1) * (rpm2 - rpm1) / (d2 - d1)
        }
    }

    fun setVelocityForDistance(distance: Double) {
        val targetRpm = getRpmForDistance(distance)
        setVelocityFromRPM(targetRpm)
    }
}