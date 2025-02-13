// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.driver.DriverConstants;
import frc.robot.subsystems.driver.Drivers;
import frc.robot.subsystems.limelight.LimelightConstants;
import frc.robot.subsystems.limelight.LimelightLocalization;
import frc.robot.subsystems.mechanisms.arm.Arm;
import frc.robot.subsystems.mechanisms.elevator.Elevator;
import frc.robot.subsystems.mechanisms.swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.mechanisms.swerve.TunerConstants;
import lombok.Getter;

public class RobotContainer {
    /* Setting up bindings for necessary control of the swerve drive platform */

    private final Telemetry logger = new Telemetry(TunerConstants.kSpeedAt12Volts.in(MetersPerSecond));

    private final SendableChooser<Command> autoChooser;
    private Command currentAuto;

    // Subsystems
    @Getter
    private static final Elevator elevator = new Elevator();
    @Getter
    private static final Arm arm = new Arm();
    @Getter
    private static final CommandSwerveDrivetrain swerve = TunerConstants.createDrivetrain();
    @Getter
    private static final LimelightLocalization vision = new LimelightLocalization(swerve);
    @Getter
    public static final Drivers drivers = new Drivers(DriverConstants.driver1Configs, DriverConstants.driver2Configs);

    public RobotContainer() {

        // Auto chooser
        autoChooser = AutoBuilder.buildAutoChooser();
        currentAuto = autoChooser.getSelected();
        autoChooser.onChange((command) -> currentAuto = command);
        SmartDashboard.putData("Automous", autoChooser);

        // configure everything
        configureLimelights();
        configureBindings();

        setupSubsystems();
    }

    private void setupSubsystems() {
        swerve.setup();
    }

    private void configureLimelights() {
        vision.addLocalizationLL(LimelightConstants.MAIN_LL);

    }

    private void configureBindings() {
        swerve.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        return currentAuto;
    }
}
