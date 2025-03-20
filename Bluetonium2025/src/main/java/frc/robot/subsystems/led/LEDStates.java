package frc.robot.subsystems.led;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.swerve.CommandSwerveDrivetrain;

public class LEDStates {
    private static LED leds = RobotContainer.getLeds();

    public static void setupStates() {
        RobotStates.teleop.whileTrue(leds.setAnimation(LEDConstants.TELEOP_ANIMATION));
        RobotStates.disabled.whileTrue(leds.setAnimation(LEDConstants.DISABLED_ANIMATION));
        // RobotStates.autoMode.whileTrue(leds.setAnimation(LEDConstants.))
    }
}
