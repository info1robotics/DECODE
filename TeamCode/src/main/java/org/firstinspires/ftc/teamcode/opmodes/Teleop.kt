package org.firstinspires.ftc.teamcode.opmodes

import android.app.Notification.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.common.ActionQueue
import org.firstinspires.ftc.teamcode.common.GamepadEx
import org.firstinspires.ftc.teamcode.common.Log
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain

class Teleop : LinearOpMode() {
    fun Gamepad.corrected_left_stick_y(): Float = -this.left_stick_y

    private fun handleInputDrivetrain()
    {
        val forwardPower = gamepad1.corrected_left_stick_y().toDouble()
        val strafePower =   gamepad1.left_stick_x.toDouble()
        val primaryRotationPower = gamepad1.right_trigger.toDouble() - gamepad1.left_trigger.toDouble()
        val secondaryRotationPower = gamepad2.right_stick_x.toDouble()
        if(primaryRotationPower != 0.0)
            Drivetrain.driveMecanum(forwardPower, strafePower, primaryRotationPower, 1.0)
        else
            Drivetrain.driveMecanum(forwardPower,strafePower,secondaryRotationPower,1.0)
    }
    override fun runOpMode() {
        val gamepadEx1 = GamepadEx(gamepad1)
        val gamepadEx2 = GamepadEx(gamepad2)
        val actionQueue = ActionQueue()

        val log = Log(telemetry)

        waitForStart()


        while (opModeIsActive() && !isStopRequested) {

            handleInputDrivetrain()

            gamepadEx1.update()
            gamepadEx2.update()
            actionQueue.update()
            log.tick()
        }
    }
}