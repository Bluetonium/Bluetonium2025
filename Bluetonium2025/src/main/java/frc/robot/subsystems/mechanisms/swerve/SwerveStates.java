package frc.robot.subsystems.mechanisms.swerve;

import java.io.File;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotContainer;
import frc.robot.RobotStates;
import frc.robot.subsystems.driver.Drivers;

public class SwerveStates {
    private static CommandSwerveDrivetrain swerve = RobotContainer.getSwerve();

    private static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private static final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    public static void setStates() {
        Drivers.wheelsXPosition.whileTrue(swerve.applyRequest(() -> brake));
        Drivers.steerWheels.whileTrue(swerve.applyRequest(
                () -> point.withModuleDirection(
                        new Rotation2d(-Drivers.chassisControlTranslation.getAsDouble(),
                                -Drivers.chassisControlStrafe.getAsDouble()))));

        Drivers.zeroHeading.onTrue(swerve.runOnce(() -> swerve.seedFieldCentric()));
        RobotStates.reefAlignLeft.whileTrue(swerve.AlignToReefRegion(true));
        RobotStates.reefAlignRight.whileTrue(swerve.AlignToReefRegion(false));
        RobotStates.coralStationAlign.whileTrue(swerve.AlignToCoralStation());

        SendableChooser<String> musicChooser = new SendableChooser<>();

        File directory = new File("Music/");
        File[] filesArray = directory.listFiles();
        if (filesArray != null) {
            for (File file : filesArray) {
                if (file.isFile()) {
                    musicChooser.addOption(file.getName(), file.getName());
                }
            }
        }
        SmartDashboard.putData("Music Choice", musicChooser);

        SmartDashboard.putBoolean("Respectful", false);
        new Trigger(() -> SmartDashboard.getBoolean("Respectful", false))
                .whileTrue(swerve.playMusic("Music/undertale.chrp"));
    }

}
