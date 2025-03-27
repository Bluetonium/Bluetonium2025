package frc.robot.subsystems.mechanisms.outtake;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.utils.sim.RollerConfig;

public class OuttakeConstant {
        public static final int OUTTAKE_MOTOR_CAN_ID = 16; // we GOTTA figure it out!!!
        public static final NeutralModeValue OUTTAKE_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more
                                                                                                  // :)
        public static final int CORAL_SENSOR_CHANNEL = 1;
        public static final CurrentLimitsConfigs CURRENT_LIMITS = new CurrentLimitsConfigs()
                        .withStatorCurrentLimit(70)
                        .withSupplyCurrentLimit(40);

        public static final double GEAR_RATIO = 5;
        // stuff
        public static final double OUTTAKE_VELOCITY = 20;
        public static final double INTAKE_VELOCITY = 5;
        public static final double ACCELERATION = 100;
        public static final double EJECTION_DELAY = 1;// delay between not sensing a coral and stopping ejecting

        // PID (we'll figure it out eventually!!!)
        public static final double kP = 0.69993;
        public static final double kI = 0;
        public static final double kD = 0;

        public static final double kS = 0.17299;
        public static final double kV = 0.47682;
        public static final double kA = 0.027378;

        public static final RollerConfig ROLLER_SIM_CONFIG = new RollerConfig(4)
                        .setPosition(1.341, .35);
}
