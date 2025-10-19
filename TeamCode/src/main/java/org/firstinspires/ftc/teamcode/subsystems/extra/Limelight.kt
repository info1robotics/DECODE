package org.firstinspires.ftc.teamcode.subsystems.extra

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.robotcore.hardware.HardwareMap
import android.util.Log
import org.firstinspires.ftc.teamcode.common.AprilTags
import org.firstinspires.ftc.teamcode.enums.AutoStartPos
import org.firstinspires.ftc.teamcode.opmodes.AutoBase

object Limelight {
    private lateinit var limelight: Limelight3A

    val allianceTag = AprilTags.BLUE//TODO change the alliance colour

    fun init(hardwareMap: HardwareMap, pipelineIndex: Int = 0) {
        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
        limelight.pipelineSwitch(pipelineIndex)
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

    fun changePipeline(newIndex: Int) {
        try {
            limelight.pipelineSwitch(newIndex)
            Log.i("Limelight", "Switched to pipeline $newIndex")
        } catch (e: Exception) {
            Log.e("Limelight", "Failed to switch pipeline: ${e.message}")
        }
    }

    /**
     * Returns the detected AprilTag (fiducial) ID if any tag is seen, or null otherwise.
     */
    fun getAprilTagId(): Int? {
        val result: LLResult? = limelight.latestResult
        if (result == null || !result.isValid) {
            return null
        }

        // Use getFiducialResults() per SDK spec
        val fiducials = result.fiducialResults
        if (fiducials.isNotEmpty()) {
            val fid = fiducials[0]
            return fid.fiducialId
        }

        return null
    }
    fun getTx(): Double? {
        val result = limelight.latestResult ?: return null
        return if (result.isValid) result.tx else null
    }

    fun getTy(): Double? {
        val result = limelight.latestResult ?: return null
        return if (result.isValid) result.ty else null
    }


    fun isPPG() = getAprilTagId() == AprilTags.PPG

    fun isPGP() = getAprilTagId() == AprilTags.PGP

    fun isGPP() = getAprilTagId() == AprilTags.PPG

    fun isAlliance() = getAprilTagId() == allianceTag

}