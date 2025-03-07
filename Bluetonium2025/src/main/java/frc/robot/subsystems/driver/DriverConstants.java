package frc.robot.subsystems.driver;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.RobotContainer;
import frc.robot.subsystems.driver.Drivers.DriverConfigs;
import frc.robot.subsystems.mechanisms.arm.Arm;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.robot.subsystems.mechanisms.elevator.Elevator;

public class DriverConstants {
    public static final DriverConfigs driver1Configs = new DriverConfigs();
    public static final DriverConfigs driver2Configs = new DriverConfigs();

    private static final Elevator elevator = RobotContainer.getElevator();
    private static final Arm arm = RobotContainer.getArm();

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
        driver2Configs.setArmControl(true);
        driver2Configs.setOuttakeControls(true);
        checkOverlap(driver2Configs, driver1Configs);
    }

    public static enum TestableSystems {
        ELEVATOR_QUASISTATIC_FORWARD(elevator.sysIdQuasistatic(Direction.kForward)),
        ELEVATOR_QUASISTATIC_REVERSE(elevator.sysIdQuasistatic(Direction.kReverse)),
        ELEVATOR_DYNAMIC_FORWARD(elevator.sysIdDynamic(Direction.kForward)),
        ELEVATOR_DYNAMIC_REVERSE(elevator.sysIdDynamic(Direction.kReverse)),
        ARM_QUASISTATIC_FORWARD(arm.sysIdQuasistatic(Direction.kForward)),
        ARM_QUASISTATIC_REVERSE(arm.sysIdQuasistatic(Direction.kReverse)),
        ARM_DYANMIC_FORWARD(arm.sysIdDynamic(Direction.kForward)),
        ARM_DYANMIC_REVERSE(arm.sysIdDynamic(Direction.kReverse)),
        ARM_POSITION_TEST(arm.setArmPosition(ArmPositions.L2));


        public final Command command;

        private TestableSystems(Command command) {
            this.command = command;
        }
    }
}
