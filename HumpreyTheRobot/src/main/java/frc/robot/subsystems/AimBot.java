package frc.robot.subsystems;

public class AimBot{

    //look up table here
    private static final float[] LOOKUP_TABLE = {};
    private float getSpeedForDistance(float distance){
        int index = (int)Math.floor(distance / SPACING);
        if (index >= LOOKUP_TABLE.length){
            return 5;
            //change value 5 to faster if far away
        }
        return LOOKUP_TABLE[index];

    }
    //motor (top, bottom)

    private static final float TURNING_ERROR_MARGIN = 0.0000001f;
    private static final float SPACING = 0.25f;

    public void shooterMotorSpeed(float distance){

        //set shooter motor speeds

    }

    public void orientToGoal(float angle, drivehumphrey drive){

        //turn the robot (control the robot)

        if (angle < -TURNING_ERROR_MARGIN ){
            //turn to the left
            drive.drive( 0 , -0.5);
        }
        else if (angle > TURNING_ERROR_MARGIN){
            //turn to the right
            drive.drive( 0 , 0.5);
        }


    }




}