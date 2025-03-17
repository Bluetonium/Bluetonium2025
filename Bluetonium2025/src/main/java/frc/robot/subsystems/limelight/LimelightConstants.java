package frc.robot.subsystems.limelight;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class LimelightConstants {
    public static final LimelightConfig MAIN_LL = new LimelightConfig("",
            new Translation3d(0.3175, -0.0127, 0.8382),
            new Rotation3d(0, 0. - 30.0, 0.0));

    public static enum Pipelines {
        LOCALIZATION(0);

        public final int pipeline;

        private Pipelines(int pipeline) {
            this.pipeline = pipeline;
        }
    }
}