package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.io.File;
import java.io.IOException;



import edu.wpi.first.wpilibj.Filesystem;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.util.Units;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class Swerve extends SubsystemBase {

    double maximumSpeed = Units.feetToMeters(17.6);
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
    SwerveDrive swerveDrive;

    public Swerve() {
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumSpeed);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;

    }



    /**
     * 
     * @param translation   the translational speed of the rboot in m/s
     * @param rotation      the rotational speed of robot in rad/s
     * @param fieldRelative wether to be head or headless
     * @param isOpenLoop    wether to run the wheels open loop or closed loop
     */
    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        swerveDrive.drive(translation, rotation, fieldRelative, isOpenLoop);
    }

    public double getMaxVelocity() {
        return swerveDrive.getMaximumChassisVelocity();
    }

    public double getMaxAngularVelocity() {
        return swerveDrive.getMaximumChassisAngularVelocity();
    }

    /**
     * 
     * @return returns the current speed of the robot in ChassisSpeeds
     */
    public ChassisSpeeds getRobotRelativeSpeeds() {
        return swerveDrive.getRobotVelocity();
    }

    /**
     * 
     * @param chassisSpeeds the speeds to drive the robot at in ChassisSpeeds
     */
    public void driveRobotReleative(ChassisSpeeds chassisSpeeds) {
        swerveDrive.drive(chassisSpeeds);
    }

    /**
     * 
     * @return Returns the current modules states
     */
    public SwerveModuleState[] getModuleStates() {
        return swerveDrive.getStates();
    }

    /**
     * 
     * @return Returns the current module positions
     */
    public SwerveModulePosition[] getModulePositions() {
        return swerveDrive.getModulePositions();
    }

    /**
     * 
     * @return Returns the robots positions in meters
     */
    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    /**
     * 
     * @param pose Sets the position of the odometry
     */
    public void setPose(Pose2d pose) {
        swerveDrive.resetOdometry(pose);
    }

    /**
     * 
     * @return Gets the current heading of the robot
     */
    public Rotation2d getHeading() {
        return getPose().getRotation();
    }

    /**
     * Zeroes the robots heading
     */
    public void zeroHeading() {
        swerveDrive.zeroGyro();
    }

    /**
     * 
     * @return Current yaw of the robot
     */
    public Rotation2d getGyroYaw() {
        return swerveDrive.getYaw();
    }

}