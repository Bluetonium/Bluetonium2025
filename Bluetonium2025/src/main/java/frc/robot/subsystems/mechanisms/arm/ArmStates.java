package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class ArmStates {
    private static Arm arm = RobotContainer.getArm();

    public static void setStates() {
        RobotStates.pos1.whileTrue(pos1());
        RobotStates.pos2.whileTrue(pos2());
    }

    private static Command pos1() {
        return arm.setArmPosition(0).withName("Arm.pos1");
    }

    private static Command pos2() {
        return arm.setArmPosition(1).withName("Arm.pos2");
    }
}
