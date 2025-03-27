package frc.robot.subsystems.driver;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import lombok.Setter;

public class Drivers {
    public static class DriverConfigs {
        @Setter
        protected int port = 0;
        @Setter
        protected boolean elevatorControl = false;
        @Setter
        protected boolean chassisDriving = false;
        @Setter
        protected boolean outtakeControl = false;
    }

    // Controller
    private final XboxController controller;

    // Control axis
    // chassis
    public static DoubleSupplier chassisControlTranslation;
    public static DoubleSupplier chassisControlStrafe;
    public static DoubleSupplier chassisControlRotation;

    // elevator
    public static DoubleSupplier elevatorAdjustment;

    // Triggers

    // Chassis
    public static Trigger wheelsXPosition;
    public static Trigger steerWheels;
    public static Trigger zeroHeading;
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
    public static Trigger coralStationAlign;
    // Elevator
    public static Trigger home;
    public static Trigger intakePosition;
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;

    // Outtake
    public static Trigger intake;
    public static Trigger outtake;

    private void applyConfigs(DriverConfigs configs) {

        if (configs.chassisDriving) {
            chassisControlTranslation = controller::getLeftY;
            chassisControlStrafe = controller::getLeftX;
            chassisControlRotation = controller::getRightX;

            wheelsXPosition = new Trigger(controller::getXButton);
            steerWheels = new Trigger(controller::getAButton);
            zeroHeading = new Trigger(controller::getBButton);
            reefAlignLeft = new Trigger(controller::getLeftBumperButton);
            reefAlignRight = new Trigger(controller::getRightBumperButton);
            coralStationAlign = new Trigger(controller::getYButton);
        }

        if (configs.elevatorControl) {
            elevatorAdjustment = controller::getLeftY;
            home = new Trigger(controller::getYButton);
            intakePosition = new Trigger(controller::getLeftBumperButton);
            L1 = new Trigger(() -> controller.getPOV() == 0);
            L2 = new Trigger(() -> controller.getPOV() == 90);
            L3 = new Trigger(() -> controller.getPOV() == 180);
            L4 = new Trigger(() -> controller.getPOV() == 270);
        }

        if (configs.outtakeControl) {
            intake = new Trigger(controller::getAButton);
            outtake = new Trigger(controller::getBButton);
        }
    }

    public boolean isDisconnected() {
        return !controller.isConnected();
    }

    public Drivers(DriverConfigs driverConfigs) {
        controller = new XboxController(driverConfigs.port);
        applyConfigs(driverConfigs);

    }

}
// hello world