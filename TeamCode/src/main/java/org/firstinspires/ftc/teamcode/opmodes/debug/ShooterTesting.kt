package org.firstinspires.ftc.teamcode.opmodes.debug

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.common.Log
import org.firstinspires.ftc.teamcode.subsystems.Shooter

@TeleOp(name = "Shooter RPM Test", group = "Debug")
@Config
class ShooterTesting : LinearOpMode() {
    companion object {
        @JvmField
        var rpm = 0.0 // Set this in FTC Dashboard
    }

    private lateinit var log: Log

    override fun runOpMode() {
        Shooter.init(hardwareMap)
        log = Log(telemetry)

        waitForStart()

        while (opModeIsActive()) {
            Shooter.setVelocityFromRPM(rpm)

            val currentRPM = Shooter.getCurrentRPM()
            val voltage = Shooter.getVoltage()

            log.add("Target RPM", rpm)
            log.add("Current RPM", currentRPM)
            log.add("Shooter Power", Shooter.getPower())
            log.add("Voltage", voltage)
            log.tick()
        }

        Shooter.stop()
    }
}