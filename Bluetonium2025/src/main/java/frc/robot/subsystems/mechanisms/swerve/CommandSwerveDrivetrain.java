package frc.robot.subsystems.mechanisms.swerve;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.RotationTarget;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotContainer;
import frc.robot.subsystems.driver.Drivers;
import frc.robot.subsystems.limelight.LimelightConfig;
import frc.robot.subsystems.limelight.Limelights;
import frc.robot.subsystems.limelight.LimelightConstants.Pipelines;
import frc.robot.subsystems.mechanisms.swerve.TunerConstants.TunerSwerveDrivetrain;
import frc.utils.Field;
import frc.utils.Field.REEF_REGIONS;

/**
 * Class that extends the Phoenix 6 SwerveDrivetrain class and implements
 * Subsystem so it can easily be used in command-based projects.
 */
public class CommandSwerveDrivetrain extends TunerSwerveDrivetrain implements Subsystem {
    // Speeds
    private final double MAX_SPEED = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top
                                                                                         // speed
    private final double MAX_ANGULAR_SPEED = RotationsPerSecond.of(2).in(RadiansPerSecond); // 3/4 of a rotation per
                                                                                            // second max
    // angular velocity

    private static final double kSimLoopPeriod = 0.005; // 5 ms
    private Notifier m_simNotifier = null;
    private double m_lastSimTime;

    /* Blue alliance sees forward as 0 degrees (toward red alliance wall) */
    private static final Rotation2d kBlueAlliancePerspectiveRotation = Rotation2d.kZero;
    /* Red alliance sees forward as 180 degrees (toward blue alliance wall) */
    private static final Rotation2d kRedAlliancePerspectiveRotation = Rotation2d.k180deg;
    /* Keep track if we've ever applied the operator perspective before or not */
    private boolean m_hasAppliedOperatorPerspective = false;

    /* Swerve requests to apply during SysId characterization */
    private final SwerveRequest.SysIdSwerveTranslation m_translationCharacterization = new SwerveRequest.SysIdSwerveTranslation();
    private final SwerveRequest.SysIdSwerveSteerGains m_steerCharacterization = new SwerveRequest.SysIdSwerveSteerGains();
    private final SwerveRequest.SysIdSwerveRotation m_rotationCharacterization = new SwerveRequest.SysIdSwerveRotation();

