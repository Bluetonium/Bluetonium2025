package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.driver.Drivers;

public class RobotStates {

    public static final Trigger teleop = new Trigger(DriverStation::isTeleopEnabled);
    public static final Trigger autoMode = new Trigger(DriverStation::isAutonomousEnabled)
            .or(new Trigger(DriverStation::isAutonomous));
    public static final Trigger testMode = new Trigger(DriverStation::isTestEnabled);
    public static final Trigger disabled = new Trigger(DriverStation::isDisabled);
    public static final Trigger dsAttached = new Trigger(DriverStation::isDSAttached);

    // elevator and Arm
    public static final Trigger L1 = Drivers.L1;
    public static final Trigger L2 = Drivers.L2;
    public static final Trigger L3 = Drivers.L3;
    public static final Trigger L4 = Drivers.L4;
    public static final Trigger Home = Drivers.Home;

    // outtake
    public static final Trigger outtakeAccept = Drivers.outtakeAccept;
    public static final Trigger reefAlignLeft = Drivers.reefAlignLeft;
    public static final Trigger reefAlignRight = Drivers.reefAlignRight;

    // sysid routines

    //arm

    public static Trigger armQuasForward = Drivers.armQuasForward;
    public static Trigger armQuasBackward = Drivers.armQuasBackward;
    public static Trigger armDynForward = Drivers.armDynForward;
    public static Trigger armDynBackward = Drivers.armDynBackward;

    // chassis

    public static Trigger chassisQuasForward = Drivers.chassisQuasForward;
    public static Trigger chassisQuasBackward = Drivers.chassisQuasBackward;
    public static Trigger chassisDynForward = Drivers.chassisDynForward;
    public static Trigger chassisDynBackward = Drivers.chassisDynBackward;

    //outtake

    public static Trigger outtakeQuasForward = Drivers.outtakeQuasForward;
    public static Trigger outtakeQuasBackward = Drivers.outtakeQuasBackward;
    public static Trigger outtakeDynForward = Drivers.outtakeDynForward;
    public static Trigger outtakeDynBackward = Drivers.outtakeDynBackward;

    // elevator

    public static Trigger elevatorQuasForward = Drivers.elevatorQuasForward;
    public static Trigger elevatorQuasBackward = Drivers.elevatorQuasBackward;
    public static Trigger elevatorDynForward = Drivers.elevatorDynForward;
    public static Trigger elevatorDynBackward = Drivers.elevatorDynBackward;

}
