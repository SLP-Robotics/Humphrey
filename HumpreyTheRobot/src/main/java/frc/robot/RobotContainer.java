package frc.robot;

public class RobotContainer {

    public ControllerSystem controller = new JoystickContainer();

    public double speed, direction;
    public boolean boostEnabled;
    public boolean autoCargoEnabled;
    public boolean aimBotEnabled;
    public boolean intakeInitiated;
    public boolean demoModeDisengaged;

    public double inputShooterSpeed;
    // The inputShooterSpeed will eventually be deleted and its function replaced by
    // a distance lookup table
    public boolean shootInitiated;

    public boolean manualShooterIntake;
    public boolean intakeReverse;
    public boolean shooterIntakeReverse;

    public RobotContainer() {
        readButtons();
    }

    public double deadzone(double input) {
        if (Math.abs(input) < 0.1) {
            return 0;
        } else {
            return input;
        }
    }

    public void readButtons() {
        speed = deadzone(controller.getSpeed());
        // System.out.println(speed);
        direction = deadzone(controller.getDirection());
        // System.out.println(direction);
        boostEnabled = controller.getBoostEnabled();
        autoCargoEnabled = controller.getAutoCargoEnabled();
        aimBotEnabled = controller.getAimBotEnabled();
        shootInitiated = controller.getShootInitiated();
        inputShooterSpeed = deadzone(controller.getInputShooterSpeed());
        demoModeDisengaged = controller.getDemoModeDisengaged();
        // Absolute value to prevent the wheel from spinning backwards which could
        // cause... idk what could happen, but probably not good
        intakeInitiated = controller.getIntakeInitiated();
        if (intakeInitiated) {
            System.out.println("another testing print statement");
         };
        intakeReverse = controller.getIntakeReverse();
        shooterIntakeReverse = controller.getShooterIntakeReverse();
        manualShooterIntake = controller.getManualShooterIntake();
        // Debora was here
    }
}
