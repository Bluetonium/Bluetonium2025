package frc.robot.subsystems.led;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;

public class LEDConstants {
    public static int LED_COUNT = 64;
    public static int CAN_ID = 23;

    // animations
    public static Animation DISABLED_ANIMATION = new ColorFlowAnimation(255, 0, 0);
    public static Animation TELEOP_ANIMATION = new LarsonAnimation(0, 0, 255, 100, 1, LED_COUNT, BounceMode.Front, 10);
    public static Animation PANIC_MODE_ANIMATION = new StrobeAnimation(255, 0, 0);
    public static Animation END_GAME_ANIMATION = new LarsonAnimation(0, 255, 0, 100, 1, LED_COUNT, BounceMode.Front,
            10);

}
