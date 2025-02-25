package frc.robot.subsystems.mechanisms.coralIntake;

import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.RobotContainer;
import frc.utils.sim.RollerConfig;

public class CoralIntakeConstants {
    public static final int CORAL_INTAKE_MOTOR_CAN_ID = 19; // we GOTTA figure it out!!!

    // PID (we'll figure it out eventually!!!)
    
    public static final double kP = 100;
    public static final double kI = 0;
    public static final double kD = 100;
    public static final NeutralModeValue CORAL_INTAKE_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)
    public static final double acceleration = 10;

    // Mount to arm, not elevator! :D
    public static final RollerConfig SIM_CONFIG = new RollerConfig(15)
    .setMount(RobotContainer.getElevator().getSim());
}
