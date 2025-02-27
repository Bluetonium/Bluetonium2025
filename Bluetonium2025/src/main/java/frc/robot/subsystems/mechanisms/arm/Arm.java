package frc.robot.subsystems.mechanisms.arm;


import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotSim;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.utils.sim.ArmSim;
import lombok.Getter;

public class Arm extends SubsystemBase {
    @Getter
    private ArmSim armSim;
    private TalonFX arm;
    private TalonFXConfiguration armConfig;
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);
    @Getter
    private ArmPositions targetPosition = ArmPositions.HOME;

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every
     * function</h1>
     * <img src=
     * "https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png"
     * id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Arm() {
        arm = new TalonFX(ArmConstants.ARM_MOTOR_CAN_ID);
        arm.setNeutralMode(ArmConstants.ARM_MOTOR_NEUTRAL_MODE);

        armConfig = new TalonFXConfiguration();

        SoftwareLimitSwitchConfigs limitSwitch = armConfig.SoftwareLimitSwitch;
        limitSwitch.ForwardSoftLimitEnable = true;
        limitSwitch.ForwardSoftLimitThreshold = Units.degreesToRotations(ArmConstants.MAX_ANGLE)
                * ArmConstants.GEAR_RATIO;
        limitSwitch.ReverseSoftLimitEnable = true;
        limitSwitch.ReverseSoftLimitThreshold = Units.degreesToRotations(ArmConstants.MIN_ANGLE)
                * ArmConstants.GEAR_RATIO;

        // PID
        Slot0Configs slot0 = armConfig.Slot0;
        slot0.kP = ArmConstants.kP;
        slot0.kI = ArmConstants.kI;
        slot0.kD = ArmConstants.kD;
        slot0.kV = ArmConstants.kV;
        slot0.kS = ArmConstants.kS;
        slot0.kA = ArmConstants.kA;
        slot0.kG = ArmConstants.kG;

        MotionMagicConfigs motionMagic = armConfig.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 160;
        motionMagic.MotionMagicAcceleration = 6000;
        motionMagic.MotionMagicJerk = 1600;

        armSim = new ArmSim(ArmConstants.SIM_CONFIG, RobotSim.leftView, arm.getSimState(), "Arm");
        applyConfig();
        SendableRegistry.add(this, "Arm");
        SmartDashboard.putData(this);
    }
    private void applyConfig() {
        StatusCode status = arm.getConfigurator().apply(armConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to arm" + status.getDescription(), false);
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Shoulder");
        builder.addStringProperty("Target Position", () -> targetPosition.name(), null);
    }

    @Override
    public void simulationPeriodic() { // man idfkcv
        armSim.simulationPeriodic();
        // TODO: figure out a better way to do this!!!!
        armSim.getConfig().setInitialX(0.81 +
                armSim.getConfig().getMount().getDisplacementX() * 1.1);
        armSim.getConfig().setInitialY(0.35 +
                armSim.getConfig().getMount().getDisplacementY() * 1.1);
    }

    public void setup() {
        ArmStates.setStates();
    }

    public Command setArmPosition(ArmPositions position) {
        return run(() -> {
            final MotionMagicVoltage request = mmVoltage;
            arm.setControl(request.withPosition(position.rotations));
            targetPosition = position;
        }).withName("Arm Target Position");
    }
}
