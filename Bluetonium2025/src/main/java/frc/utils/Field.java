package frc.utils;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class Field {
    private Field() {
    }

    /**
     * Center of the reef
     */
    public static final Translation2d REEF_CENTER = new Translation2d(Units.inchesToMeters(176.746),
            Units.inchesToMeters(158.501));

    // ordered counter-clockwise starting on the right
    public enum REEF_REGIONS {
        AB,
        CD,
        EF,
        GH,
        IJ,
        KL
    }

    /***
     * 
     * @param tagId
     * @return the height of the specified april tag in inches
     */
    public static double getTagHeight(int tagId) {
        switch (tagId) {
            case 1, 2, 12, 13:// Coral stations
                return 12 * 4 + 5 + (1.0 / 4) + 4.0625; // 57.3125
            case 3, 16:// Processor
                return 12 * 3 + 11 + (7.0 / 8) + 4.0625; // 51.9375
            case 4, 5, 14, 15:// barge
                return 12 * 5 + 11.0 + 4.0625; // 75.0625
            case 6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22:// reef
                return 8 + (7.0 / 8) + 4.0625; // 12.9375
            default:
                return 0;

        }
    }

    /***
     * Finds the desired angle for the robot to score on the reef
     * 
     * @param tagId
     * @return the angle in radians
     */
    public static double getDesiredAngleReef(int tagId) {
        switch (tagId) {
            case 7, 18:
                return 0.0;
            case 6, 17:
                return 1.04719755;
            case 11, 22:
                return 1.04719755 * 2;
            case 10, 21:
                return 1.04719755 * 3;
            case 9, 20:
                return 1.04719755 * 4;
            case 8, 19:
                return 1.04719755 * 5;
            default:
                return 0;
        }
    }

    public static REEF_REGIONS getReefRegion(Translation2d robotPosition) {
        Translation2d reefRelativePosition = robotPosition.minus(REEF_CENTER);
        double angleToReef = Math.atan2(reefRelativePosition.getY(), reefRelativePosition.getX()) + Math.PI / 12;
        System.out.println("Angle " + Math.toDegrees(angleToReef));
        angleToReef = (angleToReef < 0) ? angleToReef + Math.PI * 2 : angleToReef;
        int region = (int) Math.floor(angleToReef / ((Math.PI * 2) / 6));
        return REEF_REGIONS.values()[region];
    }

}