package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import java.util.HashMap;

public class HumphreyShooter {
    static WPI_TalonFX constantWheel = new WPI_TalonFX(5);
    static WPI_TalonFX changingWheel = new WPI_TalonFX(6);
    static WPI_VictorSPX intakeWheel = new WPI_VictorSPX(7);
    public static final double constantWheelSpeed = 0.5;// This we can preset until we find a value we like
    // Based on the situation of the motors and their placement, we might need to
    // invert this? So that the motors actually shoot the ball and dont just spin it
    // in place
    public static DifferentialDrive shooters = new DifferentialDrive(constantWheel, changingWheel);
    public static HashMap<Double, Double> yValuesToSpeeds = new HashMap<>();

    public HumphreyShooter() {
        // input all needed values
        yValuesToSpeeds.put(-20.0, 3.0);
    }

    public double getSpeed(double yValue) {
        double smallerY;
        double largerY;
        Double[] keys = (Double[]) yValuesToSpeeds.keySet().toArray();
        largerY = keys[keys.length - 1];
        smallerY = keys[keys.length - 2];
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] > yValue) {
                largerY = keys[i];
                smallerY = keys[i - 1];
                break;
            }
        }
        double percentage = (yValue - smallerY) / (largerY - smallerY);
        double speed = Math.abs(yValuesToSpeeds.get(smallerY)
                + percentage * (yValuesToSpeeds.get(largerY) - yValuesToSpeeds.get(smallerY)));
        return speed;
    }

    public static void shoot(double wheelSpeed) {
        shooters.tankDrive(constantWheelSpeed, wheelSpeed);
        // System.out.println("shooting: " + constantWheelSpeed + ", " + wheelSpeed);
    }

    public static void stopShooting() {
        shooters.tankDrive(0, 0);
    }

    public static void shooterIntake() {
        intakeWheel.set(1);
        // System.out.println("shooter intake");
    }

    public static void stopShooterIntake() {
        intakeWheel.set(0);
    }
}
