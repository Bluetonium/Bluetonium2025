package frc.robot.subsystems.mechanisms.elevator;

import com.ctre.phoenix6.signals.NeutralModeValue;

//maybe we'll make this inside a different class... oh well works for now

public final class ElevatorConstants {
    public static final int ARM_MOTOR_CAN_ID = 20; // find out once more :))))))
    public static final NeutralModeValue ARM_MOTOR_NEUTRAL_MODE = NeutralModeValue.Brake; // find out once more :))))))

    public static final int LIMIT_SWITCH_HIGH_ID = 0;
    public static final int LIMIT_SWITCH_LOW_ID = 0;

    public static final double LOW_POSITION = 0.0;
    public static final double HIGH_POSITION = 10.0; // find out what fucking value to do

    public static final double RANGE_OF_ROTATIONS = 87.2727;

}