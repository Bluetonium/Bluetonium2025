package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.sim.ArmSim;
import frc.robot.RobotSim;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;


public class Arm extends SubsystemBase implements NTSendable {
    private ArmSim armSim;
    private TalonFX arm;
    private TalonFXConfiguration armConfig;
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Arm() {
        // dumfayce
        arm = new TalonFX(ArmConstants.ARM_MOTOR_CAN_ID);
        arm.setNeutralMode(ArmConstants.ARM_MOTOR_NEUTRAL_MODE);

        armConfig = new TalonFXConfiguration();

        // PID
        Slot0Configs slot0 = armConfig.Slot0;
        slot0.kP = ArmConstants.kP;
        slot0.kI = ArmConstants.kI;
        slot0.kD = ArmConstants.kD;

        MotionMagicConfigs motionMagic = armConfig.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 160;
        motionMagic.MotionMagicAcceleration = 6000;
        motionMagic.MotionMagicJerk = 1600;

        armSim = new ArmSim(ArmConstants.SIM_CONFIG,RobotSim.leftView,arm.getSimState(),"Arm");

        applyConfig();
    }

    private void applyConfig() {
        StatusCode status = arm.getConfigurator().apply(armConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to arm" + status.getDescription(), false);
        }
    }
    @Override
    public void initSendable(NTSendableBuilder builder) {
    }

    @Override
    public void simulationPeriodic() { // man idfkcv
        armSim.simulationPeriodic();
    }

    public void setup() {
        ArmStates.setStates();
    }

    public Command setArmPosition(double position) {
        return run(() -> {
            final MotionMagicVoltage request = mmVoltage;
            arm.setControl(request.withPosition(position));
        }).withName("Arm Target Position");
    }
}
