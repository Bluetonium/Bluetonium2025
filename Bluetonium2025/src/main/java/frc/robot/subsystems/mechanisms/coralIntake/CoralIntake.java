package frc.robot.subsystems.mechanisms.coralIntake;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.sim.RollerSim;
import frc.robot.RobotSim;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;


public class CoralIntake extends SubsystemBase implements NTSendable {
    private RollerSim intakeSim;

    private TalonFX motor;

    private TalonFXConfiguration motorConfig;

    private final MotionMagicVelocityVoltage VelocityVoltage = new MotionMagicVelocityVoltage(0).withAcceleration(CoralIntakeConstants.acceleration);

    public void initSendable(NTSendableBuilder builder){
        builder.setSmartDashboardType("CoralIntake");
        builder.addDoubleProperty("Velocity", () -> motor.getVelocity().getValueAsDouble(), null);
    }

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public CoralIntake() {
        // dumfayce
        motor = new TalonFX(CoralIntakeConstants.CORAL_INTAKE_MOTOR_CAN_ID);
        motor.setNeutralMode(CoralIntakeConstants.CORAL_INTAKE_MOTOR_NEUTRAL_MODE);

        motorConfig = new TalonFXConfiguration();

        // PID
        Slot0Configs slot0 = motorConfig.Slot0;
        slot0.kP = CoralIntakeConstants.kP;
        slot0.kI = CoralIntakeConstants.kI;
        slot0.kD = CoralIntakeConstants.kD;

        applyConfig();

        intakeSim = new RollerSim(CoralIntakeConstants.SIM_CONFIG, RobotSim.leftView, motor.getSimState(), "Coral Intake");

        SendableRegistry.add(this, "Outtake");
        SmartDashboard.putData(this);
    }

    private void applyConfig() {
        StatusCode status = motor.getConfigurator().apply(motorConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to coral intake" + status.getDescription(), false);
        }
    }

    @Override
    public void simulationPeriodic() { // man idfkcv
        intakeSim.simulationPeriodic();
    }

    public void setup() {
        setDefaultCommand(stopIntake());
        CoralIntakeStates.setStates();
    }

    public Command stopIntake() {
        return run(() -> {
            motor.stopMotor();
        }).withName("Coral Intake Stopped");
    }

    public Command setIntakeVelocity(double velocity) {
        return run(() -> {
            final MotionMagicVelocityVoltage request = VelocityVoltage;
            motor.setControl(request.withVelocity(velocity));
        }).withName("Coral Intake Running");
    }
}
