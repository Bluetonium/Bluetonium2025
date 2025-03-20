package frc.robot.subsystems.limelight;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class LimelightConfig {
    public final String name;
    public Translation3d position;
    public Rotation3d rotation;

    public LimelightConfig(String name, Translation3d position, Rotation3d rotation) {
        this.name = name;
        this.position = position;
        this.rotation = rotation;
    }

}
