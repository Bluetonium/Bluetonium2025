package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.teleop.*;
import frc.robot.constants.Constants;
import frc.robot.constants.Constants.ChassisControls;

import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

        /* Controllers */
        private final XboxController driverController = new XboxController(
                        Constants.ControllerConstants.DRIVER_CONTROLLER_PORT);

        /* Chassis driver Buttons */
        private final JoystickButton zeroGyro = new JoystickButton(driverController, ChassisControls.ZERO_GYRO_BUTTON);

        /* Subsystems */
        private final Swerve swerve;

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {

                swerve = new Swerve();

                swerve.setDefaultCommand(
                                new TeleopSwerve(
                                                swerve,
                                                () -> -driverController.getRawAxis(ChassisControls.TRANSLATION_AXIS),
                                                () -> -driverController.getRawAxis(ChassisControls.STRAFE_AXIS),
                                                () -> -driverController.getRawAxis(ChassisControls.ROTATION_AXIS),
                                                () -> !driverController.getRawButton(ChassisControls.ROBOT_RELATIVE),
                                                () -> driverController.getRawAxis(ChassisControls.FAST_MODE)));

                configureButtonBindings();
        }

        /**
         * Use this method to define your button->command mappings. Buttons can be
         * created by
         * instantiating a {@link GenericHID} or one of its subclasses ({@link
         * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
         * it to a {@link
         * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
         */
        private void configureButtonBindings() {
                /* Driver Buttons */
                zeroGyro.onTrue(new InstantCommand(swerve::zeroHeading));

        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {
              return null;
        }
}
