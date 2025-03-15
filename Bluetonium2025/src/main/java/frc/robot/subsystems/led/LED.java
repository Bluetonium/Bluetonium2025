package frc.robot.subsystems.led;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.StrobeAnimation;

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
        config.stripType = LEDStripType.RGB; // set the strip type to RGB
        config.brightnessScalar = 0.5; // dim the LEDs to half brightness
        candle.configAllSettings(config);
        RainbowAnimation rainbow = new RainbowAnimation(1, 1, LEDConstants.LED_COUNT);
        candle.animate(rainbow);
    }

    public Command setAnimation(Animation animation) {
        return runOnce(() -> candle.animate(animation));
    }

    public void setup() {
        LEDStates.setupStates();
    }

    public Command panicMode() {
        return runOnce(() -> {
            candle.animate(new StrobeAnimation(255, 0, 0));
        });
    }
}
