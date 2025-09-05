package org.firstinspires.ftc.teamcode.opmodes.debug.auxiliar

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "OpMode Test", group = "Test")
class OpModeTesting : LinearOpMode() {
    override fun runOpMode() {
        telemetry.addData("Status ", "Initialized")
        telemetry.update()

        waitForStart()

        telemetry.addData("Status", "Started")
        telemetry.update()

        while (opModeIsActive()) {
            // Keep telemetry updating if needed
            telemetry.addData("Status", "Running")
            telemetry.update()
        }
    }
}
