package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Intake {

    //change motor names and change values
    static WPI_VictorSPX intakeMotor1 = new WPI_VictorSPX(7);
    static WPI_VictorSPX intakeMotor2 = new WPI_VictorSPX(8);
   
    public static final double intakeWheelSpeed = 0.1;
    //I don't know how fast this needs to be, I have it set really slow so nothing breaks    
    
    public static DifferentialDrive drive = new DifferentialDrive(intakeMotor1, intakeMotor2);
    
    public static void go(){
        drive.tankDrive(intakeWheelSpeed, intakeWheelSpeed);
        System.out.println("Intaking");
    }


}
