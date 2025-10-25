package org.firstinspires.ftc.teamcode.opmodes.debug

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.Log
import org.firstinspires.ftc.teamcode.subsystems.Glider
import org.firstinspires.ftc.teamcode.subsystems.Shooter

@TeleOp
@Config
class AutoAimTesting : LinearOpMode() {
    companion object {
        @JvmField
        var distance = 0.0
    }

    private lateinit var log: Log

    override fun runOpMode() {
        Shooter.init(hardwareMap)
        Glider.init(hardwareMap)
        log = Log(telemetry)
        waitForStart()


        while (opModeIsActive()) {
            Shooter.setRPM(Shooter.MIN_MAX_RPM)
            Glider.autoAim(distance)


            log.add("Shooter Power", Shooter.getPower())
            log.tick()
        }

        Shooter.stop()
    }
}