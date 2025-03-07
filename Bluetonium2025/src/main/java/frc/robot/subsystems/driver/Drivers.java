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
        @Setter
        protected boolean outtakeControls = false;
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
    // Elevator
    public static Trigger coralPassOff;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;
    public static Trigger Home;

    // outtake
    public static Trigger outtakeAccept;
    public static Trigger outtakeEject;

    private void applyConfigs(DriverConfigs configs) {
        function = new Trigger(controller::getStartButton);
        noFunction = new Trigger(() -> !controller.getStartButton());

        if (configs.chassisDriving) {
            chassisControlTranslation = controller::getLeftY;
            chassisControlStrafe = controller::getLeftX;
            chassisControlRotation = controller::getRightX;

            wheelsXPosition = new Trigger(controller::getXButton).and(noFunction);
            steerWheels = new Trigger(controller::getAButton).and(noFunction);
            zeroHeading = new Trigger(controller::getLeftBumperButton).and(noFunction);
            reefAlignLeft = new Trigger(controller::getLeftBumperButton);
            reefAlignRight = new Trigger(controller::getRightBumperButton);
        }

        if (configs.elevatorControl) {
            // TODO finalize these
            coralPassOff = new Trigger(() -> controller.getPOV() == 0).and(noFunction);
            L2 = new Trigger(() -> controller.getPOV() == 90);
            L3 = new Trigger(() -> controller.getPOV() == 180);
            L4 = new Trigger(() -> controller.getPOV() == 270);
            Home = new Trigger(() -> controller.getPOV() == 0).and(function);
        }

        if (configs.outtakeControls) {

            outtakeAccept = new Trigger(() -> controller.getLeftTriggerAxis() > .5);// should only need to be a button
                                                                                    // if we are using as
            // intake.. which we might so yay
            outtakeEject = new Trigger(() -> controller.getRightTriggerAxis() > .5); // TODO: fuckin figure out a button
        }

    }

    public Drivers(DriverConfigs driverConfigs) {
        controller = new XboxController(driverConfigs.port);
        applyConfigs(driverConfigs);

    }

}
