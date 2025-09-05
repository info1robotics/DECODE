package org.firstinspires.ftc.teamcode.subsystems.extra

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import android.util.Log

object Limelight {
    lateinit var limelight: Limelight3A

    private var previousList = mutableListOf<Double>()

    fun init(hardwareMap: HardwareMap) {
        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
        limelight.pipelineSwitch(0)
    }

    fun start() {
        limelight.start()
    }

    fun stop() {
        limelight.stop()
    }

    fun saveSnapshot() {
        limelight.captureSnapshot("cache-${System.currentTimeMillis()}")
    }
}