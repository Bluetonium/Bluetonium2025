package frc.robot.subsystems.mechanisms.elevator;

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
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotSim;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants.ElevatorPositions;
import frc.utils.sim.LinearSim;
import lombok.Getter;

public class Elevator extends SubsystemBase {
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
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("Elevator");
        builder.addStringProperty("Target Position", () -> elevatorTargetPosition.name(), null);
        builder.addDoubleProperty("Position", this::getPosition, null);
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
        builder.addDoubleProperty("Current", () -> motor.getStatorCurrent().getValueAsDouble(), null);
    }

    private final SysIdRoutine m_sysIdRoutine = new SysIdRoutine(
            new SysIdRoutine.Config(
                    null, // Use default ramp rate (1 V/s)
                    Volts.of(4), // Reduce dynamic step voltage to 4 to prevent brownout
                    null, // Use default timeout (10 s)
                          // Log state with Phoenix SignalLogger class
                    (state) -> SignalLogger.writeString("SysIdElevator_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    (volts) -> motor.setControl(m_sysIdControl.withOutput(volts.in(Volts))),
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
    public Elevator() {
        motor = new TalonFX(ElevatorConstants.ELEVATOR_MOTOR_CAN_ID); // constants

        config = new TalonFXConfiguration();
        config.MotorOutput.NeutralMode = ElevatorConstants.ELEVATOR_MOTOR_NEUTRAL_MODE;
        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        config.CurrentLimits = ElevatorConstants.CURRENT_LIMITS;
        // PID
        Slot0Configs slot0 = config.Slot0;
        slot0.kS = ElevatorConstants.kS;
        slot0.kV = ElevatorConstants.kV;
        slot0.kA = ElevatorConstants.kA;
        slot0.kP = ElevatorConstants.kP;
        slot0.kG = ElevatorConstants.kG;
        slot0.kI = ElevatorConstants.kI;
        slot0.kD = ElevatorConstants.kD;

        // Motion Magic
        MotionMagicConfigs motionMagic = config.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = ElevatorConstants.CRUISE_VELOCITY;
        motionMagic.MotionMagicAcceleration = ElevatorConstants.ACCELERATION;

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
        sim = new LinearSim(ElevatorConstants.SIM_CONFIG, RobotSim.rightView, motor.getSimState(), "Elevator");
    }

    public void setup() {
        ElevatorStates.setStates();
        motor.stopMotor();
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
        return runOnce(() -> {
            final MotionMagicVoltage request = mmVoltage;
            motor.setControl(request.withPosition(position.rotations));
            elevatorTargetPosition = position;
        }).withName("Elevator Target Position");

    }

    public Command stopEverything() {
        return runOnce(() -> {
            motor.stopMotor();
        });
    }

    @Override
    public void simulationPeriodic() {
        sim.simulationPeriodic();
    }

    /**
     * 
     * @return current elevator position in inches
     */
    public double getPosition() {
        return motor.getPosition().getValueAsDouble() / ElevatorConstants.END_GEAR_RATIO;
    }

    /**
     * 
     * @return if the elevator is *close enough* to desired position
     */
    public boolean elevatorIsAtDesiredPosition() {
        return Math.abs(getPosition() - elevatorTargetPosition.inches) < ElevatorConstants.POSITION_TOLERANCE;

    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
}
