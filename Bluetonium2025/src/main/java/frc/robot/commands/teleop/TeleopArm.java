package frc.robot.commands.teleop;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class TeleopArm extends Command {
    ArmSubsystem arm;
    BooleanSupplier positionOne;
    BooleanSupplier positionTwo;

    public TeleopArm(ArmSubsystem arm, BooleanSupplier positionOne, BooleanSupplier positionTwo) {
        addRequirements(arm);
        this.arm = arm;
        this.positionOne = positionOne;
        this.positionTwo = positionTwo;
    }
    
    @Override
    public void execute() {
        if (positionOne.getAsBoolean()) {
            arm.requestTargetPosition(0,false);
        } else if (positionTwo.getAsBoolean()) {
            arm.requestTargetPosition(1,false);
        }
    }
    @Override
    public void end(boolean interrupted) {
    }
}
