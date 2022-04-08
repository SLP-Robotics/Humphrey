package frc.robot.subsystems;

public class AimBot {
    private final static float TURNING_ERROR_MARGIN = 2f;
    public static boolean orientToGoal(double angle, DriveHumphrey drive) {

        // turn the robot (control the robot)
        if (angle < -TURNING_ERROR_MARGIN) {
            // turn to the left
            System.out.println("Auto turning left");
            drive.drive(0, -0.5);
        } else if (angle > TURNING_ERROR_MARGIN) {
            // turn to the right
            System.out.println("Auto turning right");
            drive.drive(0, 0.5);
        } else {
            System.out.println("Oriented");
            return true;
        }
        return false;

    }

}