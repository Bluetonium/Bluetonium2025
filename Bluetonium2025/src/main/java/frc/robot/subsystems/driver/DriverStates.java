package frc.robot.subsystems.driver;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.driver.DriverConstants.TestableSystems;

public class DriverStates {

    public static void setupTestables() {

        for (TestableSystems system : TestableSystems.values()) {
            SmartDashboard.putData(system.name(), system.command);
        }
    }

}
