package frc.robot.subsystems.limelight;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class LimelightConstants {
    public static final LimelightConfig UPPER_LL = new LimelightConfig("upper",
            new Translation3d(Units.inchesToMeters(3.5), Units.inchesToMeters(-2), Units.inchesToMeters(35.5)),
            new Rotation3d(0, Math.toRadians(10), 0.0));
    public static final LimelightConfig LOWER_LEFT_LL = new LimelightConfig("lowerleft",
            new Translation3d(Units.inchesToMeters(8), Units.inchesToMeters(10.5), Units.inchesToMeters(10.5)),
            new Rotation3d(0, 10, Math.toRadians(-20)));
    public static final LimelightConfig LOWER_RIGHT_LL = new LimelightConfig("lowerright",
            new Translation3d(Units.inchesToMeters(8), Units.inchesToMeters(-10.5), Units.inchesToMeters(10.5)),
            new Rotation3d(0, 10, Math.toRadians(20)));

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