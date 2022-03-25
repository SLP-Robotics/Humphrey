package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class HumphreyShooter {
    static WPI_TalonFX constantWheel = new WPI_TalonFX(5);
    static WPI_TalonFX changingWheel = new WPI_TalonFX(6);
    static WPI_TalonFX intakeWheel = new WPI_TalonFX(7);

    public static final double constantWheelSpeed = 0.5;// This we can preset until we find a value we like
    // Based on the situation of the motors and their placement, we might need to
    // invert this? So that the motors actually shoot the ball and dont just spin it
    // in place
    public static DifferentialDrive shooters = new DifferentialDrive(constantWheel, changingWheel);

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
