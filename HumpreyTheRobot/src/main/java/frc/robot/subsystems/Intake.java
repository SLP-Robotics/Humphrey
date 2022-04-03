package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Intake {

    // change motor names and change values
    static WPI_VictorSPX intakeMotor1 = new WPI_VictorSPX(9);
    // static WPI_VictorSPX intakeMotor2 = new WPI_VictorSPX(9);

    public static final double intakeWheelSpeed = 0.6;
    // I don't know how fast this needs to be, I have it set really slow so nothing
    // breaks

    public static DifferentialDrive drive = new DifferentialDrive(intakeMotor1, intakeMotor1);
    // Will this break everything? I don't know

    public static void intakeBall(boolean intakeOrNot) {
        if (intakeOrNot) {
            intakeMotor1.set(intakeWheelSpeed);
            System.out.println("Intaking");
        } else {
            intakeMotor1.set(0);
        }
    }

}
