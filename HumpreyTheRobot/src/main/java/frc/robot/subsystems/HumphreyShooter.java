package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import java.util.TreeMap;

public class HumphreyShooter {
    static WPI_TalonFX constantWheel = new WPI_TalonFX(6);
    static WPI_TalonFX changingWheel = new WPI_TalonFX(7);
    static WPI_VictorSPX intakeWheel = new WPI_VictorSPX(8);
    private static final double ticksPerRotation = 2048;
    private static final double invPollRate = 0.1;
    private static final double minutesPerSecond = 1.0 / 60.0;
    private static final double RPM = 3000;
    public static final double constantWheelSpeed = RPM * invPollRate * minutesPerSecond * ticksPerRotation;
    // This we can preset until we find a value we like
    // Based on the situation of the motors and their placement, we might need to
    // invert this? So that the motors actually shoot the ball and dont just spin it
    // in place

    public static TreeMap<Double, Double> yValuesToSpeeds = new TreeMap<>();

    static {
        changingWheel.setInverted(true);
        // input all needed values
        yValuesToSpeeds.put(-20.0, 3.0);
    }

    public double getSpeed(double yValue) {
        double smallerY = yValuesToSpeeds.floorEntry(yValue).getValue();
        double largerY = yValuesToSpeeds.ceilingEntry(yValue).getValue();
        double percentage = (yValue - smallerY) / (largerY - smallerY);
        double speed = Math.abs(yValuesToSpeeds.get(smallerY)
                + percentage * (yValuesToSpeeds.get(largerY) - yValuesToSpeeds.get(smallerY)));
        return speed;
    }

    public static void shoot(double wheelSpeed) {
        constantWheel.set(TalonFXControlMode.Velocity, constantWheelSpeed);
        changingWheel.set(TalonFXControlMode.Velocity,
                wheelSpeed * 6000 * invPollRate * minutesPerSecond * ticksPerRotation);
        System.out.println("Real: " + changingWheel.getSelectedSensorVelocity() + " Target: "
                + wheelSpeed * 6000 * invPollRate * minutesPerSecond * ticksPerRotation);
        // System.out.println("shooting: " + constantWheelSpeed + ", " + wheelSpeed);
    }

    public static void stopShooting() {
        constantWheel.set(TalonFXControlMode.Velocity, 0);
        changingWheel.set(TalonFXControlMode.Velocity, 0);
    }

    public static void shooterIntake() {
        intakeWheel.set(-1);
        // System.out.println("shooter intake");
    }

    public static void stopShooterIntake() {
        intakeWheel.set(0);
    }
}
