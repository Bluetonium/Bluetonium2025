package frc.robot.subsystems.driver;

import java.lang.reflect.Field;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import lombok.Setter;

public class Drivers {
    public static class DriverConfigs {
        @Setter
        private int port = 0;
        @Setter
        private boolean elevatorControl = false;
        @Setter
        private boolean chassisDriving = false;
        @Setter
        private boolean armControl = false;
    }

    // Controllers
    private final XboxController driver1Controller;
    private final XboxController driver2Controller;

    // Control axis
    public DoubleSupplier chassisControlTranslation;
    public DoubleSupplier chassisControlStrafe;
    public DoubleSupplier chassisControlRotation;

    // Triggers
    // Chassis
    public Trigger wheelsXPosition;
    public Trigger steerWheels;
    public Trigger zeroHeading;

    // Elevator
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;

    // Arm
    public static Trigger pos1;
    public static Trigger pos2;

    /***
     * Checks to see if two configurations share any controls and throw an error if
     * they do
     */
    private void checkOverlap(DriverConfigs config1, DriverConfigs config2) {
        Field[] fields = DriverConfigs.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType() == boolean.class && field.getBoolean(config1) == true
                        && field.getBoolean(config2) == true) {

                    throw new RuntimeException();
                }
            } catch (IllegalAccessException access) {
                DriverStation.reportWarning("Failed to check overlap on driver controlls", access.getStackTrace());
            } catch (RuntimeException config) {
                DriverStation.reportError("Driver Control Overlap - %s".formatted(field.getName()),
                        config.getStackTrace());
            }
        }

    }

    private void applyConfigs(DriverConfigs configs, XboxController controller) {
        if (configs.chassisDriving) {
            chassisControlTranslation = controller::getLeftY;
            chassisControlStrafe = controller::getLeftX;
            chassisControlRotation = controller::getRightX;

            wheelsXPosition = new Trigger(controller::getXButton);
            steerWheels = new Trigger(controller::getAButton);
            zeroHeading = new Trigger(controller::getLeftBumperButton);

        }

        if (configs.elevatorControl) {
            // TODO finalize these
            L1 = new Trigger(() -> controller.getPOV() == 0);
            L2 = new Trigger(() -> controller.getPOV() == 90);
            L3 = new Trigger(() -> controller.getPOV() == 180);
            L4 = new Trigger(() -> controller.getPOV() == 270);

        }

        if (configs.armControl) {
            pos1 = new Trigger(() -> controller.getXButton());
            pos2 = new Trigger(() -> controller.getYButton());
        }

    }

    public Drivers(DriverConfigs driver1Configs, DriverConfigs driver2Configs) {

        System.out.println("Constructed Drivers");
        driver1Controller = new XboxController(driver1Configs.port);
        driver2Controller = new XboxController(driver2Configs.port);
        checkOverlap(driver1Configs, driver2Configs);

        /** setting Configs */
        applyConfigs(driver1Configs, driver1Controller);
        applyConfigs(driver2Configs, driver2Controller);

        /* Triggers */

    }

}
