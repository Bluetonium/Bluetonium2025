package frc.robot.subsystems.mechanisms.elevator;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class Elevator implements Subsystem, NTSendable {
    private TalonFX motor;
    private TalonFXConfiguration config;
    private final VoltageOut m_sysIdControl = new VoltageOut(0);

    private final SysIdRoutine routine = new SysIdRoutine(
            new SysIdRoutine.Config(
                    null, // Use default ramp rate (1 V/s)
                    null,
                    null, // Use default timeout (10 s)
                          // Log state with Phoenix SignalLogger class
                    state -> SignalLogger.writeString("state", state.toString())),
            new SysIdRoutine.Mechanism(
                    volts -> motor.setControl(m_sysIdControl.withOutput(volts)),
                    null,
                    this));

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("Elevator");
        builder.addDoubleProperty("Position", () -> motor.getPosition().getValueAsDouble(), null);
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
        builder.addDoubleArrayProperty("#Scoring Positions", () -> ElevatorConstants.SCORING_POSITIONS,
                (ScoringPositions) -> ElevatorConstants.SCORING_POSITIONS = ScoringPositions);

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
        motor = new TalonFX(ElevatorConstants.ARM_MOTOR_CAN_ID); // constants
        motor.setNeutralMode(ElevatorConstants.ARM_MOTOR_NEUTRAL_MODE);

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
        limitswitch.ForwardSoftLimitThreshold = ElevatorConstants.RANGE_OF_ROTATIONS;
        limitswitch.ReverseSoftLimitEnable = true;
        limitswitch.ReverseSoftLimitThreshold = 0;

        applyConfig();

        SendableRegistry.add(this, "Elevator");
        SmartDashboard.putData(this);

        /*
         * BaseStatusSignal.setUpdateFrequencyForAll(250,
         * armMotor.getPosition(),
         * armMotor.getVelocity(),
         * armMotor.getMotorVoltage());
         * 
         * armMotor.optimizeBusUtilization();
         * 
         * SignalLogger.start();
         */
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
    public Command requestTargetPosition(double rotations) {
        return run(() -> {
            final MotionMagicVoltage request = new MotionMagicVoltage(0);
            motor.setControl(request.withPosition(rotations));
        }).withName("Elevator Target Position");

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
