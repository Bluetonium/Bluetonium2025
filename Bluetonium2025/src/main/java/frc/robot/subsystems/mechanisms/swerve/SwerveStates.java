package frc.robot.subsystems.mechanisms.swerve;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;

import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.driver.Drivers;
import frc.robot.subsystems.limelight.LimelightConstants;
import frc.robot.subsystems.limelight.LimelightConstants.Pipelines;

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
        RobotStates.reefAlignLeft
                .whileTrue(swerve.AprilTagAlign(LimelightConstants.LOWER_RIGHT_LL, Pipelines.REEF_TARGETING));
        RobotStates.reefAlignRight
                .whileTrue(swerve.AprilTagAlign(LimelightConstants.LOWER_LEFT_LL, Pipelines.REEF_TARGETING));
        RobotStates.coralStationAlign.whileTrue(swerve.AlignToCoralStation());
    }

}
