package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class AimBot {
    private static final float TURNING_ERROR_MARGIN = 2f;
    public boolean orientToGoal(double angle, DriveHumphrey drive, NetworkTable table) {
        NetworkTableEntry driverDisplay = table.getEntry("autoDisplay");
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