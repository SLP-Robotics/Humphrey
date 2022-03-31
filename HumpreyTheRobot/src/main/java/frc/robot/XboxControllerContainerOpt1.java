package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.lang.Math;

public class XboxControllerContainerOpt1 implements ControllerSystem {

    public XboxController xbox = new XboxController(0);

    public double speed, direction;
    public boolean boostEnabled;
    public boolean autoCargoEnabled;
    public boolean aimBotEnabled;
    public boolean intakeInitiated;

    public double inputShooterSpeed;
    // The inputShooterSpeed will eventually be deleted and its function replaced by
    // a distance lookup table
    public boolean shootInitiated;

    public XboxControllerContainerOpt1() {
        readButtons();
    }

    public void readButtons() {
        speed = xbox.getLeftY();
        // System.out.println(speed);
        direction = xbox.getLeftX();
        // System.out.println(direction);
        boostEnabled = xbox.getLeftStickButton();
        autoCargoEnabled = xbox.getLeftBumper();
        aimBotEnabled = xbox.getRightBumper();
        shootInitiated = (xbox.getRightTriggerAxis() > 0.1);
        inputShooterSpeed = Math.abs(xbox.getRightX());
        // Absolute value to prevent the wheel from spinning backwards which could
        // cause... idk what could happen, but probably not good
        intakeInitiated = (xbox.getLeftTriggerAxis() > 0.1);

        // Debora was here
    }

    public double getSpeed() {
        return xbox.getLeftY();
    };

    public double getDirection() {
        return xbox.getLeftX();
    };

    public boolean getBoostEnabled() {
        return xbox.getLeftStickButton();
    };

    public boolean getAutoCargoEnabled() {
        return xbox.getLeftBumper();
    };

    public boolean getAimBotEnabled() {
        return xbox.getRightBumper();
    };

    public boolean getShootInitiated() {
        return (xbox.getRightTriggerAxis() > 0.1);
    };

    public double getInputShooterSpeed() {
        return Math.abs(xbox.getRightX());
    };

    public boolean getIntakeInitiated() {
        return (xbox.getLeftTriggerAxis() > 0.1);
    };

}
