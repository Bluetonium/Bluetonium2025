package frc.robot.subsystems.mechanisms.shoulder;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.sim.ArmSim;
import lombok.Getter;
import frc.robot.RobotSim;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

public class Shoulder extends SubsystemBase implements NTSendable {
    @Getter
    private ArmSim armSim;
    private TalonFX arm;
    private TalonFXConfiguration armConfig;
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every
     * function</h1>
     * <img src=
     * "https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png"
     * id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Shoulder() {
        // dumfayce
        arm = new TalonFX(ShoulderConstants.ARM_MOTOR_CAN_ID);
        arm.setNeutralMode(ShoulderConstants.ARM_MOTOR_NEUTRAL_MODE);

        armConfig = new TalonFXConfiguration();
        armConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        SoftwareLimitSwitchConfigs limitSwitch = armConfig.SoftwareLimitSwitch;
        limitSwitch.ForwardSoftLimitEnable = true;
        limitSwitch.ForwardSoftLimitThreshold = Units.degreesToRotations(ShoulderConstants.MAX_ANGLE)
                * ShoulderConstants.GEAR_RATIO;
        limitSwitch.ReverseSoftLimitEnable = true;
        limitSwitch.ReverseSoftLimitThreshold = Units.degreesToRotations(ShoulderConstants.MIN_ANGLE)
                * ShoulderConstants.GEAR_RATIO;

        // PID
        Slot0Configs slot0 = armConfig.Slot0;
        slot0.kP = ShoulderConstants.kP;
        slot0.kI = ShoulderConstants.kI;
        slot0.kD = ShoulderConstants.kD;
        slot0.kV = ShoulderConstants.kV;
        slot0.kS = ShoulderConstants.kS;
        slot0.kA = ShoulderConstants.kA;
        slot0.kG = ShoulderConstants.kG;

        MotionMagicConfigs motionMagic = armConfig.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 160;
        motionMagic.MotionMagicAcceleration = 6000;
        motionMagic.MotionMagicJerk = 1600;

        armSim = new ArmSim(ShoulderConstants.SIM_CONFIG, RobotSim.leftView, arm.getSimState(), "Arm");

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
        // TODO: figure out a better way to do this!!!!
        // armSim.getConfig().setInitialX(0.81 +
        // armSim.getConfig().getMount().getDisplacementX() * 1.1);
        // armSim.getConfig().setInitialY(0.35 +
        // armSim.getConfig().getMount().getDisplacementY() * 1.1);
    }

    public void setup() {
        ShoulderStates.setStates();
    }

    public Command setArmAngle(double degrees) {
        return run(() -> {
            final MotionMagicVoltage request = mmVoltage;
            arm.setControl(request.withPosition(Units.degreesToRotations(degrees) * ShoulderConstants.GEAR_RATIO));
        }).withName("Arm Target Position");
    }
}
