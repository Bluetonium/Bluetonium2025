package frc.robot.subsystems.mechanisms.elevator;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.arm.Arm;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants.ElevatorPositions;

public class ElevatorStates {
    private static Elevator elevator = RobotContainer.getElevator();
    private static Arm arm = RobotContainer.getArm();
    public static DoubleSupplier elevatorPosition;

    public static void setStates() {

        RobotStates.Home.onTrue(home());
        RobotStates.STOP.onTrue(stop());
        RobotStates.deepHangSequence.onTrue(deepHangSequence());
        RobotStates.setupDeepHang.onTrue(setupDeepHang());
        RobotStates.moveElevatorDeephang.onTrue(moveElevatorDeephang());
        RobotStates.runningArm.onTrue(runningArm());
        elevatorPosition = elevator::getPosition;
    }

    private static Command home() {
        return elevator.checkArmAndMove(ElevatorPositions.HOME, ArmPositions.HOME).withName("Going to Home");
    }

    private static Command stop() {
        return elevator.stopEverything();
    }

    private static Command deepHangSequence() {
        return elevator.deepHangSequence(ElevatorPositions.DEEPHANG, ElevatorPositions.HOME, ArmPositions.HOME)
                .withName("Going to Home");
    }

    private static Command setupDeepHang() {
        return elevator.checkArmAndMove(ElevatorPositions.SETUPDEEPHANG, ArmPositions.SETUPDEEPHIGH)
                .withName("Going to Deephang");
    }

    private static Command moveElevatorDeephang() {
        return elevator.requestTargetPosition(ElevatorPositions.DEEPHANG)
                .withName("Going to Deephang");
    }

    private static Command runningArm() {
        return arm.setSpeed(1);
    }
    // sysID

}
