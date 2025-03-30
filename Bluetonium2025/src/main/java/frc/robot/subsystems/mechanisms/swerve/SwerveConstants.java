package frc.robot.subsystems.mechanisms.swerve;

import com.ctre.phoenix6.Orchestra;
import com.pathplanner.lib.path.PathConstraints;

public class SwerveConstants {
    public static final PathConstraints autoAlignmentConstraints = new PathConstraints(3, 6, 3, 3);
    public static final Orchestra orchestra = new Orchestra();

    public static double alignmentKp = -0.2;
    public static double alignmentMaxSpeed = 0.5;
    public static double alignmentMinSpeed = 0.1;
    public static final double SLOW_SPEED_MODIFIER = 0.2;
}
