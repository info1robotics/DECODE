package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.roadrunner.TankDrive.Params

object Controller {
    enum class State{ UNKNOWN,
        INIT,
        VISION,
        LOCKED
    }
    lateinit var state:State

    fun init(hardwareMap: HardwareMap) {
        Drivetrain.init(hardwareMap)
        state = State.INIT
    }

    fun setInit()
    {
        state = State.INIT
    }

    fun setInitAuto()
    {
        state = State.INIT
    }

    fun setVision()
    {
        state = State.VISION
    }
    fun setLock()
    {

    }


}