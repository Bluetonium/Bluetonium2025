package frc.robot.constants;

import edu.wpi.first.wpilibj.XboxController;


public final class Constants {

    public static final class AutonConstants {

        private AutonConstants() {

        }
    }

    public static final class ControllerConstants {
        public static final double STICK_DEADBAND = 0.1;
        public static final int DRIVER_CONTROLLER_PORT = 0;

        private ControllerConstants() {
        }
    }

    public static final class ChassisControls {
        public static final int ZERO_GYRO_BUTTON = XboxController.Button.kA.value;

        public static final int TRANSLATION_AXIS = XboxController.Axis.kLeftY.value;
        public static final int STRAFE_AXIS = XboxController.Axis.kLeftX.value;
        public static final int ROTATION_AXIS = XboxController.Axis.kRightX.value;
        public static final int ROBOT_RELATIVE = XboxController.Button.kLeftBumper.value;
        public static final int FAST_MODE = XboxController.Axis.kLeftTrigger.value;

        private ChassisControls() {
        }
    }

    private Constants() {
    }

}
