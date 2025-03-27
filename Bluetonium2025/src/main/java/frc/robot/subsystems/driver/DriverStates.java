package frc.robot.subsystems.driver;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.driver.DriverConstants.TestableSystems;

public class DriverStates {
    private  static SendableChooser<TestableSystems> testChooser;
    //public static Trigger controllerDisconnect = Trigger.kFalse;

    public static void setupTestables() {
        testChooser = new SendableChooser<>();
        for (TestableSystems system : TestableSystems.values()) {
            testChooser.addOption(system.name(), system);
        }
        SmartDashboard.putData("Test Command Chooser", testChooser);
        SmartDashboard.putData("Test Command", Commands.none());
        // testChooser.setDefaultOption("funny",
        // DriverConstants.TestableSystems.ARM_DYANMIC_FORWARD);
        testChooser.onChange((system) -> {
            if (system != null)
                SmartDashboard.putData("Test Command", system.command);
        });

    }

}
