package frc.robot.subsystems.led;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.led.LEDConstants.Animations;

public class LEDStates {
    private static LED leds = RobotContainer.getLeds();

    public static void setupStates() {
        RobotStates.endGame.onTrue(leds.setAnimation(Animations.END_GAME));
        RobotStates.teleop.onTrue(leds.setAnimation(Animations.TELEOP));
        RobotStates.disabled.onTrue(leds.setAnimation(Animations.DISABLED));
        RobotStates.Estopped.onTrue(leds.setAnimation(Animations.ESTOPPED));

    }
}
