package frc.robot.subsystems.limelight;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.limelight.LimelightConstants.Pipelines;
import frc.robot.subsystems.mechanisms.swerve.CommandSwerveDrivetrain;
import frc.utils.LimelightHelpers;

public class Limelights extends SubsystemBase {
    private static CommandSwerveDrivetrain drivetrain;
    private LimelightConfig[] limelights;
    private Pigeon2 gyro;

    /***
     * 
     * @param drivetrain the drive train - needed so we can add vision measurements
     * @param limelights names of the limelights that will be used
     */
    public Limelights(LimelightConfig... limelights) {

        this.limelights = new LimelightConfig[limelights.length];
        for (int i = 0; i < limelights.length; i++) {
            addLimelight(limelights[i], i);
        }
    }

    private void addLimelight(LimelightConfig limelight, int index) {
        LimelightHelpers.setCameraPose_RobotSpace(limelight.name, limelight.position.getX(), limelight.position.getY(),
                limelight.position.getZ(), limelight.rotation.getX(), limelight.rotation.getY(),
                limelight.rotation.getZ());
        limelights[index] = limelight;
    }

    public void setup() {
        drivetrain = RobotContainer.getSwerve();
        gyro = drivetrain.getPigeon2();
        drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, Integer.MAX_VALUE));
        setDefaultCommand(configureForLocalization());
    }

    private Command configureForLocalization() {
        return startRun(() -> {
            for (LimelightConfig limelight : limelights) {
                LimelightHelpers.setPipelineIndex(limelight.name, Pipelines.LOCALIZATION.pipeline);
            }
        }, () -> {
        }).withName("Localization");

    }

    public void setPipeline(LimelightConfig limelight, Pipelines pipeline) {
        LimelightHelpers.setPipelineIndex(limelight.name, pipeline.pipeline);
    }

    public boolean hasTarget(LimelightConfig limelight) {
        return LimelightHelpers.getTV(limelight.name);
    }

    public double getTx(LimelightConfig limelight) {
        return LimelightHelpers.getTX(limelight.name);
    }

    @Override
    public void periodic() {
        if (RobotState.isAutonomous())
            return;

        for (LimelightConfig limelight : limelights) {
            if (LimelightHelpers.getCurrentPipelineIndex(limelight.name) != Pipelines.LOCALIZATION.pipeline)
                continue;

            LimelightHelpers.SetRobotOrientation(limelight.name, drivetrain.getState().Pose.getRotation().getDegrees(),
                    gyro.getAngularVelocityZWorld().getValueAsDouble(), 0, 0, 0, 0);

            LimelightHelpers.PoseEstimate estimatedPosition = LimelightHelpers
                    .getBotPoseEstimate_wpiBlue_MegaTag2(limelight.name);

            if (estimatedPosition == null || estimatedPosition.tagCount == 0
                    || Math.abs(gyro.getAngularVelocityZWorld().getValueAsDouble()) > 720)
                continue;// reject measurements if not seeing a tag or going too fast
            double timeStamp = drivetrain.getState().Timestamp - estimatedPosition.latency / 1000;
            drivetrain.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, Integer.MAX_VALUE));
            drivetrain.addVisionMeasurement(estimatedPosition.pose,
                    timeStamp);

        }
    }
}