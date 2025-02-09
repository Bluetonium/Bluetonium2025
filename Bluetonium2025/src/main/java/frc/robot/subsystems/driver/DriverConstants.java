package frc.robot.subsystems.driver;

import frc.robot.subsystems.driver.Drivers.DriverConfigs;

public class DriverConstants {
    public static final DriverConfigs driver1Configs = new DriverConfigs();
    public static final DriverConfigs driver2Configs = new DriverConfigs();

    static {
        // driver1Configs
        driver1Configs.setChassisDriving(true);
        driver1Configs.setPort(0);

        // driver2Configs
        driver2Configs.setPort(2);

    }
}
