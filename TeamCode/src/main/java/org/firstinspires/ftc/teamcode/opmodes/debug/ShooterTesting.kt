package org.firstinspires.ftc.teamcode.opmodes.test

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.Log
import org.firstinspires.ftc.teamcode.subsystems.Glider
import org.firstinspires.ftc.teamcode.subsystems.Shooter
import org.firstinspires.ftc.teamcode.subsystems.Turret
import org.firstinspires.ftc.teamcode.subsystems.extra.Limelight

@TeleOp()
@Config

class ShooterTesting : LinearOpMode() {
    companion object {
        @JvmField
        var power = 0.0


    }
    lateinit var log: Log
    override fun runOpMode() {
        Shooter.init(hardwareMap)
        log = Log(this.telemetry)
        waitForStart()
        Shooter.calibrateMaxRPM()
        while (opModeIsActive()) {
            Shooter.setPower(power)

            log.add("Shooter Power", power)
            log.tick()
        }
    }
}