package frc.robot.subsystems.mechanisms.outtake;

import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.RobotContainer;
import frc.utils.sim.RollerConfig;

public class OuttakeConstant {
    public static final int OUTTAKE_MOTOR_CAN_ID = 17; // we GOTTA figure it out!!!
    public static final NeutralModeValue OUTTAKE_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once
                                                                                              // more :)
    public static final int CORAL_SENSOR_CHANNEL = 1;

    // stuff
    public static final double INTAKE_SPEED = 0.3;
    public static final double EJECT_FAST_SPEED = 0.6;
    public static final double EJECT_SLOW_SPEED = 0.3;

    public static final double acceleration = 10;
    public static final double ejectionDelay = 2;// delay between not sensing a coral and stopping ejecting

    // PID (we'll figure it out eventually!!!)
    public static final double kP = 5;
    public static final double kI = 0;
    public static final double kD = 0;

    public static final RollerConfig ROLLER_SIM_CONFIG = new RollerConfig(4)
            .setMount(RobotContainer.getArm().getArmSim())
            .setPosition(1.341, .35);
}
