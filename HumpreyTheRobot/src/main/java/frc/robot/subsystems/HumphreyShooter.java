package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import java.util.Map;
import java.util.TreeMap;

public class HumphreyShooter {
    private static WPI_TalonFX constantWheel = new WPI_TalonFX(6);
    private static WPI_TalonFX changingWheel = new WPI_TalonFX(7);
    private static WPI_VictorSPX intakeWheel = new WPI_VictorSPX(8);
    private static final double gP = 0.1;
    private static final double gF = 0.1;
    private static final double ticksPerRotation = 2048;
    private static final double invPollRate = 0.1;
    private static final double minutesPerSecond = 1.0 / 60.0;
    private static final double RPM = 2250;
    private static final double constantWheelSpeed = RPM * invPollRate * minutesPerSecond * ticksPerRotation;
    private static final double ERROR_THRESHOLD = 5;
    // This we can preset until we find a value we like
    // Based on the situation of the motors and their placement, we might need to
    // invert this? So that the motors actually shoot the ball and dont just spin it
    // in place

    public static final TreeMap<Double, Double> yValuesToSpeeds = new TreeMap<>();

    static {
        changingWheel.configFactoryDefault();
        changingWheel.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        changingWheel.config_kP(0, gP);
        changingWheel.config_kF(0, gF);
        changingWheel.setNeutralMode(NeutralMode.Coast);
        changingWheel.setSensorPhase(true);
        changingWheel.setInverted(true);

        constantWheel.configFactoryDefault();
        constantWheel.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        constantWheel.config_kP(0, gP);
        constantWheel.config_kF(0, gF);
        constantWheel.setNeutralMode(NeutralMode.Coast);
        constantWheel.setSensorPhase(true);

        // input all needed values
        yValuesToSpeeds.put(-2.25, 3100.0); // 150in
        yValuesToSpeeds.put(-0.69, 2750.0); // 138in
        yValuesToSpeeds.put(1.2, 2550.0); // 126in
        yValuesToSpeeds.put(3.14, 2350.0); // 114in
        yValuesToSpeeds.put(5.50, 2325.0); // 102in
        yValuesToSpeeds.put(8.0, 2220.0); // 90in
        yValuesToSpeeds.put(11.4, 2125.0); // 78in
        yValuesToSpeeds.put(15.0, 2000.0); // 66in
    }

    public double getSpeed(double yValue) {
        Map.Entry<Double, Double> smallerEntry = yValuesToSpeeds.floorEntry(yValue);
        if (null == smallerEntry) {
            System.err.println("Goal too close to determine shooter speed");
            return 0;
        }
        Map.Entry<Double, Double> largerEntry = yValuesToSpeeds.ceilingEntry(yValue);
        if (null == largerEntry) {
            System.err.println("Goal too far to determine shooter speed");
            return 0;
        }
        double smallerSpeed = smallerEntry.getValue();
        double largerSpeed = largerEntry.getValue();
        double smallerY = smallerEntry.getKey();
        double largerY = largerEntry.getKey();
        double percentage = (yValue - smallerY) / (largerY - smallerY);
        double speedRange = largerSpeed - smallerSpeed;
        double speed = smallerSpeed + (speedRange * percentage);
        System.out.println("Revving @ " + speed + ", Y-Value = " + yValue);
        return speed;
        
    }

    public boolean isReadyToFeed()
    {
        final boolean constantOK = Math.abs(constantWheel.getErrorDerivative()) < ERROR_THRESHOLD;
        final boolean changingOK = Math.abs(changingWheel.getErrorDerivative()) < ERROR_THRESHOLD;
        return constantOK && changingOK;
        // System.out.println("Constant RPM off by " + constantWheel.getErrorDerivative());
        // System.out.println("Changing RPM off by " + changingWheel.getErrorDerivative());
    }

    public void shoot(double wheelSpeed) {
        constantWheel.set(TalonFXControlMode.Velocity, constantWheelSpeed);
        changingWheel.set(TalonFXControlMode.Velocity,
                wheelSpeed * invPollRate * minutesPerSecond * ticksPerRotation);
        // System.out.println("Real: " + changingWheel.getSelectedSensorVelocity() + "
        // Target: "
        // + wheelSpeed * 6000 * invPollRate * minutesPerSecond * ticksPerRotation);
        // System.out.println("shooting: " + constantWheelSpeed + ", " + wheelSpeed);
    }

    public void stopShooting() {
        constantWheel.set(TalonFXControlMode.Velocity, 0);
        changingWheel.set(TalonFXControlMode.Velocity, 0);
    }

    public void shooterIntake() {
        intakeWheel.set(-1);
        // System.out.println("shooter intake");
    }

    public void stopShooterIntake() {
        intakeWheel.set(0);
    }

    public void load2Intake() {
        intakeWheel.set(-1);
    }

    public void reverseShooterIntake() {
        intakeWheel.set(1);
    }

}
