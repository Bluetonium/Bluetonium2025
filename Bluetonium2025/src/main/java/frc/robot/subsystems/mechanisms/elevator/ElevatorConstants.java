package frc.robot.subsystems.mechanisms.elevator;

import com.ctre.phoenix6.signals.NeutralModeValue;

public final class ElevatorConstants {
    // Motor
    public static final int ARM_MOTOR_CAN_ID = 20; // find out once more :)
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    // PID
    public static final double kP = 5.1575; // THIS SEEMS VERY WRONG (TODO: probably change this...)
    public static final double kI = 0;
    public static final double kD = 0.049979;
    public static final double kS = 0.13066;
    public static final double kV = 0.11485;
    public static final double kA = 0.00081861;
    // Physical stuff
    public static final double LOW_POSITION = 0.0;
    public static final double HIGH_POSITION = 10.0; // find out what fucking value to do
    public static final double RANGE_OF_ROTATIONS = 87.2727;

    // Positions
    public static double[] SCORING_POSITIONS = { 0, 0, 0, 0 };// L1-L4

}