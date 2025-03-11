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
        protected boolean armControl = false;
    }

    // Controller
    private final XboxController controller;

    // Control axis
    public static DoubleSupplier chassisControlTranslation;
    public static DoubleSupplier chassisControlStrafe;
    public static DoubleSupplier chassisControlRotation;

    // Triggers
    // Other
    private Trigger function;
    private Trigger noFunction;

    // Chassis
    public static Trigger wheelsXPosition;
    public static Trigger steerWheels;
    public static Trigger zeroHeading;
    public static Trigger reefAlignLeft;
    public static Trigger reefAlignRight;
    public static Trigger coralStationAlign;
    // Elevator
    public static Trigger Home;
    public static Trigger deepHangSequence;
    public static Trigger setupDeepHang;
    public static Trigger moveElevatorDeephang;
    public static Trigger runningArm;

    public static Trigger STOP;

    private void applyConfigs(DriverConfigs configs) {
        function = new Trigger(controller::getStartButton);
        noFunction = new Trigger(() -> !controller.getStartButton());

        if (configs.chassisDriving) {
            chassisControlTranslation = controller::getLeftY;
            chassisControlStrafe = controller::getLeftX;
            chassisControlRotation = controller::getRightX;

            wheelsXPosition = new Trigger(controller::getXButton).and(noFunction);
            steerWheels = new Trigger(controller::getAButton).and(noFunction);
            zeroHeading = new Trigger(controller::getBButton).and(noFunction);
            reefAlignLeft = new Trigger(controller::getLeftBumperButton);
            reefAlignRight = new Trigger(controller::getRightBumperButton);
            coralStationAlign = new Trigger(controller::getYButton);
        }

        if (configs.elevatorControl) {
            // TODO finalize these
            Home = new Trigger(controller::getYButton);
            STOP = new Trigger(() -> controller.getXButtonPressed());
            deepHangSequence = new Trigger(controller::getLeftBumperButton);
            setupDeepHang = new Trigger(controller::getAButton);
            moveElevatorDeephang = new Trigger(controller::getBButton);
            runningArm = new Trigger(controller::getRightBumperButton);
        }
    }

    public Drivers(DriverConfigs driverConfigs) {
        controller = new XboxController(driverConfigs.port);
        applyConfigs(driverConfigs);

    }

}
