package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C.Port;

public class ColorChecker {
    private final ColorSensorV3 sensor = new ColorSensorV3(Port.kOnboard);
    private static final int BLUE_THRESHOLD = 127;
    private static final int RED_THRESHOLD = 127;
    private static final int PROXIMITY_THRESHOLD = 127;

    public boolean isRed() {
        System.out.println("Red is " + Integer.toString(sensor.getRed()));
        return sensor.getRed() > RED_THRESHOLD;
    }

    public boolean isBlue() {
        System.out.println("Blue is " + Integer.toString(sensor.getBlue()));
        return sensor.getBlue() > BLUE_THRESHOLD;
    }

    public boolean ballPresent() {
        System.out.println("Proximity is " + Integer.toString(sensor.getProximity()));
        return sensor.getProximity() < PROXIMITY_THRESHOLD;
    }
}
