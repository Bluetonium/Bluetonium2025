package frc.robot.constants;

import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.math.util.Units;

public class NeoVortexSwerveConstants {
    private NeoVortexSwerveConstants() {
    }

    public static final double L1 = (8.14 / 1.0);
    /** SDS MK4i - (6.75 : 1) */
    public static final double L2 = (6.75 / 1.0);
    /** SDS MK4i - (6.12 : 1) */
    public static final double L3 = (6.12 / 1.0);

    public static final double CHOSEN_RATIO = L2;

    public static final double WHEEL_DIAMETER = Units.inchesToMeters(4.0);
    public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    public static final double ANGLE_GEAR_RATIO = ((150.0 / 7.0) / 1.0);
    public static final double DRIVE_GEAR_RATIO = CHOSEN_RATIO;
    public static final double ANGLE_KP = 1.0;
    public static final double ANGLE_KI = 0.0;
    public static final double ANGLE_KD = 0.0;
    public static final boolean DRIVE_MOTOR_INVERT = true;
    public static final boolean ANGLE_MOTOR_INVERT = true;
    public static final SensorDirectionValue CAN_CODER_INVERT = SensorDirectionValue.CounterClockwise_Positive;
}
