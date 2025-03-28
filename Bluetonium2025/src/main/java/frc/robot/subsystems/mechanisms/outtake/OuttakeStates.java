package frc.robot.subsystems.mechanisms.outtake;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class OuttakeStates {
    private static Outtake outtake = RobotContainer.getOuttake();

    public static void setupStates() {
        RobotStates.intake.and(() -> !outtake.hasCoral).whileTrue(outtake.outtakeAccept());
        RobotStates.outtake.and(() -> outtake.hasCoral).whileTrue(outtake.outtakeEject());
    }

}
