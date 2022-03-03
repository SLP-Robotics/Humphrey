package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

    public Joystick left = new Joystick(0);
    public Joystick center = new Joystick(1);
    public Joystick right = new Joystick(2);
    final JoystickButton speedBoost = new JoystickButton(center, 3);

    public double speed, direction;
    public boolean boostEnabled;

    public RobotContainer(){
        readButtons();
    }

    public void readButtons(){
        speed = center.getY();
        direction = left.getX();
        boostEnabled = speedBoost.get();
    }
}





