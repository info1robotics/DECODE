package org.firstinspires.ftc.teamcode.opmodes.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.Log
import org.firstinspires.ftc.teamcode.subsystems.Turret
import org.firstinspires.ftc.teamcode.subsystems.extra.Limelight

@TeleOp
class
TurretTesting : LinearOpMode() {
    lateinit var log: Log
    override fun runOpMode() {
        Limelight.init(hardwareMap)
        Turret.init(hardwareMap)

        Limelight.start()
        Limelight.changePipeline(0)
        log = Log(this.telemetry)
        waitForStart()

        while (opModeIsActive()) {
            Turret.aimAtTarget()

            log.add("Tx",Limelight.getTx().toString())
            log.add("Servo Left Power", Turret.servoLeft.power)
            log.add("Servo Right Power", Turret.servoRight.power)
            log.tick()
        }
    }
}