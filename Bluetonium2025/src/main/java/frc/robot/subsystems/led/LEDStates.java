package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class LEDStates {
    private static Trigger panicMode;
    private static LED leds = RobotContainer.getLeds();

    public static void setupStates() {
        panicMode = new Trigger(() -> false);
        panicMode.whileTrue(leds.setAnimation(LEDConstants.PANIC_MODE_ANIMATION));

        RobotStates.teleop.whileTrue(leds.setAnimation(LEDConstants.TELEOP_ANIMATION));
        RobotStates.disabled.whileTrue(leds.setAnimation(LEDConstants.DISABLED_ANIMATION));
        // RobotStates.autoMode.whileTrue(leds.setAnimation(LEDConstants.))
    }
}
