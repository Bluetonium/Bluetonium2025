package frc.robot.subsystems.driver;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotContainer;
import frc.robot.subsystems.driver.Drivers.DriverConfigs;
import frc.robot.subsystems.mechanisms.elevator.Elevator;
import frc.robot.subsystems.mechanisms.outtake.Outtake;

public class DriverConstants {
    public static final DriverConfigs driver1Configs = new DriverConfigs();
    public static final DriverConfigs driver2Configs = new DriverConfigs();

    private static final Elevator elevator = RobotContainer.getElevator();
    private static final Outtake outtake = RobotContainer.getOuttake();

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
        driver2Configs.setOuttakeControl(true);
        checkOverlap(driver2Configs, driver1Configs);
    }

    public static enum TestableSystems {
        ELEVATOR_QUASISTATIC_FORWARD(elevator.sysIdQuasistatic(Direction.kForward)),
        ELEVATOR_QUASISTATIC_REVERSE(elevator.sysIdQuasistatic(Direction.kReverse)),
        ELEVATOR_DYNAMIC_FORWARD(elevator.sysIdDynamic(Direction.kForward)),
        ELEVATOR_DYNAMIC_REVERSE(elevator.sysIdDynamic(Direction.kReverse)),
        OUTTAKE_QUASISTATIC_FORWARD(outtake.sysIdQuasistatic(Direction.kForward)),
        OUTTAKE_QUASISTATIC_REVERSE(outtake.sysIdQuasistatic(Direction.kReverse)),
        OUTTAKE_DYNAMIC_FORWARD(outtake.sysIdDynamic(Direction.kForward)),
        OUTTAKE_DYNAMIC_REVERSE(outtake.sysIdDynamic(Direction.kReverse));

        public final Command command;

        private TestableSystems(Command command) {
            this.command = command;
        }
    }
}
