package frc.robot.subsystems;

public class AimBot {
    private final static float TURNING_ERROR_MARGIN = 4f;
    public static boolean orientToGoal(double angle, DriveHumphrey drive) {

        // turn the robot (control the robot)
        if (angle < -TURNING_ERROR_MARGIN) {
            // turn to the left
            System.out.println("Auto turning left");
            drive.drive(0, -0.62);
        } else if (angle > TURNING_ERROR_MARGIN) {
            // turn to the right
            System.out.println("Auto turning right");
            drive.drive(0, 0.62);
        } else {
            System.out.println("Oriented");
            drive.drive(0, 0);
            return true;
        }
        return false;

    }

}