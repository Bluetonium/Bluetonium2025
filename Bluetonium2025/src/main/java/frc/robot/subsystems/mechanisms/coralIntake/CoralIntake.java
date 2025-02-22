package frc.robot.subsystems.mechanisms.coralIntake;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.sim.RollerSim;
import frc.robot.RobotSim;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;


public class CoralIntake extends SubsystemBase implements NTSendable {
    private RollerSim intakeSim;
    private TalonFX intake;
    private TalonFXConfiguration intakeConfig;
    // Use VelocityFLC instead **
    private final MotionMagicVoltage mmVoltage = new MotionMagicVoltage(0);

    /**
     * <h1>i'm only adding this because it'd feel weird if i didn't add it to every function</h1>
     * <img src="https://static.wikia.nocookie.net/qualdies-methlab/images/3/31/Soggycat.png" id="yes" alt="its supposed to be a soggy cat but you're probably offline">
     * 
     */
    public CoralIntake() {
        // dumfayce
        intake = new TalonFX(CoralIntakeConstants.CORAL_INTAKE_MOTOR_CAN_ID);
        intake.setNeutralMode(CoralIntakeConstants.CORAL_INTAKE_MOTOR_NEUTRAL_MODE);

        intakeConfig = new TalonFXConfiguration();

        // PID
        Slot0Configs slot0 = intakeConfig.Slot0;
        slot0.kP = CoralIntakeConstants.kP;
        slot0.kI = CoralIntakeConstants.kI;
        slot0.kD = CoralIntakeConstants.kD;

        MotionMagicConfigs motionMagic = intakeConfig.MotionMagic;
        motionMagic.MotionMagicCruiseVelocity = 160;
        motionMagic.MotionMagicAcceleration = 6000;
        motionMagic.MotionMagicJerk = 1600;

        intakeSim = new RollerSim(CoralIntakeConstants.SIM_CONFIG, RobotSim.leftView, intake.getSimState(), "Coral Intake");

        applyConfig();
    }

    private void applyConfig() {
        StatusCode status = intake.getConfigurator().apply(intakeConfig);
        if (!status.isOK()) {
            DriverStation.reportWarning(
                    status.getName() + "Failed to apply configs to coral intake" + status.getDescription(), false);
        }
    }
    @Override
    public void initSendable(NTSendableBuilder builder) {
    }

    @Override
    public void simulationPeriodic() { // man idfkcv
        intakeSim.simulationPeriodic();
    }

    public void setup() {
        CoralIntakeStates.setStates();
    }

    public Command setCoralIntakePosition(double position) {
        return run(() -> {
            final MotionMagicVoltage request = mmVoltage;
            intake.setControl(request.withPosition(position));
        }).withName("Coral Intake Target Position");
    }
}
