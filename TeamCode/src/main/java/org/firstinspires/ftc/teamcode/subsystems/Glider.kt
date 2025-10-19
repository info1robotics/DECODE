package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.PwmControl.PwmRange
import com.qualcomm.robotcore.hardware.ServoImplEx

object Glider {

    var offset = 0.0
    var HIGHER_LIMIT=0.69
    var LOWER_LIMIT=0.0
    private lateinit var servoGlider: ServoImplEx

    fun init(hardwareMap: HardwareMap) {
        servoGlider = hardwareMap.get(ServoImplEx::class.java, "servoGlider")
        servoGlider.pwmRange = PwmRange(500.0, 2500.0)
    }

    fun setPosition(position: Double) {

        servoGlider.position = position.coerceIn(LOWER_LIMIT, HIGHER_LIMIT)
    }

    fun getPosition(): Double {
        return servoGlider.position
    }
}