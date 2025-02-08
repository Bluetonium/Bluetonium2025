package frc.robot.commands.teleop;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.mechanisms.elevator.Elevator;

public class TeleopElevator extends Command {
    Elevator arm;
    BooleanSupplier positionOne;
    BooleanSupplier positionTwo;

    @Deprecated // remove this and just have a bunch of commands to do
    public TeleopElevator(Elevator arm, BooleanSupplier positionOne, BooleanSupplier positionTwo) {
        addRequirements(arm);
        this.arm = arm;
        this.positionOne = positionOne;
        this.positionTwo = positionTwo;
    }

    @Override
    public void execute() {
        if (positionOne.getAsBoolean()) {
            arm.requestTargetPosition(0, false);
        } else if (positionTwo.getAsBoolean()) {
            arm.requestTargetPosition(1, false);
        }
    }

    @Override
    public void end(boolean interrupted) {
    }
}
