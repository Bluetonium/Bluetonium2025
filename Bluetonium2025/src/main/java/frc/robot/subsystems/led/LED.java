package frc.robot.subsystems.led;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.led.LEDConstants.Animations;

public class LED extends SubsystemBase {
    private CANdle candle = new CANdle(LEDConstants.CAN_ID); // lol
    private String currentAnimation = "NONE";

    /**
     * <img src=
     * "https://media.tenor.com/06CbLjZSX20AAAAM/cringe-cat.gif"
     * id="yes" alt="shitter">
     * 
     */
    public LED() {
        CANdleConfiguration config = new CANdleConfiguration();
        config.disableWhenLOS = true;
        config.statusLedOffWhenActive = true;
        config.stripType = LEDStripType.GRB; // set the strip type to RGB
        config.brightnessScalar = 0.5; // dim the LEDs to half brightness
        candle.configAllSettings(config);

        SendableRegistry.add(this, "LED");
        SmartDashboard.putData(this);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("LED");
        builder.addStringProperty("Current Animation", () -> currentAnimation, null);
    }

    public Command setAnimation(Animations animations) {
        return runOnce(() -> {

            for (int i = 0; i < animations.animations.length; i++) {
                candle.animate(animations.animations[i], i);
            }

            for (int i = animations.animations.length; i < candle.getMaxSimultaneousAnimationCount(); i++) {
                candle.clearAnimation(i);
            }

            if (animations.rgb != null) {
                candle.setLEDs(animations.rgb[0], animations.rgb[1], animations.rgb[2]);
            }
            currentAnimation = animations.name();
        }).withName("LED." + animations.name()).ignoringDisable(true);
    }

    public void setup() {
        LEDStates.setupStates();
    }

}
