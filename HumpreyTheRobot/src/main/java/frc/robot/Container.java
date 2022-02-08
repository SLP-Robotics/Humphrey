package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Container {

    public Joystick left = new Joystick(0);
    public Joystick center = new Joystick(1);
    public Joystick right = new Joystick(2);

    public void configureButtons(){
        left.getY();
        double speed = left.getY();
        double direction = left.getX();





    }



}





