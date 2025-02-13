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

    public static final Trigger L1 = Drivers.L1;
    public static final Trigger L2 = Drivers.L2;
    public static final Trigger L3 = Drivers.L3;
    public static final Trigger L4 = Drivers.L4;
    public static final Trigger Home = null;// TODO change

    // arm

    public static final Trigger pos1 = Drivers.pos1;
    public static final Trigger pos2 = Drivers.pos2;

}
