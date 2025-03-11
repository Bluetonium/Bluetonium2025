package frc.robot.subsystems.mechanisms.elevator;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.util.Units;
import frc.utils.sim.LinearConfig;

public final class ElevatorConstants {
    // Motor
    public static final int ELEVATOR_MOTOR_CAN_ID = 14; // find out once more :)
    public static final NeutralModeValue ELEVATOR_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    // PID and motion magic
    public static final double kP = 1.6141;
    public static final double kI = 0;
    public static final double kD = 0.19623;
    public static final double kS = 0.068232;
    public static final double kV = 0.12429;
    public static final double kA = 0.0047557;
    public static final double kG = 0.2052;
    public static final double CRUISE_VELOCITY = 200;
    public static final double ACCELERATION = 100;

    // safety
    public static final double POSITION_TOLERANCE = 0.5; // bear in mind that this is used to determine if it is safe
                                                         // rather than actually move the elevator itself

    // Physical stuff
    public static final double MAX_EXTENSION = 34.5;
    public static final double LOW_POSITION = 0.0;
    public static final double HIGH_POSITION = MAX_EXTENSION * 0.999;
    public static final double GEAR_RATIO = 20;
    public static final double SPROCKET_SIZE = Units.inchesToMeters(1.87 / 2);
    public static final double END_GEAR_RATIO = Math.PI * SPROCKET_SIZE * 2 * GEAR_RATIO;
    public static final double MOUNTING_ANGLE = Math.toRadians(75);

    // Positions
    public static double[] SCORING_POSITIONS = { 0, HIGH_POSITION / 4, HIGH_POSITION / 2, HIGH_POSITION };// L1-L4

    public static enum ElevatorPositions {
        HOME(1),
        SETUPDEEPHANG(8),
        DEEPHANG(12);

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