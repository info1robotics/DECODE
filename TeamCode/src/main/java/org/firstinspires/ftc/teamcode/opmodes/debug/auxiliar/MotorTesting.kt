package org.firstinspires.ftc.teamcode.opmodes.debug

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.common.Log

@Config
@TeleOp
class MotorTesting: LinearOpMode() {
    companion object {
        @JvmField
        var motorPower = 0.0

        @JvmField
        var encoder = false

        @JvmField
        var motor = "motor"
    }

    override fun runOpMode() {
        val log = Log(telemetry)
        val motor = hardwareMap.dcMotor.get(motor)

        if (encoder) {
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }

        waitForStart()
        while (opModeIsActive() && !isStopRequested) {

            motor.power = motorPower


            log.add("Position", motor.currentPosition)
            log.tick()
        }
    }
}