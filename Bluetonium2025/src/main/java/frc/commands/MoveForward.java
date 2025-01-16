package frc.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.LimelightLocalization;

public class MoveForward extends Command{
    private CommandSwerveDrivetrain driveTrain;
    private LimelightLocalization limelight;
    private Command currentAuto;
    private Timer timer;
    private final SwerveRequest.ApplyRobotSpeeds drive = new SwerveRequest.ApplyRobotSpeeds();

    public  MoveForward(CommandSwerveDrivetrain driveTrain, LimelightLocalization limelight, Command currentAuto){
        
            
        this.driveTrain = driveTrain;

        addRequirements(driveTrain);
        this.limelight = limelight;
        this.currentAuto = currentAuto;

        timer = new Timer();
    }
    
    @Override
    public void initialize() {
        timer.start();

        driveTrain.setControl(drive.withSpeeds(new ChassisSpeeds(0.5,0,0)));
     
        
    }
    

    @Override
    public void execute() {
        if (timer.hasElapsed(3)){
            driveTrain.setControl(drive.withSpeeds(new ChassisSpeeds(0,0,0)));
        }
    }

    @Override
    public boolean isFinished() {
        return limelight.hasDetectedTag();
    }

    @Override
    public void end(boolean interrupted) {
        if(!interrupted) {
            CommandScheduler.getInstance().schedule(currentAuto);
        }
    }
}
