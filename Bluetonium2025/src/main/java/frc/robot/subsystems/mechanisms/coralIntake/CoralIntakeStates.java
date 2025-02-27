package frc.robot.subsystems.mechanisms.coralIntake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class CoralIntakeStates {
    private static CoralIntake coralIntake = RobotContainer.getCoralIntake();

    public static void setStates() {
        RobotStates.intakeCoral.whileTrue(spinForward());
        RobotStates.intakeEjectCoral.whileTrue(spinBackwards());
    }

    private static Command spinForward() {
        return coralIntake.setIntakeVelocity(2);
    }

    private static Command spinBackwards() {
        return coralIntake.setIntakeVelocity(-2);
    }
}
