package frc.robot.subsystems.limelight;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class LimelightConstants {
    // TODO setup the position of the limelights
    public static final LimelightConfig UPPER_LL = new LimelightConfig("upper",
            new Translation3d(0.3175, -0.0127, 0.8382),
            new Rotation3d(0, 0. - 30.0, 0.0));

    public static final LimelightConfig LOWER_LEFT_LL = new LimelightConfig("lowerleft",
            new Translation3d(0.3175, -0.0127, 0.8382),
            new Rotation3d(0, 0. - 30.0, 0.0));
    public static final LimelightConfig LOWER_RIGHT_LL = new LimelightConfig("lowerright",
            new Translation3d(0.3175, -0.0127, 0.8382),
            new Rotation3d(0, 0. - 30.0, 0.0));

    public static enum Pipelines {
        LOCALIZATION(0),
        REEF_TARGETING(1),
        CORAL_STATION_TARGETING(2);

        public final int pipeline;

        private Pipelines(int pipeline) {
            this.pipeline = pipeline;
        }
    }
}