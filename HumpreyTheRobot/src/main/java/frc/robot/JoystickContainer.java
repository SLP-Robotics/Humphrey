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
    final JoystickButton aimBotToggle = new JoystickButton(right, 3);

    public double speed, direction;
    public boolean boostEnabled;
    public boolean autoCargoEnabled;
    public boolean aimBotEnabled;
    public boolean intakeInitiated;
    final JoystickButton intakeInitiate = new JoystickButton(right, 2);
    final JoystickButton shootInitiate = new JoystickButton(right, 1);

    public double inputShooterSpeed;
    // The inputShooterSpeed will eventually be deleted and its function replaced by
    // a distance lookup table
    public boolean shootInitiated;

    public double getSpeed() {
        return center.getY();
    };

    public double getDirection() {
        return left.getX();
    };

    public boolean getBoostEnabled() {
        return speedBoost.get();
    };

    public boolean getAutoCargoEnabled() {
        return teleopCargoLock.get();
    };

    public boolean getAimBotEnabled() {
        return aimBotToggle.get();
    };

    public boolean getShootInitiated() {
        return shootInitiate.get();
    };

    public double getInputShooterSpeed() {
        return Math.abs(right.getY());
    };

    public boolean getIntakeInitiated() {
        return intakeInitiate.get();
    };
}
