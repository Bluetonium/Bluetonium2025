package frc.robot.subsystems.mechanisms.outtake;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

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

import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
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

    private final MutVoltage m_appliedVoltage = Volts.mutable(0);
    private final MutAngle m_angle = Radians.mutable(0);
    private final MutAngularVelocity m_velocity = RadiansPerSecond.mutable(0);
    private final SysIdRoutine m_sysIdRoutine =
      new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          new SysIdRoutine.Config(),
          new SysIdRoutine.Mechanism(
              // Tell SysId how to plumb the driving voltage to the motor(s).
              motor::setVoltage,
              // Tell SysId how to record a frame of data for each motor on the mechanism being
              // characterized.
              log -> {
                // Record a frame for the shooter motor.
                log.motor("outtaee")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            motor.get() * RobotController.getBatteryVoltage(), Volts))
                    .angularPosition(m_angle.mut_replace(motor.getEncoder().getPosition(), Rotations))
                    .angularVelocity(
                        m_velocity.mut_replace(motor.getEncoder().getVelocity(), RotationsPerSecond));
              },
              // Tell SysId to make generated commands require this subsystem, suffix test state in
              // WPILog with this subsystem's name ("shooter")
              this));
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
    
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
    
}
