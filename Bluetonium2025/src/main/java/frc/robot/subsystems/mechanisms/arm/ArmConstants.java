package frc.robot.subsystems.mechanisms.arm;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.util.Units;
import frc.robot.RobotContainer;
import frc.utils.sim.ArmConfig;

public class ArmConstants {
    public static final int ARM_MOTOR_CAN_ID = 16;
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake;
    public static final int ABSOLUTE_ENCODER_CHANNEL = 8;
    public static final double ABSOLUTE_ENCODER_OFFSET = 0.8216;// units of rotatations

    public static final double kP = 119.16;
    public static final double kI = 0;
    public static final double kD = 38.285;
    public static final double kV = 11.146;
    public static final double kS = 0.19305; // came out as negative :(
    public static final double kA = 0.34718;
    public static final double kG = 0.078246;

    // safety
    public static final double POSITION_TOLERANCE = Math.toRadians(2); // bear in mind that this is used to determine if
    // it is safe
    // rather than actually move the elevator itself

    // arm physical properties
    public static final double ARM_LENGTH = 23.5;
    public static final double GEAR_RATIO = 100;
    public static final double MIN_ANGLE = -90;
    public static final double MAX_ANGLE = 120;
    public static final double MAX_ANGLE_TO_MOVE_ELEVATOR = 83;// degrees

    // positions
    public static enum ArmPositions {
        TRANSITION_STATE(0),
        HOME(0),
        DEEPHANG(45), // TODO: find out lol
        SETUPDEEPHIGH(-90);

        public double rotations;
        public double angle;

        private ArmPositions(double angle) {
            rotations = Units.degreesToRotations(angle);
            this.angle = Math.toRadians(angle);
        }
    }

    public static final ArmConfig SIM_CONFIG = new ArmConfig(
            0.81,
            0.35,
            GEAR_RATIO,
            Units.inchesToMeters(ARM_LENGTH),
            MIN_ANGLE,
            MAX_ANGLE,
            0).setMount(RobotContainer.getElevator().getSim(), true);
}
