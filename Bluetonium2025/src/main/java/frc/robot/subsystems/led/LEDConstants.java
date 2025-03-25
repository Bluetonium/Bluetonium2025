package frc.robot.subsystems.led;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;

public class LEDConstants {
        public static int LED_COUNT = 65 + 8;
        public static int CAN_ID = 23;

        public static enum Animations {
                DISABLED(new ColorFlowAnimation(255, 0, 0, 0, 0.5, LED_COUNT, Direction.Forward)),
                TELEOP(new LarsonAnimation(0, 0, 255, 100, 0.5, LED_COUNT, BounceMode.Front,
                                10)),
                END_GAME(new LarsonAnimation(0, 255, 0, 100, 1, LED_COUNT, BounceMode.Front,
                                10)),
                ESTOPPED(new StrobeAnimation(255, 0, 0, 0, 0.1, LED_COUNT)),
                AUTON(new LarsonAnimation(255, 0, 0, 0, 0.5, LED_COUNT, BounceMode.Front, 10));

                public final Animation[] animations;

                private Animations(Animation... animations) {
                        this.animations = animations;

                }
        }

}
