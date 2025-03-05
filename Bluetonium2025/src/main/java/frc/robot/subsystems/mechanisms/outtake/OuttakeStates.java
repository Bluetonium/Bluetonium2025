package frc.robot.subsystems.mechanisms.outtake;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.arm.Arm;
import frc.robot.subsystems.mechanisms.elevator.Elevator;

public class OuttakeStates {
    private static Outtake outtake = RobotContainer.getOuttake();
    private static Elevator elevator = RobotContainer.getElevator();
    private static Arm arm = RobotContainer.getArm();
    public static void setupStates() {
        // lmao!!!!!
        boolean isInPosition = elevator.elevatorIsAtDesiredPosition() && arm.armIsAtDesiredPosition();
        RobotStates.outtakeAccept.whileTrue(outtake.outtakeAccept()).and(()->isInPosition);
        RobotStates.outtakeEject.whileTrue(outtake.outtakeEject()).and(()->isInPosition);
        // RobotStates.reef
        // sys id (this states file dry as hell dawg)

    }

}
