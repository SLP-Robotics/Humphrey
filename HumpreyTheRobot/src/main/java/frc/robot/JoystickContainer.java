package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.lang.Math;

public class JoystickContainer implements ControllerSystem {

    public Joystick left = new Joystick(0);
    public Joystick center = new Joystick(1);
    public Joystick right = new Joystick(2);
    final JoystickButton speedBoost = new JoystickButton(center, 3);
    final JoystickButton teleopCargoLock = new JoystickButton(left, 3);
    final JoystickButton aimBotToggle = new JoystickButton(center, 2);

    public double speed, direction;
    public boolean boostEnabled;
    public boolean autoCargoEnabled;
    public boolean aimBotEnabled;
    public boolean intakeInitiated;
    final JoystickButton intakeInitiate = new JoystickButton(center, 1);
    final JoystickButton shootInitiate = new JoystickButton(right, 1);
    final JoystickButton intakeReverse = new JoystickButton(right, 3);
    final JoystickButton shooterIntakeReverse = new JoystickButton(right, 5);
    final JoystickButton manualShooterIntake = new JoystickButton(right, 4);

    public double inputShooterSpeed;
    // The inputShooterSpeed will eventually be deleted and its function replaced by
    // a distance lookup table
    public boolean shootInitiated;

    @Override
    public double getSpeed() {
        return center.getY();
    };

    @Override
    public double getDirection() {
        return left.getX();
    };

    @Override
    public boolean getBoostEnabled() {
        return speedBoost.get();
    };

    @Override
    public boolean getAutoCargoEnabled() {
        return teleopCargoLock.get();
    };

    @Override
    public boolean getAimBotEnabled() {
        return aimBotToggle.get();
    };

    @Override
    public boolean getShootInitiated() {
        return shootInitiate.get();
    };

    @Override
    public double getInputShooterSpeed() {
        return Math.abs(right.getY());
    };

    @Override
    public boolean getIntakeInitiated() {
        return intakeInitiate.get();
    };

    @Override
    public boolean getIntakeReverse() {
        return intakeReverse.get();
    }

    @Override
    public boolean getShooterIntakeReverse() {
        return shooterIntakeReverse.get();
    }

    @Override
    public boolean getManualShooterIntake() {
        return manualShooterIntake.get();
    }
}
