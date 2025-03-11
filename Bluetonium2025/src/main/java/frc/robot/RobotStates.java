package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.driver.Drivers;

public class RobotStates {

    public static void setupStates() {
        teleop = new Trigger(DriverStation::isTeleopEnabled);
        autoMode = new Trigger(DriverStation::isAutonomousEnabled)
                .or(new Trigger(DriverStation::isAutonomous));
        testMode = new Trigger(DriverStation::isTestEnabled);
        disabled = new Trigger(DriverStation::isDisabled);
        dsAttached = new Trigger(DriverStation::isDSAttached);

        Home = Drivers.Home;
        deepHangSequence = Drivers.deepHangSequence;
        setupDeepHang = Drivers.setupDeepHang;
        moveElevatorDeephang = Drivers.moveElevatorDeephang;
        runningArm = Drivers.runningArm;
        STOP = Drivers.STOP;

        reefAlignLeft = Drivers.reefAlignLeft;
        reefAlignRight = Drivers.reefAlignRight;
        coralStationAlign = Drivers.coralStationAlign;
    }

    public static Trigger teleop;
    public static Trigger autoMode;
    public static Trigger testMode;
    public static Trigger disabled;
    public static Trigger dsAttached;

    // elevator and Arm
    public static Trigger Home;
    public static Trigger deepHangSequence;
    public static Trigger setupDeepHang;
    public static Trigger moveElevatorDeephang;
    public static Trigger runningArm;
    public static Trigger STOP;

    // outtake
    public static Trigger outtakeAccept;

    // chassis
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
    public static Trigger coralStationAlign;
}
