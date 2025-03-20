package frc.robot.subsystems.led;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LED extends SubsystemBase {
    private CANdle candle = new CANdle(LEDConstants.CAN_ID); // lol

    /**
     * <img src=
     * "https://media.tenor.com/06CbLjZSX20AAAAM/cringe-cat.gif"
     * id="yes" alt="shitter">
     * 
     */
    public LED() {
        CANdleConfiguration config = new CANdleConfiguration();
        config.stripType = LEDStripType.GRB; // set the strip type to RGB
        config.brightnessScalar = 0.5; // dim the LEDs to half brightness
        candle.configAllSettings(config);

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
    }

    public Command setAnimation(Animation animation) {
        return runOnce(() -> {
            animation.setLedOffset(8);
            candle.animate(animation);
        }).ignoringDisable(true);
    }

    public void setup() {
        LEDStates.setupStates();
    }

}
