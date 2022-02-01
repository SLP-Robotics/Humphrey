package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class drivehumphrey {
    WPI_VictorSPX rightMotor1;
    WPI_VictorSPX rightMotor2;
    WPI_VictorSPX leftMotor1;
    WPI_VictorSPX leftMotor2;

    public void drivehumphrey() {
        rightMotor1 = new WPI_VictorSPX(1);
        rightMotor2 = new WPI_VictorSPX(2);

        leftMotor1 = new WPI_VictorSPX(3);
        leftMotor2 = new WPI_VictorSPX(4);

        rightMotor2.follow(rightMotor1);
        leftMotor2.follow(leftMotor1);

    }

    public void driveleft(double speed) {
        leftMotor1.set(ControlMode.PercentOutput, speed);

    }

    public void driveright(double speed) {
        rightMotor1.set(ControlMode.PercentOutput, speed);

    }
      //Better tankDrive used exclussively with joystick(s)
      public void arcadeDrivePower(double y, double x) {
        //y is the y axis of the joystick
        //x is the x axis of the SAME joystick

        if (Math.abs(x) + Math.abs(y) < .75) {
            tankDrivePower(y + x, y - x);
        } else {
            // limits the motors from ever going over 75% speed
            double betterX = (x/(Math.abs(x)+Math.abs(y)))*.75;
            double betterY = (y/(Math.abs(x)+Math.abs(y)))*.75;
            tankDrivePower(betterY + betterX, betterY - betterX);
        }
    }

    //Runs speed methods base upon two values
    public void tankDrivePower(double leftSpeed, double rightSpeed) {
        driveleft(leftSpeed);
        driveright(rightSpeed);
    }

}
