package frc.robot.subsystems.mechanisms.arm;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.sim.ArmSim;
import frc.robot.RobotSim;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;


public class Arm extends SubsystemBase implements NTSendable {
    private ArmSim armSim;
    private TalonFX arm;
    private TalonFXConfiguration armConfig;
    private SparkClosedLoopController armPID;
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
        // it goes num of motors, gear ratio, moment of inertia(lol)
        //armSim = new SingleJointedArmSim(DCMotor.getNeo550(1), 0, 0, 0, 0, 90, false, 0, null);
        armSim = new ArmSim(ArmConstants.SIM_CONFIG,RobotSim.leftView,arm.getSimState(),"Arm");

        applyConfig();
    }

    private void applyConfig() {
        StatusCode status = arm.getConfigurator().apply(armConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to elevator" + status.getDescription(), false);
        }
    }
    @Override
    public void initSendable(NTSendableBuilder builder) {
    }

    @Override
    public void simulationPeriodic() { // man idfk
        armSim.simulationPeriodic();
    }

    public void setup() {
        ArmStates.setStates();
    }

    public Command setArmPosition(double position) {
        return run(() -> {
            armPID.setReference(position,ControlType.kPosition);
        }).withName("Arm Target Position");
    }
}
