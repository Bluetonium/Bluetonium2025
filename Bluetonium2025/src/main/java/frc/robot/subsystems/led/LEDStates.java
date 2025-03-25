package frc.robot.subsystems.led;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class LEDStates {
    private static LED leds = RobotContainer.getLeds();

    public static void setupStates() {
        RobotStates.teleop.onTrue(leds.setAnimation(LEDConstants.TELEOP_ANIMATION, "LED.Teleop"));
        RobotStates.disabled.onTrue(leds.setAnimation(LEDConstants.DISABLED_ANIMATION, "LED.Disabled"));
        RobotStates.endGame.onTrue(leds.setAnimation(LEDConstants.END_GAME_ANIMATION,
                "LED.Endgame"));

    }
}
