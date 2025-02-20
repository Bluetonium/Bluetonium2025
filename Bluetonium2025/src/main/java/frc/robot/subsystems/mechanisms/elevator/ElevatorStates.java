package frc.robot.subsystems.mechanisms.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class ElevatorStates {
    private static Elevator elevator = RobotContainer.getElevator();

    public static void setStates() {
        RobotStates.L1.whileTrue(l1());
        RobotStates.L2.whileTrue(l2());
        RobotStates.L3.whileTrue(l3());
        RobotStates.L4.whileTrue(l4());
        // RobotStates.Home.whileTrue(home());
    }

    private static Command l1() {
        return elevator.requestTargetPosition(ElevatorConstants.SCORING_POSITIONS[0]).withName("Elevator.l1");
    }

    private static Command l2() {
        return elevator.requestTargetPosition(ElevatorConstants.SCORING_POSITIONS[1]).withName("Elevator.l2");
    }

    private static Command l3() {
        return elevator.requestTargetPosition(ElevatorConstants.SCORING_POSITIONS[2]).withName("Elevator.l3");
    }

    private static Command l4() {
        return elevator.requestTargetPosition(ElevatorConstants.SCORING_POSITIONS[3]).withName("Elevator.l4");
    }

    private static Command home() {
        return elevator.requestTargetPosition(0).withName("Elevator Homing");
    }
}
