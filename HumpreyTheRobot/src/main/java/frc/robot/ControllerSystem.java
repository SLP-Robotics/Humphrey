package frc.robot;

public interface ControllerSystem {
    public double getSpeed();

    public double getDirection();

    public boolean getBoostEnabled();

    public boolean getAutoCargoEnabled();

    public boolean getAimBotEnabled();

    public boolean getShootInitiated();

    public double getInputShooterSpeed();

    public boolean getIntakeInitiated();

}
