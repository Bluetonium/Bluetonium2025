package frc.robot.subsystems.mechanisms.outtake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;

public class OuttakeStates {
    private static Outtake outtake = RobotContainer.getOuttake();

    public static void setupStates() {
        RobotStates.outtakeAccept.whileTrue(outtake.outtakeAccept());

        //sys id (this states file dry as hell dawg)

        RobotStates.outtakeQuasForward.whileTrue(quasForward());
        RobotStates.outtakeQuasBackward.whileTrue(quasBackward());
        RobotStates.outtakeDynForward.whileTrue(dynForward());
        RobotStates.outtakeDynBackward.whileTrue(dynBackward());

    }

    //sysID

    private static Command quasForward() {
        return outtake.sysIdDynamic(Direction.kForward);
    }

    private static Command quasBackward() {
        return outtake.sysIdDynamic(Direction.kForward);
    }

    private static Command dynForward() {
        return outtake.sysIdDynamic(Direction.kForward);
    }
    
    private static Command dynBackward() {
        return outtake.sysIdDynamic(Direction.kForward);
    }
}
