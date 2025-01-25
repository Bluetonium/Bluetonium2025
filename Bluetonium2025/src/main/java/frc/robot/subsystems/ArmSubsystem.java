package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import com.ctre.phoenix6.hardware.TalonFX;

public class ArmSubsystem implements Subsystem {
    private TalonFX armMotor;
    public ArmSubsystem() {
        armMotor = new TalonFX(1); //constants
        armMotor.setControl();
    }
}
