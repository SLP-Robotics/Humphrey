package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Intake {
    WPI_VictorSPX FlippyThingMotor1;
    WPI_VictorSPX OverTheBumperMotor2;

    public void OverTheBumper() {
        
        OverTheBumperMotor2 = new WPI_VictorSPX(2);
        
        
    
    }


}
 
