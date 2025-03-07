package frc.robot.subsystems.mechanisms.elevator;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants.ElevatorPositions;

public class ElevatorStates {
    private static Elevator elevator = RobotContainer.getElevator();
    public static DoubleSupplier elevatorPosition;

    public static void setStates() {
        RobotStates.coralPassOff.onTrue(coralPassOff());
        RobotStates.L2.onTrue(l2());
        RobotStates.L3.onTrue(l3());
        RobotStates.L4.onTrue(l4());
        RobotStates.Home.onTrue(home());
        RobotStates.STOP.onTrue(stop());

        elevatorPosition = elevator::getPosition;
    }

    private static Command coralPassOff() {
        return elevator.checkArmAndMove(ElevatorPositions.CORAL_PASS_OFF, ArmPositions.CORAL_PASS_OFF)
                .withName("Elevator.L1");
    }

    private static Command l2() {
        return elevator.checkArmAndMove(ElevatorPositions.L2, ArmPositions.L2).withName("Elevator.l2");
    }

    private static Command l3() {
        return elevator.checkArmAndMove(ElevatorPositions.L3, ArmPositions.L3).withName("Elevator.l3");
    }

    private static Command l4() {
        return elevator.checkArmAndMove(ElevatorPositions.L4, ArmPositions.L4).withName("Elevator.l4");
    }

    private static Command home() {
        return elevator.checkArmAndMove(ElevatorPositions.HOME, ArmPositions.HOME).withName("Going to Home");
    }

    private static Command stop() {
        return elevator.stopEverything();
    }
    // sysID

}
