package frc.robot.subsystems.mechanisms.outtake;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotSim;
import frc.utils.sim.RollerSim;

public class Outtake extends SubsystemBase {
    private TalonFX motor;
    private RollerSim sim;
    private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private TalonFXConfiguration motorConfig;
    private DigitalInput coralSensor;

    private MotionMagicVelocityVoltage mmVelocityVoltage = new MotionMagicVelocityVoltage(0)
            .withAcceleration(OuttakeConstant.ACCELERATION);

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Outtake");
        builder.addDoubleProperty("Target Velocity", () -> mmVelocityVoltage.Velocity, null);
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
    }

    private final SysIdRoutine m_sysIdRoutine = new SysIdRoutine(
            new SysIdRoutine.Config(
                    null, // Use default ramp rate (1 V/s)
                    Volts.of(4), // Reduce dynamic step voltage to 4 to prevent brownout
                    null, // Use default timeout (10 s)
                          // Log state with Phoenix SignalLogger class
                    (state) -> SignalLogger.writeString("SysIdArm_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    (volts) -> motor.setControl(m_sysIdControl.withOutput(volts.in(Volts))),
                    null,
                    this));

    public Outtake() {
        motor = new TalonFX(OuttakeConstant.OUTTAKE_MOTOR_CAN_ID);
        motor.setNeutralMode(OuttakeConstant.OUTTAKE_MOTOR_NEUTRAL_MODE);

        motorConfig = new TalonFXConfiguration();
        motorConfig.CurrentLimits = OuttakeConstant.CURRENT_LIMITS;

        Slot0Configs slot0 = motorConfig.Slot0;
        slot0.kP = OuttakeConstant.kP;
        slot0.kI = OuttakeConstant.kI;
        slot0.kD = OuttakeConstant.kD;

        applyConfig();

        coralSensor = new DigitalInput(OuttakeConstant.CORAL_SENSOR_CHANNEL);

        sim = new RollerSim(OuttakeConstant.ROLLER_SIM_CONFIG, RobotSim.rightView, motor.getSimState(), "Outtake");

        SendableRegistry.add(this, "Outtake");
        SmartDashboard.putData(this);
    }

    private void applyConfig() {
        StatusCode status = motor.getConfigurator().apply(motorConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to outtake" + status.getDescription(), false);
        }
    }

    public void setup() {
        setDefaultCommand(run(() -> {
            motor.setControl(mmVelocityVoltage.withVelocity(0));
        }).withName("Outtake.Stopped"));

        OuttakeStates.setupStates();
    }

    public Command outtakeAccept() {
        return new FunctionalCommand(
                () -> {
                    motor.setControl(mmVelocityVoltage.withVelocity(OuttakeConstant.INTAKE_VELOCITY));
                },
                () -> {
                },
                (interupted) -> {
                    motor.setControl(mmVelocityVoltage.withVelocity(0));
                },
                () -> {
                    return coralSensor.get();
                },
                this).withName("OutakeAccept");
    }

    public Command outtakeEject() {
        Timer ejectionTimer = new Timer();
        return new FunctionalCommand(
                () -> {
                    ejectionTimer.reset();

                    motor.setControl(mmVelocityVoltage.withVelocity(OuttakeConstant.OUTTAKE_VELOCITY));
                },
                () -> {
                    if (!coralSensor.get() && ejectionTimer.isRunning()) {
                        ejectionTimer.start();
                    }
                },
                (interupted) -> {
                    motor.setControl(mmVelocityVoltage.withVelocity(0));
                    ejectionTimer.stop();
                },
                () -> {
                    return ejectionTimer.hasElapsed(OuttakeConstant.EJECTION_DELAY);
                }, this).withName("Outtake Eject");
    }

    @Override
    public void simulationPeriodic() {
        sim.simulationPeriodic();
    }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
}
