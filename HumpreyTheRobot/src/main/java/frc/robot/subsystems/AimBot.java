package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class AimBot {
    private static final float TURNING_ERROR_MARGIN = 2f;
    NetworkTableInstance inst;
    static NetworkTable table;
    static NetworkTableEntry driverDisplay = table.getEntry("autoDisplay");
    public static boolean orientToGoal(double angle, DriveHumphrey drive) {
        String orientState;
        // turn the robot (control the robot)
            orientState = "notOriented";
        if (angle < -TURNING_ERROR_MARGIN) {
            // turn to the left
            orientState = "Orienting";
            System.out.println("Auto turning left");
            drive.drive(0, -0.5);
        } else if (angle > TURNING_ERROR_MARGIN) {
            // turn to the right
            orientState = "Orienting";
            System.out.println("Auto turning right");
            drive.drive(0, 0.5);
        } else {
            orientState = "Oriented";
            System.out.println("Oriented");
            return true;
        }
        driverDisplay.setString(orientState);
        return false;

    }

}