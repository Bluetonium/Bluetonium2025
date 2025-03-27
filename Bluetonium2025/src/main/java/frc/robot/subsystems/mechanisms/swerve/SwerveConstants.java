package frc.robot.subsystems.mechanisms.swerve;

import com.ctre.phoenix6.Orchestra;
import com.pathplanner.lib.path.PathConstraints;

public class SwerveConstants {
    public static final PathConstraints autoAlignmentConstraints = new PathConstraints(3, 6, 3, 3);
    public static final Orchestra orchestra = new Orchestra();
}
