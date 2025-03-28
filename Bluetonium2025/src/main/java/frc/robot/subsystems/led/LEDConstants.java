package frc.robot.subsystems.led;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;

public class LEDConstants {
        public static int LED_COUNT = 65 + 8;
        public static int CAN_ID = 17;

        public static enum Animations {
                DISABLED(255, 0, 0),
                TELEOP(
                                new LarsonAnimation(0, 0, 255, 100, 0.5, LED_COUNT, BounceMode.Front,
                                                10, 0)),
                END_GAME(new StrobeAnimation(0, 0, 255, 255, .5, LED_COUNT)),
                ESTOPPED(new StrobeAnimation(255, 0, 0, 255, .5, LED_COUNT)),
                AUTON(new LarsonAnimation(255, 0, 0, 0, 0.5, LED_COUNT, BounceMode.Front, 10)),
                DISCONNECTED(new ColorFlowAnimation(255, 0, 0, 0, 0.5, LED_COUNT, Direction.Forward));

                public final Animation[] animations;
                public final int[] rgb;

                private Animations(Animation... animations) {
                        this.animations = animations;
                        this.rgb = null;
                }

                private Animations(int r, int g, int b) {
                        this.animations = new Animation[0];
                        this.rgb = new int[] { r, g, b };
                }
        }

}
