package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import frc.robot.constants.SensorConstants;
import frc.robot.constants.SwerveConstants;

public final class CTREConfigs {
    public static final TalonFXConfiguration SWERVE_ANGLE_CONFIG = new TalonFXConfiguration();

    public static final TalonFXConfiguration SWERVE_DRIVE_CONFIG = new TalonFXConfiguration();
    public static final CANcoderConfiguration SWERVE_CANCODER_CONFIG = new CANcoderConfiguration();

    static {
        /** Swerve CANCoder Configuration */
        SWERVE_CANCODER_CONFIG.MagnetSensor.SensorDirection = SensorConstants.CANCODER_INVERT;

        /** Swerve Angle Motor Configurations */
        /* Motor Inverts and Neutral Mode */
        SWERVE_ANGLE_CONFIG.MotorOutput.Inverted = SwerveConstants.Swerve.ANGLE_MOTOR_INVERT;
        SWERVE_ANGLE_CONFIG.MotorOutput.NeutralMode = SwerveConstants.Swerve.ANGLE_NUETRAL_MODE;

        /* Gear Ratio and Wrapping Config */
        SWERVE_ANGLE_CONFIG.Feedback.SensorToMechanismRatio = SwerveConstants.Swerve.ANGLE_GEAR_RATIO;
        SWERVE_ANGLE_CONFIG.ClosedLoopGeneral.ContinuousWrap = true;

        /* Current Limiting */
        SWERVE_ANGLE_CONFIG.CurrentLimits.SupplyCurrentLimitEnable = SwerveConstants.Swerve.ANGLE_ENABLE_CURRENT_LIMIT;
        SWERVE_ANGLE_CONFIG.CurrentLimits.SupplyCurrentLimit = SwerveConstants.Swerve.ANGLE_CURRENT_LIMIT;
        SWERVE_ANGLE_CONFIG.CurrentLimits.SupplyCurrentThreshold = SwerveConstants.Swerve.ANGLE_CURRENT_THRESHOLD;
        SWERVE_ANGLE_CONFIG.CurrentLimits.SupplyTimeThreshold = SwerveConstants.Swerve.ANGLE_CURRENT_THRESHOLD_TIME;

        /* PID Config */
        SWERVE_ANGLE_CONFIG.Slot0.kP = SwerveConstants.Swerve.ANGLE_KP;
        SWERVE_ANGLE_CONFIG.Slot0.kI = SwerveConstants.Swerve.ANGLE_KI;
        SWERVE_ANGLE_CONFIG.Slot0.kD = SwerveConstants.Swerve.ANGLE_KD;

        /** Swerve Drive Motor Configuration */
        /* Motor Inverts and Neutral Mode */
        SWERVE_DRIVE_CONFIG.MotorOutput.Inverted = SwerveConstants.Swerve.DRIVE_MOTOR_INVERT;
        SWERVE_DRIVE_CONFIG.MotorOutput.NeutralMode = SwerveConstants.Swerve.DRIVE_NEUTRAL_MODE;

        /* Gear Ratio Config */
        SWERVE_DRIVE_CONFIG.Feedback.SensorToMechanismRatio = SwerveConstants.Swerve.DRIVE_GEAR_RATIO;

        /* Current Limiting */
        SWERVE_DRIVE_CONFIG.CurrentLimits.SupplyCurrentLimitEnable = SwerveConstants.Swerve.DRIVE_ENABLE_CURRENT_LIMIT;
        SWERVE_DRIVE_CONFIG.CurrentLimits.SupplyCurrentLimit = SwerveConstants.Swerve.DRIVE_CURRENT_LIMIT;
        SWERVE_DRIVE_CONFIG.CurrentLimits.SupplyCurrentThreshold = SwerveConstants.Swerve.DRIVE_CURRENT_THRESHOLD;
        SWERVE_DRIVE_CONFIG.CurrentLimits.SupplyTimeThreshold = SwerveConstants.Swerve.DRIVE_CURRENT_THRESHOLD_TIME;

        /* PID Config */
        SWERVE_DRIVE_CONFIG.Slot0.kP = SwerveConstants.Swerve.DRIVE_KP;
        SWERVE_DRIVE_CONFIG.Slot0.kI = SwerveConstants.Swerve.DRIVE_KI;
        SWERVE_DRIVE_CONFIG.Slot0.kD = SwerveConstants.Swerve.DRIVE_KD;

        /* Open and Closed Loop Ramping */
        SWERVE_DRIVE_CONFIG.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = SwerveConstants.Swerve.OPEN_LOOP_RAMP;
        SWERVE_DRIVE_CONFIG.OpenLoopRamps.VoltageOpenLoopRampPeriod = SwerveConstants.Swerve.OPEN_LOOP_RAMP;

        SWERVE_DRIVE_CONFIG.ClosedLoopRamps.DutyCycleClosedLoopRampPeriod = SwerveConstants.Swerve.CLOSED_LOOP_RAMP;
        SWERVE_DRIVE_CONFIG.ClosedLoopRamps.VoltageClosedLoopRampPeriod = SwerveConstants.Swerve.CLOSED_LOOP_RAMP;
    }

    private CTREConfigs() {
    }
}