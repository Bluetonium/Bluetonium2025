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

    private DriverConfigs config;

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
    public static Trigger L1;
    public static Trigger L2;
    public static Trigger L3;
    public static Trigger L4;
    public static Trigger Home;

    // Shoulder
    public static Trigger pos1;
    public static Trigger pos2;

    // outtake
    public static Trigger outtakeAccept;

    // sysid routines

    // arm

    public static Trigger armQuasForward;
    public static Trigger armQuasBackward;
    public static Trigger armDynForward;
    public static Trigger armDynBackward;

    // chassis

    public static Trigger chassisQuasForward;
    public static Trigger chassisQuasBackward;
    public static Trigger chassisDynForward;
    public static Trigger chassisDynBackward;

    // outtake

    public static Trigger outtakeQuasForward;
    public static Trigger outtakeQuasBackward;
    public static Trigger outtakeDynForward;
    public static Trigger outtakeDynBackward;

    // elevator

    public static Trigger elevatorQuasForward;
    public static Trigger elevatorQuasBackward;
    public static Trigger elevatorDynForward;
    public static Trigger elevatorDynBackward;

    private void applyTestConfigs(DriverConfigs configs) {

        // disables all other movement... TODO: allow regular configs to resume!

        chassisControlTranslation = () -> 0.0;
        chassisControlStrafe = () -> 0.0;
        chassisControlRotation = () -> 0.0;

        wheelsXPosition = new Trigger(() -> false);
        steerWheels = new Trigger(() -> false);
        zeroHeading = new Trigger(() -> false);
        reefAlignLeft = new Trigger(() -> false);
        reefAlignRight = new Trigger(() -> false);

        L1 = new Trigger(() -> false);
        L2 = new Trigger(() -> false);
        L3 = new Trigger(() -> false);
        L4 = new Trigger(() -> false);
        Home = new Trigger(() -> false);

        pos1 = new Trigger(() -> false);
        pos2 = new Trigger(() -> false);

        outtakeAccept = new Trigger(() -> false);
        // controller 1 stuff

        //TODO: idk figure out controls im feeling lazy rn
        if (configs.chassisDriving) {
            chassisQuasForward = new Trigger(() -> false);
            chassisQuasBackward = new Trigger(() -> false);
            chassisDynForward = new Trigger(() -> false);
            chassisDynBackward = new Trigger(() -> false);
    
            elevatorQuasForward = new Trigger(() -> false);
            elevatorQuasBackward = new Trigger(() -> false);
            elevatorDynForward = new Trigger(() -> false);
            elevatorDynBackward = new Trigger(() -> false);
        } else {
            outtakeQuasForward = new Trigger(() -> false);
            outtakeQuasBackward = new Trigger(() -> false);
            outtakeDynForward = new Trigger(() -> false);
            outtakeDynBackward = new Trigger(() -> false);

            armQuasForward = new Trigger(() -> false);
            armQuasBackward = new Trigger(() -> false);
            armDynForward = new Trigger(() -> false);
            armDynBackward = new Trigger(() -> false);
        }  

    }

    private void applyConfigs(DriverConfigs configs) {
        function = new Trigger(controller::getStartButton);
        noFunction = function.not();

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
            L1 = new Trigger(() -> controller.getPOV() == 0).and(noFunction);
            L2 = new Trigger(() -> controller.getPOV() == 90);
            L3 = new Trigger(() -> controller.getPOV() == 180);
            L4 = new Trigger(() -> controller.getPOV() == 270);
            Home = new Trigger(() -> controller.getPOV() == 0).and(function);
        }

        if (configs.armControl) {
            pos1 = new Trigger(controller::getXButton);
            pos2 = new Trigger(controller::getYButton);
        }

        if (configs.outtakeControls) {
            outtakeAccept = new Trigger(controller::getAButton);
        }

        armQuasForward = new Trigger(() -> false);
        armQuasBackward = new Trigger(() -> false);
        armDynForward = new Trigger(() -> false);
        armDynBackward = new Trigger(() -> false);

        chassisQuasForward = new Trigger(() -> false);
        chassisQuasBackward = new Trigger(() -> false);
        chassisDynForward = new Trigger(() -> false);
        chassisDynBackward = new Trigger(() -> false);

        outtakeQuasForward = new Trigger(() -> false);
        outtakeQuasBackward = new Trigger(() -> false);
        outtakeDynForward = new Trigger(() -> false);
        outtakeDynBackward = new Trigger(() -> false);

        elevatorQuasForward = new Trigger(() -> false);
        elevatorQuasBackward = new Trigger(() -> false);
        elevatorDynForward = new Trigger(() -> false);
        elevatorDynBackward = new Trigger(() -> false);
    }

    public Drivers(DriverConfigs driverConfigs) {
        config = driverConfigs;
        controller = new XboxController(driverConfigs.port);

        /** setting Configs */
        applyConfigs(driverConfigs);

        /* Triggers */

    }

    // could make the applyTestConfigs function public and use the config var but it
    // feels wrong not to mimic applyConfigs
    public void changeToTest() {
        applyTestConfigs(config);
    }
}
