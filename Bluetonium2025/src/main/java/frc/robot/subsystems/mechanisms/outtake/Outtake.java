package frc.robot.subsystems.mechanisms.outtake;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.mechanisms.arm.ArmConstants;
import frc.utils.sim.RollerSim;

public class Outtake extends SubsystemBase {
    private SparkMax motor;
    private RollerSim sim;
    //private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private SparkMaxConfig motorConfig;
    private DigitalInput coralSensor;
    private SparkClosedLoopController closedLoopController;

    //private MotionMagicVelocityVoltage mmVelocityVoltage = new MotionMagicVelocityVoltage(0)
            //.withAcceleration(OuttakeConstant.acceleration);

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Outtake");
        // builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
    }

    /*
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
    */
    public Outtake() {
        motor = new SparkMax(OuttakeConstant.OUTTAKE_MOTOR_CAN_ID, MotorType.kBrushless);
        closedLoopController = motor.getClosedLoopController();
        motorConfig = new SparkMaxConfig();
        motorConfig.closedLoop
        .p(ArmConstants.kP)
        .i(ArmConstants.kI)
        .d(ArmConstants.kD);
        motorConfig.idleMode(IdleMode.kBrake);

        applyConfig();

        coralSensor = new DigitalInput(OuttakeConstant.CORAL_SENSOR_CHANNEL);

        //sim = new RollerSim(OuttakeConstant.ROLLER_SIM_CONFIG, RobotSim.rightView, motor.getSimState(), "Outtake");

        SendableRegistry.add(this, "Outtake");
        SmartDashboard.putData(this);
    }

    private void applyConfig() {
        REVLibError error = motor.configure(motorConfig,ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        if (error.equals(REVLibError.kOk)) {
            DriverStation.reportWarning(
                error.name() + "Failed to apply configs to arm", false);
        }
    }

    public void setup() {
        setDefaultCommand(run(() -> {
            motor.stopMotor();
        }).withName("Stop"));

        OuttakeStates.setupStates();
    }

    public Command outtakeAccept() {
        return new FunctionalCommand(
                () -> {
                    closedLoopController.setReference(OuttakeConstant.runningVelocity, ControlType.kMAXMotionVelocityControl,
                        ClosedLoopSlot.kSlot0);
                },
                () -> {// nothing to do here
                },
                (interupted) -> {
                    closedLoopController.setReference(0, ControlType.kMAXMotionVelocityControl,
                        ClosedLoopSlot.kSlot0);
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
                    closedLoopController.setReference(OuttakeConstant.runningVelocity, ControlType.kMAXMotionVelocityControl,
                        ClosedLoopSlot.kSlot0);
                },
                () -> {
                    if (!coralSensor.get() && ejectionTimer.isRunning()) {
                        ejectionTimer.start();
                    }
                },
                (interupted) -> {
                    closedLoopController.setReference(0, ControlType.kMAXMotionVelocityControl,
                        ClosedLoopSlot.kSlot0);                    
                        ejectionTimer.stop();
                },
                () -> {
                    return ejectionTimer.hasElapsed(OuttakeConstant.ejectionDelay);
                }, this).withName("Outtake Eject");
    }

    @Override
    public void simulationPeriodic() {
        sim.simulationPeriodic();
    }
    /*
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
    */
}
