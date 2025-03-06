package frc.robot.subsystems.mechanisms.arm;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.subsystems.mechanisms.arm.ArmConstants.ArmPositions;
import frc.robot.subsystems.mechanisms.elevator.ElevatorConstants;
import frc.robot.subsystems.mechanisms.elevator.ElevatorStates;
import frc.utils.sim.ArmSim;
import lombok.Getter;

public class Arm extends SubsystemBase {
    @Getter
    private ArmSim armSim;
    private double desiredAngle;

    private SparkMax arm;
    private SparkClosedLoopController closedLoopController;
    private SparkMaxConfig armConfig;
    @Getter
    private ArmPositions targetPosition = ArmPositions.HOME;

    private final MutVoltage m_appliedVoltage = Volts.mutable(0);
    private final MutAngle m_angle = Radians.mutable(0);
    private final MutAngularVelocity m_velocity = RadiansPerSecond.mutable(0);
    private final SysIdRoutine m_sysIdRoutine =
      new SysIdRoutine(
          // Empty config defaults to 1 volt/second ramp rate and 7 volt step voltage.
          new SysIdRoutine.Config(),
          new SysIdRoutine.Mechanism(
              // Tell SysId how to plumb the driving voltage to the motor(s).
              arm::setVoltage,
              // Tell SysId how to record a frame of data for each motor on the mechanism being
              // characterized.
              log -> {
                // Record a frame for the shooter motor.
                log.motor("arm")
                    .voltage(
                        m_appliedVoltage.mut_replace(
                            arm.get() * RobotController.getBatteryVoltage(), Volts))
                    .angularPosition(m_angle.mut_replace(arm.getEncoder().getPosition(), Rotations))
                    .angularVelocity(
                        m_velocity.mut_replace(arm.getEncoder().getVelocity(), RotationsPerSecond));
              },
              // Tell SysId to make generated commands require this subsystem, suffix test state in
              // WPILog with this subsystem's name ("shooter")
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
        arm = new SparkMax(ArmConstants.ARM_MOTOR_CAN_ID,MotorType.kBrushless);
        closedLoopController = arm.getClosedLoopController();

        armConfig = new SparkMaxConfig();
        armConfig.softLimit
        .forwardSoftLimitEnabled(true)
        .forwardSoftLimit(Units.degreesToRotations(ArmConstants.MAX_ANGLE) * ArmConstants.GEAR_RATIO)
        .reverseSoftLimitEnabled(true)
        .reverseSoftLimit(Units.degreesToRotations(ArmConstants.MIN_ANGLE) * ArmConstants.GEAR_RATIO);
        /*
        SoftwareLimitSwitchConfigs limitSwitch = armConfig.SoftwareLimitSwitch;
        limitSwitch.ForwardSoftLimitEnable = true;
        limitSwitch.ForwardSoftLimitThreshold = Units.degreesToRotations(ArmConstants.MAX_ANGLE)
                * ArmConstants.GEAR_RATIO;
        limitSwitch.ReverseSoftLimitEnable = true;
        limitSwitch.ReverseSoftLimitThreshold = Units.degreesToRotations(ArmConstants.MIN_ANGLE)
                * ArmConstants.GEAR_RATIO;
        */
        // PID
        armConfig.closedLoop
        .p(ArmConstants.kP)
        .i(ArmConstants.kI)
        .d(ArmConstants.kD);

        armConfig.idleMode(IdleMode.kBrake);

        armConfig.closedLoop.maxMotion
        .maxVelocity(160)
        .maxAcceleration(600);
        //TODO: sim for neo arm!!!
        //armSim = new ArmSim(ArmConstants.SIM_CONFIG, RobotSim.rightView, arm.getSimState(), "Arm");
        applyConfig();
        SendableRegistry.add(this, "Arm");
        SmartDashboard.putData(this);
    }

    private void applyConfig() {
        REVLibError error = arm.configure(armConfig,ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        if (error.equals(REVLibError.kOk)) {
            DriverStation.reportWarning(
                error.name() + "Failed to apply configs to arm", false);
        }
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Shoulder");
        builder.addStringProperty("Target Position", () -> targetPosition.name(), null);
        builder.addDoubleProperty("Current Position", this::getPosition, null);
    }
    /*
    @Override
    public void simulationPeriodic() { // man idfkcv
        armSim.simulationPeriodic();
        // TODO: figure out a better way to do this!!!!
        armSim.getConfig().setInitialX(0.81 +
                armSim.getConfig().getMount().getDisplacementX() * 1.1);
        armSim.getConfig().setInitialY(0.35 +
                armSim.getConfig().getMount().getDisplacementY() * 1.1);
    }
    */

    public void setup() {
        ArmStates.setStates();
    }

    public Command setArmPosition(ArmPositions position) {
        closedLoopController.setReference(position.rotations, ControlType.kMAXMotionPositionControl,
          ClosedLoopSlot.kSlot0);

        desiredAngle = position.angle / ArmConstants.GEAR_RATIO;
        return Commands.waitUntil(() -> isSafeToMove(position)).andThen(
                runOnce(() -> {
                    closedLoopController.setReference(position.rotations, ControlType.kMAXMotionPositionControl,
                        ClosedLoopSlot.kSlot0);                    
                    targetPosition = position;
                }).withName("Arm Target Position"));
    }

    /**
     * @return current arm position in radians
     */

    public double getPosition() {
        return Units.rotationsToRadians(arm.getEncoder().getPosition() / ArmConstants.GEAR_RATIO);
    }

    private boolean isSafeToMove(ArmPositions targetPosition) {
        double armY = Math.sin(targetPosition.angle) * ArmConstants.ARM_LENGTH;
        double elevatorY = Math.sin(ElevatorConstants.MOUNTING_ANGLE) * ElevatorStates.elevatorPosition.getAsDouble();
        return (armY + elevatorY) > 6;
    }

    public boolean armIsAtDesiredPosition() {
        return Math.abs(getPosition()-desiredAngle)<ArmConstants.POSITION_TOLERANCE;
    }
    
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.quasistatic(direction);
    }

    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutine.dynamic(direction);
    }
}
