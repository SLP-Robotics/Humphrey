package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Intake {

    //change motor names and change values
    static WPI_VictorSPX rightMotor1 = new WPI_VictorSPX(7);
    static WPI_VictorSPX rightMotor2 = new WPI_VictorSPX(8);
   
    public static final double constantWheelSpeed = 0.35;     
    
    public static DifferentialDrive drive = new DifferentialDrive(rightMotor1, rightMotor2);
    
    public static void go(){
        drive.tankDrive(constantWheelSpeed, constantWheelSpeed);
        System.out.println("Intaking");
    }


}
 
