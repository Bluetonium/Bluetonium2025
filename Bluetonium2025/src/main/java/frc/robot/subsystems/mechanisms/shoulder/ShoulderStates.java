package frc.robot.subsystems.mechanisms.shoulder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.shoulder.ShoulderConstants.ShoulderPositions;

public class ShoulderStates {
    private static Shoulder shoulder = RobotContainer.getShoulder();

    public static void setStates() {
        RobotStates.pos1.whileTrue(scoringPosition());
        RobotStates.pos2.whileTrue(coralPassoffPosition());
    }

    private static Command scoringPosition() {
        return shoulder.setShoulderPosition(ShoulderPositions.SCORING).withName("Arm.scoringPosition");
    }

    private static Command coralPassoffPosition() {
        return shoulder.setShoulderPosition(ShoulderPositions.CORAL_PASSOFF).withName("Arm.coralPassoffPosition");
    }
}
