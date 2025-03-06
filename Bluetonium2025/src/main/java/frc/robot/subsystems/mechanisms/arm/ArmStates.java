package frc.robot.subsystems.mechanisms.arm;

import java.util.function.DoubleSupplier;

import frc.robot.RobotContainer;

public class ArmStates {
    private static Arm arm = RobotContainer.getArm();

    public static final DoubleSupplier armPosition = arm::getPosition;

    public static void setStates() {
    }

}
