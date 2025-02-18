package frc.robot.subsystems.mechanisms.swerve;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotContainer;
import frc.robot.subsystems.driver.Drivers;

public class SwerveStates {
    private static CommandSwerveDrivetrain swerve = RobotContainer.getSwerve();
    private static Drivers drivers = RobotContainer.getDrivers();

    private static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private static final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    public static void setStates() {

        Drivers.wheelsXPosition.whileTrue(swerve.applyRequest(() -> brake));
        Drivers.steerWheels.whileTrue(swerve.applyRequest(
                () -> point.withModuleDirection(
                        new Rotation2d(-drivers.chassisControlTranslation.getAsDouble(),
                                -drivers.chassisControlStrafe.getAsDouble()))));

        // reset the field-centric heading on left bumper press
        Drivers.zeroHeading.onTrue(swerve.runOnce(() -> swerve.seedFieldCentric()));
        Drivers.reefAlgin.whileTrue(swerve.AlignToReefRegion(false));

    }

}
