package frc.robot.constants;

import edu.wpi.first.wpilibj.XboxController;

public class DriverControls {
    public static final double STICK_DEADBAND = 0.1;

    public static class Chassis {
        public static final int TRANSLATION_AXIS = XboxController.Axis.kLeftY.value;
        public static final int STRAFE_AXIS = XboxController.Axis.kLeftX.value;
        public static final int ROTATION_AXIS = XboxController.Axis.kRightX.value;

        private Chassis() {
        }
    }

    private DriverControls() {
    }
}
