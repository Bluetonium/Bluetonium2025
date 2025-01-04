package frc.robot.commands.auton;



import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Swerve;

public class SpinWheelTest extends Command {
    private Swerve swerve;
    private double angle; //its in radians because i feel like it :)
    public SpinWheelTest(Swerve swerve) {
        addRequirements(swerve);
        this.swerve = swerve;
    }

    @Override
    public void execute() {
        //swerve.getModuleStates()[0].angle
        angle += 0.005;
        double dx = Math.sin(angle);
        double dy = Math.cos(angle);

        swerve.driveRobotReleative(new ChassisSpeeds(dx,dy,0));
    }
   
    @Override
    public boolean isFinished() {
        return angle>5;
    }
   
    @Override
    public void end(boolean interrupted) {
        swerve.driveRobotReleative(new ChassisSpeeds(0,0,0));
    }
}
