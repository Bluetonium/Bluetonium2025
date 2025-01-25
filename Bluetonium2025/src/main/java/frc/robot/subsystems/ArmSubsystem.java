package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Subsystem;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.constants.ArmConstants;

public class ArmSubsystem implements Subsystem {
    private TalonFX armMotor;
    private DigitalInput limitSwitchLow;
    private DigitalInput limitSwitchHigh;
    private TalonFXConfiguration config;
    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public ArmSubsystem() {
        //TODO: need to confirm if there's anything else to set
        armMotor = new TalonFX(ArmConstants.ARM_MOTOR_CAN_ID); //constants
        armMotor.setNeutralMode(ArmConstants.ARM_MOTOR_NEUTRAL_MODE);
        //PID bullshit!!!!
        Slot0Configs slot0 = config.Slot0;
        slot0.kS = 0.0;
        slot0.kV = 0.124;
        slot0.kA = 0.00;
        slot0.kP = 0.1;
        slot0.kI = 0;
        slot0.kD = 0.0;

        // whole lotta shit
        MotionMagicConfigs motionMagic = config.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 80;
        motionMagic.MotionMagicAcceleration = 40;        
        motionMagic.MotionMagicJerk = 1600;

        armMotor.getConfigurator().apply(config);

        limitSwitchHigh = new DigitalInput(ArmConstants.LIMIT_SWITCH_HIGH_ID); //TODO: figure out THE NUMBERS!!!!!!
        limitSwitchLow = new DigitalInput(ArmConstants.LIMIT_SWITCH_LOW_ID);
    }

    /**
     * <b>NOTE:</b> Be careful when using this. The arm isn't fragile but kraken motors are strong and fast, dude.
     * @param speed the value of speed you want
     * 
     */
    public void setArmSpeed(double speed) {
        armMotor.set(speed);
    }

    /**
     * 
     * @param rotations the position you want it to go to. Range from 0-1
     * @param inRotations if we're just doing raw rotations rather than 0-1
     */
    public void requestTargetPosition(double position, boolean inRotations) {
        if (inRotations) {
            position *= ArmConstants.RANGE_OF_ROTATIONS;
        }
        final MotionMagicVoltage request = new MotionMagicVoltage(0);
        armMotor.setControl(request.withPosition(position));
    }
    
    /**
     * Gets the arm position. <i>far as I'm aware, this is relative.</i>
     * @return The angle of the motor in rotations.
     * 
     */
    public double getArmPosition() {
        return armMotor.getPosition().getValueAsDouble();
    }

    /**
     * Sets the arm position. <i>Will probably be mainly used for the limit switches</i>
     * 
     */
    public void setArmPosition(double position) {
        armMotor.setPosition(position);
    }

    /**
     * @return Whether the high limit switch is pressed
     * 
     */
    public boolean getLimitSwitchHigh() {
        return limitSwitchHigh.get();
    }

    /**
     * @return Whether the low limit switch is pressed
     * 
     */
    public boolean getLimitSwitchLow() {
        return limitSwitchLow.get();
    }
}
