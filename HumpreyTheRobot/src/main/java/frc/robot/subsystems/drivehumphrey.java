package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class drivehumphrey {
    WPI_VictorSPX rightMotor1 = new WPI_VictorSPX(0);
    WPI_VictorSPX rightMotor2 = new WPI_VictorSPX(1);
    WPI_VictorSPX leftMotor1 = new WPI_VictorSPX(2);
    WPI_VictorSPX leftMotor2 = new WPI_VictorSPX(3);

    public MotorControllerGroup leftSide = new MotorControllerGroup(leftMotor2, leftMotor1);
    public MotorControllerGroup rightSide = new MotorControllerGroup(rightMotor2, rightMotor1);

    public DifferentialDrive drive = new DifferentialDrive(leftSide, rightSide);
    
    public void drive(double speed, double rotation) {
        
        drive.arcadeDrive(speed * 0.75, rotation);

    }
}