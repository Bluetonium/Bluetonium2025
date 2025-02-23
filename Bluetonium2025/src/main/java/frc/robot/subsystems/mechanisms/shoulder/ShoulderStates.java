package frc.robot.subsystems.mechanisms.shoulder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class ShoulderStates {
    private static Shoulder shoulder = RobotContainer.getArm();

    public static void setStates() {
        RobotStates.pos1.whileTrue(pos1());
        RobotStates.pos2.whileTrue(pos2());
    }

    private static Command pos1() {
        return shoulder.setArmPosition(0).withName("Arm.pos1");
    }

    private static Command pos2() {
        return shoulder.setArmPosition(0.1).withName("Arm.pos2");
    }
}
