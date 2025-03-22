package frc.robot.subsystems.mechanisms.intake;

import com.ctre.phoenix6.configs.Slot0Configs;
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
import frc.robot.subsystems.mechanisms.arm.Arm;
import frc.robot.subsystems.mechanisms.arm.ArmConstants;
import frc.robot.subsystems.mechanisms.outtake.OuttakeConstant;
import frc.utils.sim.RollerSim;

public class Intake extends SubsystemBase {
    private SparkMax intakeArm;
    private RollerSim intakeArmSim;
    // private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private SparkMaxConfig intakeArmConfig;
    private SparkClosedLoopController intakeArmClosedLoopController;

    private SparkMax intakeRoller;
    private RollerSim intakeRollerSim;
    // private final VoltageOut m_sysIdControl = new VoltageOut(0);
    private SparkMaxConfig intakeRollerConfig;
    
    private DigitalInput coralSensor;
    

    // private MotionMagicVelocityVoltage mmVelocityVoltage = new
    // MotionMagicVelocityVoltage(0)
    // .withAcceleration(OuttakeConstant.acceleration);

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("Intake");
    }

    /*
     * private final SysIdRoutine m_sysIdRoutine =
     * new SysIdRoutine(
     * // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
     * new SysIdRoutine.Config(),
     * new SysIdRoutine.Mechanism(
     * // Tell SysId how to plumb the driving voltage to the motor(s).
     * motor::setVoltage,
     * // Tell SysId how to record a frame of data for each motor on the mechanism
     * being
     * // characterized.
     * log -> {
     * // Record a frame for the shooter motor.
     * log.motor("outtaee")
     * .voltage(
     * m_appliedVoltage.mut_replace(
     * motor.get() * RobotController.getBatteryVoltage(), Volts))
     * .angularPosition(m_angle.mut_replace(motor.getEncoder().getPosition(),
     * Rotations))
     * .angularVelocity(
     * m_velocity.mut_replace(motor.getEncoder().getVelocity(),
     * RotationsPerSecond));
     * },
     * // Tell SysId to make generated commands require this subsystem, suffix test
     * state in
     * // WPILog with this subsystem's name ("shooter")
     * this));
     */
    public Intake() {
        //arm config
        intakeArm = new SparkMax(IntakeConstants.INTAKE_ARM_MOTOR_CAN_ID, MotorType.kBrushless);
        intakeArmConfig = new SparkMaxConfig();
        intakeArmConfig.closedLoop
                .p(ArmConstants.kP)
                .i(ArmConstants.kI)
                .d(ArmConstants.kD);
        intakeArmConfig.idleMode(IdleMode.kBrake);

        applyIntakeArmConfig();

        //roller config
        intakeRoller = new SparkMax(IntakeConstants.INTAKE_ROLLER_MOTOR_CAN_ID, MotorType.kBrushless);
        intakeRollerConfig = new SparkMaxConfig();
        intakeRollerConfig.idleMode(IdleMode.kBrake);

        applyIntakeRollerConfig();

        coralSensor = new DigitalInput(IntakeConstants.CORAL_SENSOR_CHANNEL);

        // sim = new RollerSim(OuttakeConstant.ROLLER_SIM_CONFIG, RobotSim.rightView,
        // motor.getSimState(), "Outtake");

        SendableRegistry.add(this, "Intake");
        SmartDashboard.putData(this);
    }

    private void applyIntakeArmConfig() {
        REVLibError error = intakeArm.configure(intakeArmConfig, ResetMode.kResetSafeParameters,
                PersistMode.kNoPersistParameters);

        if (error.equals(REVLibError.kOk)) {
            DriverStation.reportWarning(
                    error.name() + "Failed to apply configs to arm", false);
        }
    }

    private void applyIntakeRollerConfig() {
        REVLibError error = intakeArm.configure(intakeArmConfig, ResetMode.kResetSafeParameters,
                PersistMode.kNoPersistParameters);

        if (error.equals(REVLibError.kOk)) {
            DriverStation.reportWarning(
                    error.name() + "Failed to apply configs to roller", false);
        }
    }

    public void setup() {
        setDefaultCommand(run(() -> {
            intakeArm.stopMotor();
            intakeRoller.stopMotor();
        }).withName("Stop"));
        IntakeStates.setupStates();
    }

    public Command setRollerVelocity(double velocity){
        return runOnce(() -> {
            intakeRoller.set(velocity);
        });
    }

    public Command setArmPosition(double targetAngle){
        double targetPosition = targetAngle/350.0 * IntakeConstants.GEAR_RATIO * IntakeConstants.ENCODER_CPR;
        return new FunctionalCommand(
                () -> {
                    intakeArmClosedLoopController.setReference(targetPosition,
                            ControlType.kPosition,
                            ClosedLoopSlot.kSlot0);
                },
                () -> {// nothing to do here
                },
                (interrupted) -> {
                    intakeArmClosedLoopController.setReference(0, ControlType.kPosition,
                            ClosedLoopSlot.kSlot0);
                },
                () -> {
                    return coralSensor.get();
                },
                this).withName("IntakeArmPosition");
    }

    @Override
    public void simulationPeriodic() {
        // sim.simulationPeriodic();
    }
    /*
     * public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
     * return m_sysIdRoutine.quasistatic(direction);
     * }
     * 
     * public Command sysIdDynamic(SysIdRoutine.Direction direction) {
     * return m_sysIdRoutine.dynamic(direction);
     * }
     */

}
