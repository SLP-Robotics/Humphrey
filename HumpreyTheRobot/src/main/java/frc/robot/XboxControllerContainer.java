package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.XboxControllerSim;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.lang.Math;

public class XboxControllerContainer {

    public XboxController xbox = new XboxController(0);

    public double speed, direction;
    public boolean boostValue;
    public boolean autoCargoEnabled;
    public boolean aimBotEnabled;
    public boolean intakeInitiated;

    public double inputShooterSpeed;
    // The inputShooterSpeed will eventually be deleted and its function replaced by
    // a distance lookup table
    public boolean shootInitiated;

    public XboxControllerContainer() {
        readButtons();
    }

    public void readButtons() {
        speed = xbox.getLeftY();
        System.out.println(speed);
        direction = xbox.getLeftX();
        System.out.println(direction);
        boostValue = xbox.getLeftStickButton();
        autoCargoEnabled = xbox.getLeftBumper();
        aimBotEnabled = xbox.getRightBumper();
        shootInitiated = (xbox.getRightTriggerAxis() > 0.1);
        inputShooterSpeed = Math.abs(xbox.getRightX());
        // Absolute value to prevent the wheel from spinning backwards which could
        // cause... idk what could happen, but probably not good
        intakeInitiated = (xbox.getLeftTriggerAxis() > 0.1);
    }
}
