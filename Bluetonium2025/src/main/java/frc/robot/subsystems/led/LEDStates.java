package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.led.LEDConstants.Animations;

public class LEDStates {
    private static LED leds = RobotContainer.getLeds();

    public static void setupStates() {
        RobotStates.endGame.onTrue(leds.setAnimation(Animations.END_GAME));

        RobotStates.teleop.onTrue(leds.setAnimation(Animations.TELEOP));
        RobotStates.disabled.and(RobotStates.Estopped.negate()).onTrue(leds.setAnimation(Animations.DISABLED));
        RobotStates.Estopped.onTrue(leds.setAnimation(Animations.ESTOPPED));
        RobotStates.autoMode.onTrue(leds.setAnimation(Animations.AUTON));
        RobotStates.dsAttached.onFalse(leds.setAnimation((Animations.DISCONNECTED)));
    }
}
