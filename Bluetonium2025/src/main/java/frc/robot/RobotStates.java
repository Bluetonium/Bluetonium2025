package frc.robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.driver.Drivers;

public class RobotStates {

    // states
    public static Trigger teleop;
    public static Trigger autoMode;
    public static Trigger testMode;
    public static Trigger disabled;
    public static Trigger dsAttached;
    public static Trigger endGame;
    public static Trigger Estopped;

    // elevator
    public static Trigger home;
    public static Trigger intakePosition;
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger algaeRemove;

    // chassis
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
    public static Trigger coralStationAlign;
    public static Trigger microAdjust;
    public static Trigger triggerMicroAdjust;

    // outtake
    public static Trigger intake;
    public static Trigger outtake;

    public static void setupStates() {
        teleop = new Trigger(DriverStation::isTeleopEnabled);
        autoMode = new Trigger(RobotState::isAutonomous);
        testMode = new Trigger(RobotState::isTest);
        disabled = new Trigger(RobotState::isDisabled);
        dsAttached = new Trigger(DriverStation::isDSAttached);
        Estopped = new Trigger(DriverStation::isEStopped);

        endGame = teleop.and(() -> DriverStation.getMatchTime() < 20);

        home = Drivers.home;
        intakePosition = Drivers.intakePosition;
        L1 = Drivers.L1;
        L2 = Drivers.L2;
        L3 = Drivers.L3;
        algaeRemove = Drivers.algaeRemove;
        intake = Drivers.intake;
        outtake = Drivers.outtake;

        reefAlignLeft = Drivers.reefAlignLeft;
        reefAlignRight = Drivers.reefAlignRight;
        coralStationAlign = Drivers.coralStationAlign;
        microAdjust = Drivers.microAdjust;
        triggerMicroAdjust = Drivers.triggerMicroAdjust;

        loadMusic();
    }

    private static void loadMusic() {
        SendableChooser<String> chooser = new SendableChooser<>();
        System.out.println(Filesystem.getDeployDirectory() + "/Music/");
        try (Stream<Path> filesStream = Files.list(Paths.get(Filesystem.getDeployDirectory() + "/Music/"))) {
            List<String> fileNames = filesStream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

            fileNames.forEach((name) -> {
                chooser.addOption(name, name);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        SmartDashboard.putData("Music Choice", chooser);
        SmartDashboard.putBoolean("Play Music", false);

        new Trigger(
                () -> SmartDashboard.getBoolean("PlayMusic", false))
                .whileTrue(
                        Commands.deferredProxy(() -> {
                            return RobotContainer.playSong(chooser.getSelected());
                        }));
    }

}
