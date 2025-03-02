package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;

public class ArmStates {
    private static Arm arm = RobotContainer.getArm();

    public static void setStates() {
        RobotStates.L2.whileTrue(L2());
        RobotStates.L3.whileTrue(L3());
        RobotStates.L4.whileTrue(L4());
        RobotStates.Home.whileTrue(Home());

        RobotStates.armQuasForward.whileTrue(quasForward());
        RobotStates.armQuasBackward.whileTrue(quasBackward());
        RobotStates.armDynForward.whileTrue(dynForward());
        RobotStates.armDynBackward.whileTrue(dynBackward());
    }

    private static Command L2() {
        return arm.setArmPosition(ArmPositions.L2).withName("Arm.L2");
    }

    private static Command L3() {
        return arm.setArmPosition(ArmPositions.L3).withName("Arm.L3");
    }

    private static Command L4() {
        return arm.setArmPosition(ArmPositions.L4).withName("Arm.L4");
    }

    private static Command Home() {
        return arm.setArmPosition(ArmPositions.HOME).withName("Arm.Home");
    }

    //sysID

    private static Command quasForward() {
        return arm.sysIdDynamic(Direction.kForward);
    }

    private static Command quasBackward() {
        return arm.sysIdDynamic(Direction.kForward);
    }

    private static Command dynForward() {
        return arm.sysIdDynamic(Direction.kForward);
    }

    private static Command dynBackward() {
        return arm.sysIdDynamic(Direction.kForward);
    }
}
