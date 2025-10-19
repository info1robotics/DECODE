package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoImplEx
import org.firstinspires.ftc.teamcode.subsystems.extra.Limelight
import kotlin.math.absoluteValue
object Turret {

    private const val LEFT_SERVO_NAME = "turretLeft"
    private const val RIGHT_SERVO_NAME = "turretRight"

    lateinit var servoLeft: CRServo
    lateinit var servoRight: CRServo

    private const val KP_X = 0.01
    private const val UPPER_LIMIT = 0.1
    private const val LOWER_LIMIT = -0.1
    fun init(hardwareMap: HardwareMap) {
        servoLeft = hardwareMap.get(CRServo::class.java, LEFT_SERVO_NAME)
        servoRight = hardwareMap.get(CRServo::class.java, RIGHT_SERVO_NAME)
    }

    fun aimAtTarget() {
        val xError = Limelight.getTx()

        val power = if (xError!! < UPPER_LIMIT && xError > LOWER_LIMIT ) {
            0.0
        } else {
            (xError * KP_X).coerceIn(-1.0, 1.0)
        }

        servoLeft.power = power
        servoRight.power = power
    }
}