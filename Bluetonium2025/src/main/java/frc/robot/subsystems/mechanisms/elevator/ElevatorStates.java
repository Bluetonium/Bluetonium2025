package frc.robot.subsystems.mechanisms.elevator;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants.ElevatorPositions;

public class ElevatorStates {
    private static Elevator elevator = RobotContainer.getElevator();
    public static DoubleSupplier elevatorPosition;

    public static void setupStates() {
        RobotStates.disabled.onTrue(elevator.setCoast(true));
        RobotStates.disabled.negate().onTrue(elevator.setCoast(false).andThen(elevator.holdPosition()));
        RobotStates.home.onTrue(home());
        RobotStates.L1.onTrue(L1());
        RobotStates.L2.onTrue(L2());
        RobotStates.L3.onTrue(L3());
        RobotStates.L4.onTrue(L4());
        RobotStates.intakePosition.onTrue(IntakePosition());
        elevatorPosition = elevator::getPosition;
    }

    private static Command home() {
        return elevator.requestTargetPosition(ElevatorPositions.HOME).withName("Elevator.Home");
    }

    private static Command IntakePosition() {
        return elevator.requestTargetPosition(ElevatorPositions.INTAKE).withName("Elevator.Intake");
    }

    private static Command L1() {
        return elevator.requestTargetPosition(ElevatorPositions.L1)
                .withName("Elevator.L1");
    }

    private static Command L2() {
        return elevator.requestTargetPosition(ElevatorPositions.L2)
                .withName("Elevator.L2");
    }

    private static Command L3() {
        return elevator.requestTargetPosition(ElevatorPositions.L3)
                .withName("Elevator.L3");
    }

    private static Command L4() {
        return elevator.requestTargetPosition(ElevatorPositions.L4)
                .withName("Elevator.L4");
    }

}
