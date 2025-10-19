package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

object Intake {

    lateinit var intake:DcMotor
    private lateinit var sensorIntake: RevColorSensorV3
    private var colour = "Red"//TODO change the colour that you want to collect

    fun init(hardwareMap: HardwareMap) {
        intake = hardwareMap.get(DcMotor::class.java, "motorIntake")
        intake.direction = DcMotorSimple.Direction.REVERSE
        val motorConfigurationType = intake.motorType.clone()
        motorConfigurationType.achieveableMaxRPMFraction = 1.0
        intake.motorType = motorConfigurationType

        sensorIntake = hardwareMap.get(RevColorSensorV3::class.java, "sensorIntake")

    }

    fun setPower(power: Double) {
        intake.power = power
    }

    fun stop() {
        intake.power = 0.0
    }

    fun reverse() {
        intake.power = -0.8
    }

    fun take() {
        intake.power = 0.8
    }


}