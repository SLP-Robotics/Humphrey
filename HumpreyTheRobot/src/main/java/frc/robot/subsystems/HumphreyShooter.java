package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


public class HumphreyShooter {
    static WPI_VictorSPX constantWheel = new WPI_VictorSPX(5);
    static WPI_VictorSPX changingWheel = new WPI_VictorSPX(6);

    public static final double constantWheelSpeed = 1;//This we can preset until we find a value we like

    public static DifferentialDrive drive = new DifferentialDrive(constantWheel, changingWheel);

    public static void shoot(double wheelSpeed){
        drive.tankDrive(constantWheelSpeed, wheelSpeed);
        System.out.println("shooting: " + constantWheelSpeed + ", " + wheelSpeed);
    }
}