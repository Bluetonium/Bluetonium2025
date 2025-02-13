package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Arm implements Subsystem, NTSendable {
    
    private SparkMax arm;
    private SparkMaxConfig armConfig;
    private SparkClosedLoopController armPID;
    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Arm() {
        // dumfayce
        arm = new SparkMax(ArmConstants.ARM_MOTOR_CAN_ID, MotorType.kBrushless);
        armConfig = new SparkMaxConfig();
        armPID = arm.getClosedLoopController();
        // maybe feedback sensor or something
        armConfig.closedLoop //TODO: fuckin pid tune thiss
            .p(ArmConstants.kP)
            .i(ArmConstants.kI)
            .d(ArmConstants.kD);
            armConfig.idleMode(ArmConstants.ARM_MOTOR_IDLE_MODE);
        arm.configure(armConfig,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        
    }

    public Command setArmPosition(double position) {
        return run(() -> {
            armPID.setReference(position,ControlType.kPosition);
        }).withName("Arm Target Position");
    }
}
