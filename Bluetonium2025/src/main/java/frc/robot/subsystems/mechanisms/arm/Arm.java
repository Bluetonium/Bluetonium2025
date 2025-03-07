package frc.robot.subsystems.mechanisms.arm;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotSim;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.utils.sim.ArmSim;
import lombok.Getter;

public class Arm extends SubsystemBase {
    @Getter
    private ArmSim armSim;
    private DutyCycleEncoder absoluteEncoder;
    private TalonFX arm;
    private TalonFXConfiguration armConfig;
    private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);
    @Getter
    private ArmPositions targetPosition = ArmPositions.HOME;
    private final SysIdRoutine m_sysIdRoutine = new SysIdRoutine(
            new SysIdRoutine.Config(
                    Volts.of(0.1).per(Second),
                    Volts.of(0.4), // Reduce dynamic step voltage to 4 to prevent brownout
                    null, // Use default timeout (10 s)
                          // Log state with Phoenix SignalLogger class
                    (state) -> SignalLogger.writeString("SysIdArm_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    (volts) -> arm.setControl(m_sysIdControl.withOutput(volts.in(Volts))),
                    null,
                    this));

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

        absoluteEncoder = new DutyCycleEncoder(ArmConstants.ABSOLUTE_ENCODER_CHANNEL);
        armConfig = new TalonFXConfiguration();
        armConfig.MotorOutput.NeutralMode = ArmConstants.ARM_MOTOR_NEUTRAL_MODE;

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

        armSim = new ArmSim(ArmConstants.SIM_CONFIG, RobotSim.rightView, arm.getSimState(), "Arm");
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
        super.initSendable(builder);
        builder.setSmartDashboardType("Arm");
        builder.addStringProperty("Target Position", () -> targetPosition.name(), null);
        builder.addDoubleProperty("Current Position", this::getPosition, null);
        builder.addDoubleProperty("Absolute Encoder Position", () -> absoluteEncoder.get(), null);

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
        double currentPos = absoluteEncoder.get() - ArmConstants.ABSOLUTE_ENCODER_OFFSET;
        arm.setPosition(currentPos * ArmConstants.GEAR_RATIO);
        arm.stopMotor();
        ArmStates.setStates();
    }

    public Command setArmPosition(ArmPositions position) {
        return runOnce(() -> {
            final MotionMagicVoltage request = mmVoltage;
            arm.setControl(request.withPosition(position.rotations));
            targetPosition = position;
        }).withName("Arm Target Position");
    }

    /**
     * @return current arm position in radians
     */

    public double getPosition() {
        return Units.rotationsToRadians(arm.getPosition().getValueAsDouble() / ArmConstants.GEAR_RATIO);
    }

    public boolean armIsAtDesiredPosition() {
        return Math.abs(getPosition() - targetPosition.angle) < ArmConstants.POSITION_TOLERANCE;
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
}
