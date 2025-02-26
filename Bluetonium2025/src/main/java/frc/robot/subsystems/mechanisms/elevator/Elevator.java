package frc.robot.subsystems.mechanisms.elevator;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotSim;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants.ElevatorPositions;
import frc.utils.sim.LinearSim;
import lombok.Getter;

public class Elevator extends SubsystemBase implements NTSendable {
    private TalonFX motor;
    private TalonFXConfiguration config;
    private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);
    @Getter
    private ElevatorPositions elevatorTargetPosition = ElevatorPositions.HOME;

    // SIM
    @Getter
    private final LinearSim sim;

    @Override
    public void initSendable(NTSendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("Elevator");
        builder.addStringProperty("Position", () -> elevatorTargetPosition.name(), null);
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);

    }

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every
     * function</h1>
     * <img src=
     * "https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png"
     * id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public Elevator() {
        // TODO: need to confirm if there's anything else to set
        motor = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_CAN_ID); // constants
        motor.setNeutralMode(ElevatorConstants.ELEVATOR_MOTOR_NEUTRAL_MODE);

        config = new TalonFXConfiguration();

        // PID
        Slot0Configs slot0 = config.Slot0;
        slot0.kS = ElevatorConstants.kS;
        slot0.kV = ElevatorConstants.kV;
        slot0.kA = ElevatorConstants.kA;
        slot0.kP = ElevatorConstants.kP;
        // slot0.kG = something, TODO: figure this value as it gave a negative lol
        slot0.kI = ElevatorConstants.kI;
        slot0.kD = ElevatorConstants.kD;

        // Motion Magic
        MotionMagicConfigs motionMagic = config.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 160;
        motionMagic.MotionMagicAcceleration = 80;
        motionMagic.MotionMagicJerk = 1600;

        // SoftLimits
        SoftwareLimitSwitchConfigs limitswitch = config.SoftwareLimitSwitch;
        limitswitch.ForwardSoftLimitEnable = true;
        limitswitch.ForwardSoftLimitThreshold = ElevatorConstants.HIGH_POSITION *
                ElevatorConstants.END_GEAR_RATIO;
        limitswitch.ReverseSoftLimitEnable = true;
        limitswitch.ReverseSoftLimitThreshold = 0;

        applyConfig();

        SendableRegistry.add(this, "Elevator");
        SmartDashboard.putData(this);

        // SIM
        sim = new LinearSim(ElevatorConstants.SIM_CONFIG, RobotSim.leftView, motor.getSimState(), "Elevator");
    }

    public void setup() {
        ElevatorStates.setStates();
    }

    private void applyConfig() {
        StatusCode status = motor.getConfigurator().apply(config);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to elevator" + status.getDescription(), false);
        }
    }

    /**
     * 
     * @param rotations   the position you want it to go to. Range from 0-1
     * @param inRotations if we're just doing raw rotations rather than 0-1
     */
    public Command requestTargetPosition(ElevatorPositions position) {

        return run(() -> {
            final MotionMagicVoltage request = mmVoltage;
            motor.setControl(request.withPosition(position.rotations));
            elevatorTargetPosition = position;
        }).withName("Elevator Target Position");

    }

    @Override
    public void simulationPeriodic() {
        sim.simulationPeriodic();
    }

    /*
     * public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
     * return routine.quasistatic(direction);
     * }
     * public Command sysIdDynamic(SysIdRoutine.Direction direction) {
     * return routine.dynamic(direction);
     * }
     */
}
