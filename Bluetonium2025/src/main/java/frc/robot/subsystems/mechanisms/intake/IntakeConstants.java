package frc.robot.subsystems.mechanisms.intake;

import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.util.Units;
import frc.robot.RobotContainer;
import frc.utils.sim.ArmConfig;

public class IntakeConstants {
    public static final int INTAKE_ARM_MOTOR_CAN_ID = 19;
    public static final int INTAKE_ROLLER_MOTOR_CAN_ID = 20;
    public static final NeutralModeValue INTAKE_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake;
    public static final int CORAL_SENSOR_CHANNEL = 21;

    public static final double runningVelocity = 5;
    public static final double acceleration = 10;
    public static final double ejectionDelay = 2;// delay between not sensing a coral and stopping ejecting

    public static final int ABSOLUTE_ENCODER_CHANNEL = 8;
    public static final double ABSOLUTE_ENCODER_OFFSET = 0.1385748;// units of rotatations

    public static final double kP = 1400;
    public static final double kI = 0;
    public static final double kD = 160;
    public static final double kV = 0;
    public static final double kS = 0.4;
    public static final double kA = 0.002;
    public static final double kG = 7;

    // safety
    public static final double POSITION_TOLERANCE = Math.toRadians(5); // bear in mind that this is used to determine if
    // it is safe
    // rather than actually move the elevator itself

    // intake physical properties
    public static final double INTAKE_LENGTH = 20.90551;
    public static final double GEAR_RATIO = 100;
    public static final double ENCODER_CPR = 42;
    public static final double MIN_ANGLE = 0; //not sure
    public static final double MAX_ANGLE = 140; //not sure
    public static final double MAX_ANGLE_TO_MOVE_ELEVATOR = 100;// degrees

    // positions
    public static enum IntakePositions {
        CORAL_PASS_OFF(-45),
        L2(10),
        L3(10),
        L4(110),
        TRANSITION_STATE(0),
        HOME(90);

        public double rotations;
        public double angle;

        private IntakePositions(double angle) {
            rotations = Units.degreesToRotations(angle) * GEAR_RATIO;
            this.angle = Math.toRadians(angle);
        }
    }

    public static final ArmConfig SIM_CONFIG = new ArmConfig(
            0.81,
            0.35,
            GEAR_RATIO,
            Units.inchesToMeters(INTAKE_LENGTH),
            MIN_ANGLE,
            MAX_ANGLE,
            0).setMount(RobotContainer.getElevator().getSim(), true);
}
