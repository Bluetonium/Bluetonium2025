package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.driver.Drivers;

public class RobotStates {

    public static void setupStates() {

        teleop = new Trigger(RobotState::isTeleop);
        autoMode = new Trigger(RobotState::isAutonomous);
        testMode = new Trigger(RobotState::isTest);
        disabled = new Trigger(RobotState::isDisabled);
        dsAttached = new Trigger(DriverStation::isDSAttached);

        home = Drivers.home;
        intakePosition = Drivers.intakePosition;
        L1 = Drivers.L1;
        L2 = Drivers.L2;
        L3 = Drivers.L3;
        L4 = Drivers.L4;
        intake = Drivers.intake;
        outtake = Drivers.outtake;

        reefAlignLeft = Drivers.reefAlignLeft;
        reefAlignRight = Drivers.reefAlignRight;
        coralStationAlign = Drivers.coralStationAlign;
    }

    public static Trigger teleop;
    public static Trigger autoMode;
    public static Trigger testMode;
    public static Trigger disabled;
    public static Trigger dsAttached;

    // elevator
    public static Trigger home;
    public static Trigger intakePosition;
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;

    // chassis
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
    public static Trigger coralStationAlign;

    // outtake
    public static Trigger intake;
    public static Trigger outtake;
}
