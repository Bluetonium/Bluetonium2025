package frc.robot.subsystems.mechanisms.arm;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.util.Units;
import frc.robot.RobotContainer;
import frc.utils.sim.ArmConfig;

public class ArmConstants {
    public static final int ARM_MOTOR_CAN_ID = 19; // we GOTTA figure it out!!!
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :)

    // PID (we'll figure it out eventually!!!)
    public static final double kP = 1400;
    public static final double kI = 0;
    public static final double kD = 160;
    public static final double kV = 0;
    public static final double kS = 0.4;
    public static final double kA = 0.002;
    public static final double kG = 7;

    // safety
    public static final double POSITION_TOLERANCE = 0.5; //bear in mind that this is used to determine if it is safe rather than actually move the elevator itself


    // arm physical properties
    public static final double ARM_LENGTH = 20.90551;
    public static final double GEAR_RATIO = 22;
    public static final double MIN_ANGLE = -90;
    public static final double MAX_ANGLE = 120;

    // positions
    public static enum ArmPositions {
        L1(0),
        L2(0),
        L3(0),
        L4(100),
        HOME(90);

        public double rotations;
        public double angle;

        private ArmPositions(double angle) {
            rotations = Units.degreesToRotations(angle) * GEAR_RATIO;
            this.angle = Math.toRadians(angle);
        }
    }

    public static final ArmConfig SIM_CONFIG = new ArmConfig( // TODO: change this shiet
            0.81,
            0.35,
            GEAR_RATIO,
            Units.inchesToMeters(ARM_LENGTH),
            MIN_ANGLE,
            MAX_ANGLE,
            0).setMount(RobotContainer.getElevator().getSim(), true);
}
