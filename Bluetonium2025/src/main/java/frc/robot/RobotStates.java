package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotStates {

    public static final Trigger teleop = new Trigger(DriverStation::isTeleopEnabled);
    public static final Trigger autoMode = new Trigger(DriverStation::isAutonomousEnabled)
            .or(new Trigger(DriverStation::isAutonomous));
    public static final Trigger testMode = new Trigger(DriverStation::isTestEnabled);
    public static final Trigger disabled = new Trigger(DriverStation::isDisabled);
    public static final Trigger dsAttached = new Trigger(DriverStation::isDSAttached);
}
