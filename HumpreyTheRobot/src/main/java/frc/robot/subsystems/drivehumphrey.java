package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class drivehumphrey {
    static WPI_VictorSPX rightMotor1 = new WPI_VictorSPX(1);
    static WPI_VictorSPX rightMotor2 = new WPI_VictorSPX(2);
    static WPI_VictorSPX leftMotor1 = new WPI_VictorSPX(3);
    static WPI_VictorSPX leftMotor2 = new WPI_VictorSPX(4);

    public static MotorControllerGroup leftSide = new MotorControllerGroup(leftMotor2, leftMotor1);
    public static MotorControllerGroup rightSide = new MotorControllerGroup(rightMotor2, rightMotor1);

    public static DifferentialDrive drive = new DifferentialDrive(leftSide, rightSide);
    
    public static void drive(double speed, double rotation) {
        drive.arcadeDrive(speed, rotation * -0.6);

    }
}