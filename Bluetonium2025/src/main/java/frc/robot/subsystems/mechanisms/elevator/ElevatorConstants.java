package frc.robot.subsystems.mechanisms.elevator;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.util.Units;
import frc.utils.sim.LinearConfig;

public final class ElevatorConstants {
    // Motor
    public static final int ELEVATOR_MOTOR_CAN_ID = 14; // find out once more :)
    public static final NeutralModeValue ELEVATOR_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    // PID
    public static final double kP = 5.1575; // THIS SEEMS VERY WRONG (TODO: probably change this...)
    public static final double kI = 0;
    public static final double kD = 0.049979;
    public static final double kS = 0.13066;
    public static final double kV = 0.11485;
    public static final double kA = 0.00081861;
    // Physical stuff
    public static final double MAX_EXTENSION = 32;
    public static final double LOW_POSITION = 0.0;
    public static final double HIGH_POSITION = 48 * 0.999;
    public static final double GEAR_RATIO = 1;
    public static final double SPROCKET_SIZE = Units.inchesToMeters(1.87 / 2);
    public static final double END_GEAR_RATIO = Math.PI * SPROCKET_SIZE * 2 * GEAR_RATIO;
    public static final double MOUNTING_ANGLE = Math.toRadians(75);

    // Positions
    public static double[] SCORING_POSITIONS = { 0, HIGH_POSITION / 4, HIGH_POSITION / 2, HIGH_POSITION };// L1-L4

    public static enum ElevatorPositions {
        HOME(1),
        L1(8),
        L2(16),
        L3(24),
        L4(32);

        public final double rotations;
        public final double inches;

        private ElevatorPositions(double inches) {
            this.rotations = inches * END_GEAR_RATIO;
            this.inches = inches;
        }
    }

    // SIM
    public static final LinearConfig SIM_CONFIG = new LinearConfig(.8, 0.35, GEAR_RATIO, SPROCKET_SIZE)
            .setAngle(Math.toDegrees(MOUNTING_ANGLE) + 30)
            .setMovingLength(36.5)
            .setStaticLength(36.5)
            .setMaxHeight(MAX_EXTENSION);

}