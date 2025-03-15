// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.driver.DriverConstants;
import frc.robot.subsystems.driver.DriverStates;
import frc.robot.subsystems.driver.Drivers;
import frc.robot.subsystems.led.LED;
import frc.robot.subsystems.limelight.LimelightConstants;
import frc.robot.subsystems.limelight.LimelightLocalization;
import frc.robot.subsystems.mechanisms.elevator.Elevator;
import frc.robot.subsystems.mechanisms.outtake.Outtake;
import frc.robot.subsystems.mechanisms.swerve.CommandSwerveDrivetrain;
import frc.robot.subsystems.mechanisms.swerve.TunerConstants;
import lombok.Getter;

public class RobotContainer {
    /* Setting up bindings for necessary control of the swerve drive platform */

    private final Telemetry logger = new Telemetry(TunerConstants.kSpeedAt12Volts.in(MetersPerSecond));
    private final double INIT_DELAY = 0.1;

    private final SendableChooser<Command> autoChooser;
    private Command currentAuto;

    // Subsystems
    @Getter
    private static Elevator elevator;
    @Getter
    private static CommandSwerveDrivetrain swerve;
    @Getter
    private static LimelightLocalization vision;
    @Getter
    private static Drivers driver1;
    @Getter
    private static Drivers driver2;
    @Getter
    private static Outtake outtake;
    @Getter
    private static LED leds;

    public RobotContainer() {
        initalizeSubsystems();
        RobotStates.setupStates();

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

    private void initalizeSubsystems() {
        elevator = new Elevator();
        Timer.delay(INIT_DELAY);
        swerve = TunerConstants.createDrivetrain();
        Timer.delay(INIT_DELAY);
        vision = new LimelightLocalization(swerve);
        Timer.delay(INIT_DELAY);
        outtake = new Outtake();
        Timer.delay(INIT_DELAY);
        leds = new LED();
        Timer.delay(INIT_DELAY);

        // in order for the testable systems to work, these need to init last... trust
        // me
        driver1 = new Drivers(DriverConstants.driver1Configs);
        Timer.delay(INIT_DELAY);
        driver2 = new Drivers(DriverConstants.driver2Configs);
    }

    public void resetRobotState() {
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        RobotStates.setupStates();
        setupSubsystems();

    }

    private void setupSubsystems() {
        swerve.setup();
        elevator.setup();
        outtake.setup();
        leds.setup();
        DriverStates.setupTestables();
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
