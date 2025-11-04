package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.opmodes.debug.ShooterTesting
import org.firstinspires.ftc.teamcode.opmodes.debug.ShooterTesting.Companion
import kotlin.math.max
import kotlin.math.min

object Shooter {
    lateinit var shooterFirst: DcMotorEx//6000 gobilda rpm motor, final 8400 after gear ratio(140%)
    private lateinit var shooterSecond: DcMotorEx
    private lateinit var voltageSensor: VoltageSensor

    public  const val MIN_MAX_RPM = 5800
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

        val motorConfigurationType = shooterFirst.motorType.clone()
        motorConfigurationType.achieveableMaxRPMFraction = 1.0
        shooterFirst.motorType = motorConfigurationType
        shooterSecond.motorType = motorConfigurationType

        voltageSensor = hardwareMap.voltageSensor.iterator().next()
    }

    fun setPower(power: Double) {

        shooterFirst.power = power
        shooterSecond.power = power
    }
    fun getPower(): Double {
        return shooterFirst.power
    }

    fun setVelocity(velocity: Double)
    {
        shooterFirst.velocity = velocity
        shooterSecond.velocity = velocity
    }
    fun setRPM(rpm:Double)
    {
        val targetVelocityTicksPerSec = (rpm * 28) / 60.0
        shooterFirst.velocity=targetVelocityTicksPerSec
        shooterSecond.velocity=targetVelocityTicksPerSec
    }
    fun getRPM(): Double {
        val currentTicksPerSec = shooterFirst.velocity
        val currentRPM = (currentTicksPerSec / 28) * 60.0
        return currentRPM
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
    fun getVelocity(): Double {
        return  shooterFirst.velocity
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
        setRPM(targetRpm)
    }

}