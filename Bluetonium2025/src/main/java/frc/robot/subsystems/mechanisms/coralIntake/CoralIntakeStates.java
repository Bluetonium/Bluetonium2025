package frc.robot.subsystems.mechanisms.coralIntake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class CoralIntakeStates {
    private static CoralIntake coralIntake = RobotContainer.getCoralIntake();

    public static void setStates() {
        //RobotStates.pos1.whileTrue(pos1());
        //RobotStates.pos2.whileTrue(pos2());
    }

    //private static Command pos1() {
    //    return arm.setArmPosition(0).withName("Arm.pos1");
    //}

    //private static Command pos2() {
    //    return arm.setArmPosition(0.1).withName("Arm.pos2");
    //}
}