    // Requests
    private final SwerveRequest.ApplyRobotSpeeds driveRealtive = new SwerveRequest.ApplyRobotSpeeds(); // pathplanner
                                                                                                       // and dpad
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MAX_SPEED * 0.1).withRotationalDeadband(MAX_ANGULAR_SPEED * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors

    // private final Map<REEF_REGIONS,Command> pathfinding

    /*
     * SysId routine for characterizing translation. This is used to find PID gains
     * for the drive motors.
     */
    @SuppressWarnings("unused")
    private final SysIdRoutine m_sysIdRoutineTranslation = new SysIdRoutine(
            new SysIdRoutine.Config(
                    null, // Use default ramp rate (1 V/s)
                    Volts.of(4), // Reduce dynamic step voltage to 4 V to prevent brownout
                    null, // Use default timeout (10 s)
                    // Log state with SignalLogger class
                    state -> SignalLogger.writeString("SysIdTranslation_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    output -> setControl(m_translationCharacterization.withVolts(output)),
                    null,
                    this));

    /*
     * SysId routine for characterizing steer. This is used to find PID gains for
     * the steer motors.
     */
    private final SysIdRoutine m_sysIdRoutineSteer = new SysIdRoutine(
            new SysIdRoutine.Config(
                    null, // Use default ramp rate (1 V/s)
                    Volts.of(7), // Use dynamic voltage of 7 V
                    null, // Use default timeout (10 s)
                    // Log state with SignalLogger class
                    state -> SignalLogger.writeString("SysIdSteer_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    volts -> setControl(m_steerCharacterization.withVolts(volts)),
                    null,
                    this));

    /*
     * SysId routine for characterizing rotation.
     * This is used to find PID gains for the FieldCentricFacingAngle
     * HeadingController.
     * See the documentation of SwerveRequest.SysIdSwerveRotation for info on
     * importing the log to SysId.
     */
    @SuppressWarnings("unused")
    private final SysIdRoutine m_sysIdRoutineRotation = new SysIdRoutine(
            new SysIdRoutine.Config(
                    /* This is in radians per second², but SysId only supports "volts per second" */
                    Volts.of(Math.PI / 6).per(Second),
                    /* This is in radians per second, but SysId only supports "volts" */
                    Volts.of(Math.PI),
                    null, // Use default timeout (10 s)
                    // Log state with SignalLogger class
                    state -> SignalLogger.writeString("SysIdRotation_State", state.toString())),
            new SysIdRoutine.Mechanism(
                    output -> {
                        /* output is actually radians per second, but SysId only supports "volts" */
                        setControl(m_rotationCharacterization.withRotationalRate(output.in(Volts)));
                        /* also log the requested output for SysId */
                        SignalLogger.writeDouble("Rotational_Rate", output.in(Volts));
                    },
                    null,
                    this));

    /* The SysId routine to test */
    private SysIdRoutine m_sysIdRoutineToApply = m_sysIdRoutineSteer;

    /**
     * Constructs a CTRE SwerveDrivetrain using the specified constants.
     * <p>
     * This constructs the underlying hardware devices, so users should not
     * construct
     * the devices themselves. If they need the devices, they can access them
     * through
     * getters in the classes.
     *
     * @param drivetrainConstants Drivetrain-wide constants for the swerve drive
     * @param modules             Constants for each specific module
     */

    public PPHolonomicDriveController PPdriveController;

    public CommandSwerveDrivetrain(
            SwerveDrivetrainConstants drivetrainConstants,
            SwerveModuleConstants<?, ?, ?>... modules) {
        super(drivetrainConstants, modules);
        if (Utils.isSimulation()) {
            startSimThread();
        }

        configurePathPlanner();

    }

    public void setup() {
        SwerveStates.setStates();

        setDefaultCommand(
                // Drivetrain will execute this command periodically
                applyRequest(
                        () -> drive.withVelocityX(-Drivers.chassisControlTranslation.getAsDouble() * MAX_SPEED) // Drive
                                .withVelocityY(-Drivers.chassisControlStrafe.getAsDouble() * MAX_SPEED) // Drive left
                                                                                                        // with
                                                                                                        // // (left)
                                .withRotationalRate(
                                        -Drivers.chassisControlRotation.getAsDouble() * MAX_ANGULAR_SPEED))
                        .withName("Swerve.Teleop-Drive"));

    }

    public Command dpadRelative(DoubleSupplier POV) {

        // couldnt figure it out with run() or whatever so have this abomination against
        // nature itself instead
        return applyRequest(
                () -> driveRealtive.withSpeeds(new ChassisSpeeds(-Math.cos(Math.toRadians(POV.getAsDouble())) * 5,
                        Math.sin(Math.toRadians(POV.getAsDouble())) * 5, 0)));
    }

    public Command slowSwerve() {
        return applyRequest(
                () -> drive
                        .withVelocityX(
                                -Drivers.chassisControlTranslation.getAsDouble() * MAX_SPEED
                                        * SwerveConstants.SLOW_SPEED_MODIFIER) // Drive
                        .withVelocityY(-Drivers.chassisControlStrafe.getAsDouble() * MAX_SPEED
                                * SwerveConstants.SLOW_SPEED_MODIFIER) // Drive
                        // left
                        // with
                        // // (left)
                        .withRotationalRate(
                                -Drivers.chassisControlRotation.getAsDouble() * MAX_ANGULAR_SPEED))
                .withName("Swerve.Teleop-Drive");

    }

    /*
     * 
     * if (controller.getPOV() != -1){
     * double rads = Math.toRadians(controller.getPOV());
     * povTranslation = () -> Math.cos(rads);
     * povStrafe = () -> Math.sin(rads);
     * }
     */

    private void configurePathPlanner() {
        RobotConfig config;
        try {
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            e.printStackTrace();
            config = new RobotConfig(58, 7.1, new ModuleConfig(0.0508, 4, 1.02, DCMotor.getKrakenX60(1), 80, 1),
                    new Translation2d[] {});
        }
        PPdriveController = new PPHolonomicDriveController(
                new PIDConstants(5.0, 0.0, 0.0),
                new PIDConstants(8, 0.0, 0.2));
        AutoBuilder.configure(
                () -> getState().Pose,
                this::resetPose,
                () -> getState().Speeds,
                (speeds, feedforwards) -> setControl(
                        driveRealtive.withSpeeds(speeds)
                                .withWheelForceFeedforwardsX(feedforwards.robotRelativeForcesXNewtons())
                                .withWheelForceFeedforwardsY(feedforwards.robotRelativeForcesYNewtons())),
                PPdriveController,
                config,
                () -> {
                    Optional<Alliance> alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;

                },
                this

        );

    }

    /**
     * Constructs a CTRE SwerveDrivetrain using the specified constants.
     * <p>
     * This constructs the underlying hardware devices, so users should not
     * construct
     * the devices themselves. If they need the devices, they can access them
     * through
     * getters in the classes.
     *
     * @param drivetrainConstants     Drivetrain-wide constants for the swerve drive
     * @param odometryUpdateFrequency The frequency to run the odometry loop. If
     *                                unspecified or set to 0 Hz, this is 250 Hz on
     *                                CAN FD, and 100 Hz on CAN 2.0.
     * @param modules                 Constants for each specific module
     */
    public CommandSwerveDrivetrain(
            SwerveDrivetrainConstants drivetrainConstants,
            double odometryUpdateFrequency,
            SwerveModuleConstants<?, ?, ?>... modules) {
        super(drivetrainConstants, odometryUpdateFrequency, modules);
        if (Utils.isSimulation()) {
            startSimThread();
        }
    }

    /**
     * Constructs a CTRE SwerveDrivetrain using the specified constants.
     * <p>
     * This constructs the underlying hardware devices, so users should not
     * construct
     * the devices themselves. If they need the devices, they can access them
     * through
     * getters in the classes.
     *
     * @param drivetrainConstants       Drivetrain-wide constants for the swerve
     *                                  drive
     * @param odometryUpdateFrequency   The frequency to run the odometry loop. If
     *                                  unspecified or set to 0 Hz, this is 250 Hz
     *                                  on
     *                                  CAN FD, and 100 Hz on CAN 2.0.
     * @param odometryStandardDeviation The standard deviation for odometry
     *                                  calculation
     *                                  in the form [x, y, theta]ᵀ, with units in
     *                                  meters
     *                                  and radians
     * @param visionStandardDeviation   The standard deviation for vision
     *                                  calculation
     *                                  in the form [x, y, theta]ᵀ, with units in
     *                                  meters
     *                                  and radians
     * @param modules                   Constants for each specific module
     */
    public CommandSwerveDrivetrain(
            SwerveDrivetrainConstants drivetrainConstants,
            double odometryUpdateFrequency,
            Matrix<N3, N1> odometryStandardDeviation,
            Matrix<N3, N1> visionStandardDeviation,
            SwerveModuleConstants<?, ?, ?>... modules) {
        super(drivetrainConstants, odometryUpdateFrequency, odometryStandardDeviation, visionStandardDeviation,
                modules);
        if (Utils.isSimulation()) {
            startSimThread();
        }
    }

    /**
     * Returns a command that applies the specified control request to this swerve
     * drivetrain.
     *
     * @param request Function returning the request to apply
     * @return Command to run
     */
    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> this.setControl(requestSupplier.get()));
    }

    /**
     * Runs the SysId Quasistatic test in the given direction for the routine
     * specified by {@link #m_sysIdRoutineToApply}.
     *
     * @param direction Direction of the SysId Quasistatic test
     * @return Command to run
     */
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutineToApply.quasistatic(direction);
    }

    /**
     * Runs the SysId Dynamic test in the given direction for the routine
     * specified by {@link #m_sysIdRoutineToApply}.
     *
     * @param direction Direction of the SysId Dynamic test
     * @return Command to run
     */
    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return m_sysIdRoutineToApply.dynamic(direction);
    }

    public Command AlignToReefRegion(boolean leftBranch) {
        Set<Subsystem> requirements = new HashSet<Subsystem>();
        requirements.add(this);
        if (leftBranch)
            return Commands.defer(this::getPathToReefLeft, requirements);
        return Commands.defer(this::getPathToReefRight, requirements);
    }

    private Command getPathToReefLeft() {
        return getPathToReef(true);
    }

    private Command getPathToReefRight() {
        return getPathToReef(false);
    }

    /**
     * Drives to a target position using PathPlanner
     * 
     * @param targetPos target position to drive to
     * @param name      name
     */
    private Command createPath(Pose2d targetPos, String name) {
        SwerveDriveState state = getState();

        // finding a rotation because for waypoints rotation is heading (direction we
        // wanna be heading) so we us the angle to target so we head towards target at
        // first
        Rotation2d angleToTarget = Rotation2d
                .fromRadians(Field.getAngleTo(state.Pose.getTranslation(), targetPos.getTranslation()))
                .plus(Rotation2d.k180deg); // (probably) the problem child; .plus calls rotateBy which can return 0,0
                                           // and thus crashes

        Pose2d startingPos = new Pose2d(state.Pose.getTranslation(), angleToTarget);
        List<Waypoint> pathPoints = PathPlannerPath
                .waypointsFromPoses(
                        startingPos,
                        targetPos);

        List<RotationTarget> rotationTargets = Arrays.asList(
                new RotationTarget(0.5, targetPos.getRotation()));
        PathPlannerPath path = new PathPlannerPath(
                pathPoints,
                rotationTargets,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                SwerveConstants.autoAlignmentConstraints,
                null,
                new GoalEndState(0, targetPos.getRotation()),
                false);

        path.name = name;
        path.preventFlipping = true;
        // RotationTarget e = new RotationTarget();

        return AutoBuilder.followPath(path).withName(path.name);
    }

    public Command AlignToCoralStation() {
        Set<Subsystem> requirements = new HashSet<Subsystem>();
        requirements.add(this);
        return Commands.defer(this::getPathToCoralStation, requirements);
    }

    // Drive to coral station
    private Command getPathToCoralStation() {
        final double angle = Math.toRadians(54);

        SwerveDriveState state = getState();
        Pose2d fieldPos = state.Pose;

        double fieldDividerLine = Field.fieldWidth / 2;

        double distance = 1.25; // 1.25 from the x direction, and 1.25 from the y. Not diagonal. Manhattan
                                // Distance.

        double goalX = 0;
        double goalY = 0;
        double rotation = 0;

        if (fieldPos.getY() < fieldDividerLine) {
            goalX = distance;
            goalY = distance;
            rotation = Math.PI + angle;
        } else {
            goalX = distance;
            goalY = Field.fieldWidth - distance;
            rotation = Math.PI - angle;
        }

        Rotation2d goalRotation = new Rotation2d(rotation);

        Pose2d goalPos = new Pose2d(goalX, goalY, goalRotation);

        goalPos = Field.mirrorIfRed(goalPos);
        return createPath(goalPos, "Aligning to Coral Station");
    }

    // Drive to reef
    private Command getPathToReef(boolean left) {
        REEF_REGIONS region = getCurrentRegion();
        try {
            Pose2d targetPos = Field.rotateIfRed(Field.reefRegionToPose(region, left));
            return createPath(targetPos, "Aligning Reef Side : " + region.name());
        } catch (Exception e) {
            System.out.println("Error in getPathToReef");
        }
        return null;
    }

    public REEF_REGIONS getCurrentRegion() {
        return Field.getReefRegion(getState().Pose.getTranslation());
    }

    public Command AprilTagAlign(LimelightConfig usedLimelight, Pipelines pipeline) {
        Limelights vision = RobotContainer.getVision();
        return new FunctionalCommand(() -> {
            vision.setPipeline(usedLimelight, pipeline);
        },
                () -> {
                    final double calculatedSpeed = MathUtil.clamp(
                            SwerveConstants.alignmentKp * vision.getTx(usedLimelight),
                            -SwerveConstants.alignmentMaxSpeed,
                            SwerveConstants.alignmentMaxSpeed);

                    final double strafeSpeed = Math.copySign(
                            Math.max(SwerveConstants.alignmentMinSpeed, // minor spelling mistake, i win
                                    Math.abs(calculatedSpeed)),
                            calculatedSpeed);

                    driveRealtive
                            .withSpeeds(new ChassisSpeeds(0,
                                    strafeSpeed,
                                    0));
                    this.setControl(driveRealtive);

                },
                (interupted) -> {
                    this.setControl(driveRealtive.withSpeeds(new ChassisSpeeds(0, 0, 0)));
                },
                () -> {

                    return Math.abs(vision.getTx(usedLimelight)) < 4;
                }, this, vision).withName("aprilTagAlign");

    }

    @Override
    public void periodic() {
        /*
         * Periodically try to apply the operator perspective.
         * If we haven't applied the operator perspective before, then we should apply
         * it regardless of DS state.
         * This allows us to correct the perspective in case the robot code restarts
         * mid-match.
         * Otherwise, only check and apply the operator perspective if the DS is
         * disabled.
         * This ensures driving behavior doesn't change until an explicit disable event
         * occurs during testing.
         */
        if (!m_hasAppliedOperatorPerspective || DriverStation.isDisabled()) {
            DriverStation.getAlliance().ifPresent(allianceColor -> {
                setOperatorPerspectiveForward(
                        allianceColor == Alliance.Red
                                ? kRedAlliancePerspectiveRotation
                                : kBlueAlliancePerspectiveRotation);
                m_hasAppliedOperatorPerspective = true;
            });
        }
    }

    private void startSimThread() {
        m_lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = new Notifier(() -> {
            final double currentTime = Utils.getCurrentTimeSeconds();
            double deltaTime = currentTime - m_lastSimTime;
            m_lastSimTime = currentTime;
            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage());
        });
        m_simNotifier.startPeriodic(kSimLoopPeriod);
    }

}
