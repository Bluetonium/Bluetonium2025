package frc.robot.subsystems.mechanisms.shoulder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class ShoulderStates {
    private static Shoulder shoulder = RobotContainer.getShoulder();

    public static void setStates() {
        RobotStates.pos1.whileTrue(scoringPosition());
        RobotStates.pos2.whileTrue(coralPassoffPosition());
    }

    private static Command scoringPosition() {
        return shoulder.setArmAngle(ShoulderConstants.SCORING_POSITION).withName("Arm.scoringPosition");
    }

    private static Command coralPassoffPosition() {
        return shoulder.setArmAngle(ShoulderConstants.CORAL_PASSOFF_POSITION).withName("Arm.coralPassoffPosition");
    }
}
