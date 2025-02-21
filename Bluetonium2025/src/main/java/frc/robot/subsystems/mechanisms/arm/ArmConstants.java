package frc.robot.subsystems.mechanisms.arm;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.utils.sim.ArmConfig;

public class ArmConstants {
    public static final int ARM_MOTOR_CAN_ID = 19; // we GOTTA figure it out!!!

    // PID (we'll figure it out eventually!!!)
    
    public static final double kP = 1;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    public static final ArmConfig SIM_CONFIG = new ArmConfig( // TODO: change this shiet
    1,
    1,
    1,
    1,
    0,
    180,
    45
    );


}
