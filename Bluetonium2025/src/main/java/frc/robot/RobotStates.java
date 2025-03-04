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

        L1 = Drivers.L1;
        L2 = Drivers.L2;
        L3 = Drivers.L3;
        L4 = Drivers.L4;
        Home = Drivers.Home;

        outtakeAccept = Drivers.outtakeAccept;
        reefAlignLeft = Drivers.reefAlignLeft;
        reefAlignRight = Drivers.reefAlignRight;
    }

    public static Trigger teleop;
    public static Trigger autoMode;
    public static Trigger testMode;
    public static Trigger disabled;
    public static Trigger dsAttached;

    // elevator and Arm
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;
    public static Trigger Home;

    // outtake
    public static Trigger outtakeAccept;
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
}
