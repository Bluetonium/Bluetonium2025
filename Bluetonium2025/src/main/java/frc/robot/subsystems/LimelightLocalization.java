package frc.robot.subsystems;

import java.util.ArrayList;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.LimelightConfigs;
import frc.utils.LimelightHelpers;

public class LimelightLocalization extends SubsystemBase {
    private CommandSwerveDrivetrain drivetrain;
    private boolean enabled = true;
    private ArrayList<String> localizationLimelights;
    private Pigeon2 gyro;

    /***
     * 
     * @param drivetrain the drive train - needed so we can add vision measurements
     * @param limelights names of the limelights that will be used
     */
    public LimelightLocalization(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        gyro = drivetrain.getPigeon2();
        localizationLimelights = new ArrayList<String>(0);
    }

    public void addLocalizationLL(LimelightConfigs limelight) {
        LimelightHelpers.setCameraPose_RobotSpace(limelight.name, limelight.xLocation, limelight.yLocation,
                limelight.zLocation, limelight.roll, limelight.pitch, limelight.yaw);
        localizationLimelights.add(limelight.name);
    }

    public void configureLocalizationLLs() {
        for (String limelight : localizationLimelights) {
            LimelightHelpers.setPipelineIndex(limelight, LimelightHelpers.pipelines.LOCALIZATION.ordinal());
        }

        drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, Integer.MAX_VALUE));
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    @Override
    public void periodic() {
        if (!enabled)
            return;

        for (String limelight : localizationLimelights) {
            LimelightHelpers.SetRobotOrientation(limelight, gyro.getYaw().getValueAsDouble(),
                    gyro.getAngularVelocityZWorld().getValueAsDouble(), 0, 0, 0, 0);

            LimelightHelpers.PoseEstimate estimatedPosition = LimelightHelpers
                    .getBotPoseEstimate_wpiBlue_MegaTag2(limelight);
            if (estimatedPosition.tagCount == 0 || Math.abs(gyro.getAngularVelocityZWorld().getValueAsDouble()) > 720)
                return;

            drivetrain.addVisionMeasurement(estimatedPosition.pose,
                    estimatedPosition.timestampSeconds);

        }
    }
}