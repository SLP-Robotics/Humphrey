package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Intake {

    // change motor names and change values
    static WPI_VictorSPX intakeMotor1 = new WPI_VictorSPX(9);
    // static WPI_VictorSPX intakeMotor2 = new WPI_VictorSPX(9);

    public static final double intakeWheelSpeed = 0.6;
    // I don't know how fast this needs to be, I have it set really slow so nothing
    // breaks

    public static void intakeBall(boolean intakeOrNot) {
        if (intakeOrNot) {
            intakeMotor1.set(intakeWheelSpeed);
            System.out.println("Intaking");
        } else {
            intakeMotor1.set(0);
        }
    }

    public static void reverse() {
        intakeMotor1.set(-0.2);
    }

}
