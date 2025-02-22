package frc.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;

public class Field {
    public static final double fieldLength = Units.inchesToMeters(690.876);
    public static final double fieldWidth = Units.inchesToMeters(317);

    /**
     * how far the reef branches are off from the center of a side of a reef
     */
    public static final double REEF_BRANCH_OFFSET = Units.inchesToMeters(12.94 / 2);

    private Field() {
    }

    /**
     * Center of the reef
     */
    public static final Translation2d REEF_CENTER = new Translation2d(Units.inchesToMeters(176.746),
            Units.inchesToMeters(158.501));

    // ordered counter-clockwise starting on the right
    public enum REEF_REGIONS {
        GH,
        IJ,
        KL,
        AB,
        CD,
        EF
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

    public static Pose2d reefRegionToPose(REEF_REGIONS region, boolean left) {
        Pose2d position;
        switch (region) {
            case AB:
                position = new Pose2d(2.5, 4, Rotation2d.fromDegrees(0));
                break;
            case CD:
                position = new Pose2d(3.5, 2.3, Rotation2d.fromDegrees(60));
                break;
            case EF:
                position = new Pose2d(5.5, 2.3, Rotation2d.fromDegrees(120));
                break;
            case GH:
                position = new Pose2d(6.5, 4, Rotation2d.fromDegrees(180));
                break;
            case IJ:
                position = new Pose2d(5.5, 5.7, Rotation2d.fromDegrees(240));
                break;
            case KL:
                position = new Pose2d(3.5, 5.7, Rotation2d.fromDegrees(300));
                break;
            default:
                position = new Pose2d(2.5, 4, Rotation2d.fromDegrees(0));
                break;
        }

        double angle = position.getRotation().getRadians() + Math.PI / 2;

        int flip = (left) ? -1 : 1;
        double xOffset = Math.cos(angle) * REEF_BRANCH_OFFSET * flip;
        double YOffset = Math.sin(angle) * REEF_BRANCH_OFFSET * flip;

        return new Pose2d(position.getX() - xOffset, position.getY() - YOffset, position.getRotation());
    }

    public static boolean isBlue() {
        return DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue)
                .equals(DriverStation.Alliance.Blue);
    }

    public static Rotation2d flipIfRed(Rotation2d rotation) {
        if (!isBlue()) {
            return rotation.plus(Rotation2d.fromDegrees(180));
        }
        return rotation;
    }

    public static Pose2d flipIfRed(Pose2d position) {
        Translation2d translation = flipIfRed(position.getTranslation());
        if (!isBlue()) {
            return new Pose2d(translation, position.getRotation().plus(Rotation2d.fromDegrees(180)));
        }
        return new Pose2d(translation, position.getRotation());
    }

    public static Translation2d flipIfRed(Translation2d position) {
        if (!isBlue()) {
            return new Translation2d(fieldLength - position.getX(), fieldWidth - position.getY());
        }
        return position;
    }

    public static double getAngleToReef(Translation2d robotPosition) {
        // robotPosition = flipIfRed(robotPosition);
        Translation2d reefRelativePosition = robotPosition.minus(flipIfRed(REEF_CENTER));
        return Math.atan2(reefRelativePosition.getY(), reefRelativePosition.getX());
    }

    public static REEF_REGIONS getReefRegion(Translation2d robotPosition) {
        double angleToReef = getAngleToReef(robotPosition);
        angleToReef += Math.PI / 6;
        angleToReef = (angleToReef < 0) ? angleToReef + Math.PI * 2 : angleToReef;
        if (!isBlue()) {
            angleToReef += Math.PI;
            angleToReef %= Math.PI * 2;
        }

        int region = (int) Math.floor(angleToReef / ((Math.PI * 2) / 6));
        return REEF_REGIONS.values()[region];
    }
}