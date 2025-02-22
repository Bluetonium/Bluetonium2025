package frc.robot.subsystems.driver;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.driver.Drivers.DriverConfigs;

public class DriverConstants {
    public static final DriverConfigs driver1Configs = new DriverConfigs();
    public static final DriverConfigs driver2Configs = new DriverConfigs();

    private static void checkOverlap(DriverConfigs config1, DriverConfigs config2) {
        Field[] fields = DriverConfigs.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType() == boolean.class && field.getBoolean(config1) == true
                        && field.getBoolean(config2) == true) {
                    throw new RuntimeException();
                }
            } catch (IllegalAccessException access) {

                DriverStation.reportWarning("Failed to check overlap on driver controlls",
                        access.getStackTrace());
            } catch (RuntimeException config) {
                DriverStation.reportError("Driver Control Overlap - %s".formatted(field.getName()),
                        config.getStackTrace());
            }
        }

    }

    static {
        // driver1Configs
        driver1Configs.setChassisDriving(true);
        driver1Configs.setPort(0);

        // driver2Configs
        driver2Configs.setPort(1);
        driver2Configs.setElevatorControl(true);

        checkOverlap(driver2Configs, driver1Configs);
    }
}
