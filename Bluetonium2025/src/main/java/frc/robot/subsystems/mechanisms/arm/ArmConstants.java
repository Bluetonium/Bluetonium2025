package frc.robot.subsystems.mechanisms.arm;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.robot.RobotContainer;
import frc.utils.sim.ArmConfig;

public class ArmConstants {
    public static final int ARM_MOTOR_CAN_ID = 19; // we GOTTA figure it out!!!

    // PID (we'll figure it out eventually!!!)
    
    public static final double kP = 100;
    public static final double kI = 0;
    public static final double kD = 100;
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    public static final ArmConfig SIM_CONFIG = new ArmConfig( // TODO: change this shiet
    0.81,
    0.35,
    1.0,
    1.0,
    1.0,
    180.0,
    45.0
    ).setMount(RobotContainer.getElevator().getSim(),false);
}
