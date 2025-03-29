package frc.robot.subsystems.mechanisms.swerve;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.driver.Drivers;

public class SwerveStates {
    private static CommandSwerveDrivetrain swerve = RobotContainer.getSwerve();

    private static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private static final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    public static void setStates() {
        // Drivers.
        Drivers.wheelsXPosition.whileTrue(swerve.applyRequest(() -> brake));
        Drivers.steerWheels.whileTrue(swerve.applyRequest(
                () -> point.withModuleDirection(
                        new Rotation2d(-Drivers.chassisControlTranslation.getAsDouble(),
                                -Drivers.chassisControlStrafe.getAsDouble()))));

        Drivers.zeroHeading.onTrue(swerve.runOnce(() -> swerve.seedFieldCentric()));
        RobotStates.microAdjust.whileTrue(swerve.dpadRelative(Drivers.pov));
        RobotStates.triggerMicroAdjust.whileTrue(swerve.slowSwerve());
        RobotStates.reefAlignLeft.whileTrue(swerve.AlignToReefRegion(true));
        RobotStates.reefAlignRight.whileTrue(swerve.AlignToReefRegion(false));
        RobotStates.coralStationAlign.whileTrue(swerve.AlignToCoralStation());
    }

}
