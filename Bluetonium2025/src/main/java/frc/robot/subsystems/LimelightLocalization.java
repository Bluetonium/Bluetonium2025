package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.helperclasses.LimelightHelpers;

public class LimelightLocalization extends SubsystemBase {
    private boolean tagDetected = false;
    private CommandSwerveDrivetrain drivetrain;
    private String[] limelights;

    /***
     * 
     * @param drivetrain the drive train - needed so we can add vision measurements
     * @param limelights names of the limelights that will be used
     */
    public LimelightLocalization(CommandSwerveDrivetrain drivetrain, String... limelights) {
        this.drivetrain = drivetrain;
        this.limelights = limelights;

        for (String limelight : limelights) {
            LimelightHelpers.setPipelineIndex(limelight, LimelightHelpers.pipelines.LOCALIZATION.ordinal());
        }
    }

    /***
     * 
     * @return if an april tag has been detected at some points since start
     */
    public boolean hasDetectedTag() {
        return tagDetected;
    }

    @Override
    public void periodic() {
        for (String limelight : limelights) {
            if (LimelightHelpers.getTV(limelight)) {
                tagDetected = true;
                double timeStamp = Timer.getFPGATimestamp() + LimelightHelpers.getLatency_Pipeline(limelight)
                        + LimelightHelpers.getLatency_Capture(limelight);
                drivetrain.addVisionMeasurement(LimelightHelpers.getBotPose2d_wpiBlue(limelight), timeStamp);
            }
        }
    }
}