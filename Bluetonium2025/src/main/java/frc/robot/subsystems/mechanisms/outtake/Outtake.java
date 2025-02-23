package frc.robot.subsystems.mechanisms.outtake;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotSim;
import frc.utils.sim.RollerSim;

public class Outtake extends SubsystemBase implements NTSendable {
    private TalonFX motor;
    private RollerSim sim;
    private TalonFXConfiguration motorConfig;
    private DigitalInput coralSensor;

    private MotionMagicVelocityVoltage mmVelocityVoltage = new MotionMagicVelocityVoltage(0)
            .withAcceleration(OuttakeConstant.acceleration);

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("Outtake");
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
    }

    public Outtake() {
        motor = new TalonFX(OuttakeConstant.OUTTAKE_MOTOR_CAN_ID);
        motor.setNeutralMode(OuttakeConstant.OUTTAKE_MOTOR_NEUTRAL_MODE);

        motorConfig = new TalonFXConfiguration();

        Slot0Configs slot0 = motorConfig.Slot0;
        slot0.kP = OuttakeConstant.kP;
        slot0.kI = OuttakeConstant.kI;
        slot0.kD = OuttakeConstant.kD;

        applyConfig();

        coralSensor = new DigitalInput(OuttakeConstant.CORAL_SENSOR_CHANNEL);

        sim = new RollerSim(OuttakeConstant.ROLLER_SIM_CONFIG, RobotSim.leftView, motor.getSimState(), "Outtake");

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
            motor.stopMotor();
        }).withName("Stop"));

        OuttakeStates.setupStates();
    }

    public Command outtakeAccept() {
        return new FunctionalCommand(
                () -> {
                    motor.setControl(mmVelocityVoltage.withVelocity(OuttakeConstant.runningVelocity));
                },
                () -> {// nothing to do here
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

                    motor.setControl(mmVelocityVoltage.withVelocity(OuttakeConstant.runningVelocity));
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
                    return ejectionTimer.hasElapsed(OuttakeConstant.ejectionDelay);
                }, this).withName("Outtake Eject");
    }

    @Override
    public void simulationPeriodic() {
        sim.simulationPeriodic();
    }
}
