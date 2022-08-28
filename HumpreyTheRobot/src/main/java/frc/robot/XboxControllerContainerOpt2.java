package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;

public class XboxControllerContainerOpt2 implements ControllerSystem {

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

    public XboxControllerContainerOpt2() {
        readButtons();
    }

    public void readButtons() {
        speed = xbox.getLeftY();
        // System.out.println(speed);
        direction = xbox.getLeftX();
        // System.out.println(direction);
        boostEnabled = xbox.getBButtonPressed();
        autoCargoEnabled = xbox.getYButtonPressed();
        aimBotEnabled = xbox.getAButtonPressed();
        shootInitiated = xbox.getXButtonPressed();
        inputShooterSpeed = Math.abs(xbox.getRightTriggerAxis());
        // Absolute value to prevent the wheel from spinning backwards which could
        // cause... idk what could happen, but probably not good
        intakeInitiated = (xbox.getPOV() == 180);

        // Debora was here
    }

    public double getSpeed() {
        return xbox.getLeftY();
    };

    public double getDirection() {
        return xbox.getLeftX();
    };

    public boolean getBoostEnabled() {
        return xbox.getBButtonPressed();
    };

    public boolean getAutoCargoEnabled() {
        return xbox.getYButtonPressed();
    };

    public boolean getAimBotEnabled() {
        return xbox.getAButtonPressed();
    };

    public boolean getShootInitiated() {
        return xbox.getXButtonPressed();
    };

    public double getInputShooterSpeed() {
        return Math.abs(xbox.getRightTriggerAxis());
    };

    public boolean getIntakeInitiated() {
        return (xbox.getPOV() == 180);
    }

    @Override
    public boolean getIntakeReverse() {
        return false;
    }

    @Override
    public boolean getShooterIntakeReverse() {
        return false;
    }

    @Override
    public boolean getManualShooterIntake() {
        return false;
    }

    @Override
    public boolean getDemoModeDisengaged() {
        // TODO Auto-generated method stub
        return false;
    };
}
