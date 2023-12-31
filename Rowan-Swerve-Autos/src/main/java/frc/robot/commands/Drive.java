package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.DrivetrainSubsystem;

public class Drive extends CommandBase {
    private final DrivetrainSubsystem drivetrain;
    private final BooleanSupplier robotCentricMode;
    private final DoubleSupplier translationXSupplier;
    private final DoubleSupplier translationYSupplier;
    private final DoubleSupplier rotationSupplier;
    
    public Drive(DrivetrainSubsystem drivetrainSubsystem,
    BooleanSupplier robotCentricMode,
    DoubleSupplier translationXSupplier,
    DoubleSupplier translationYSupplier,
    DoubleSupplier rotationSupplier) {
        this.drivetrain = drivetrainSubsystem;
        this.robotCentricMode = robotCentricMode;
        this.translationXSupplier = translationXSupplier;
        this.translationYSupplier = translationYSupplier;
        this.rotationSupplier = rotationSupplier;
        addRequirements(drivetrainSubsystem);
    }
    @Override
    public void execute() {
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of field-oriented movement
        if (robotCentricMode.getAsBoolean()) {
            drivetrain.drive(new ChassisSpeeds(
                translationXSupplier.getAsDouble() * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
                translationYSupplier.getAsDouble() * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
                rotationSupplier.getAsDouble() * DriveConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
            ));
        } else {
            drivetrain.drive(
                ChassisSpeeds.fromFieldRelativeSpeeds(
                    translationXSupplier.getAsDouble() * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
                    translationYSupplier.getAsDouble() * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
                    rotationSupplier.getAsDouble() * DriveConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                    drivetrain.getGyroscopeRotation()
                )
            );
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}