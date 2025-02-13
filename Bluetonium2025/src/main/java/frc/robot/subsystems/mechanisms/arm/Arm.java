package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Arm implements Subsystem, NTSendable {
    
    private SparkMax arm;
    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Arm() {
        // dumfayce
        arm = new SparkMax(0, MotorType.kBrushless);
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        
    }

    public Command setArmPosition(double position) {
        return run(() -> {
            arm.getClosedLoopController().setReference(position,ControlType.kPosition);
        }).withName("Arm Target Position");
    }
}
