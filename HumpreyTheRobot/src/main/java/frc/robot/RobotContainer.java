package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class RobotContainer {

    public Joystick left = new Joystick(0);
    public Joystick center = new Joystick(1);
    public Joystick right = new Joystick(2);

    public double speed, direction;

    public RobotContainer(){
        configureButtons();
    }

    public void configureButtons(){
        speed = left.getY();
        direction = left.getX();
    }
}





