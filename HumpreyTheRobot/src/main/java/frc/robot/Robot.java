// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.AimBot;
import frc.robot.subsystems.DriveHumphrey;
import frc.robot.subsystems.HumphreyShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.limelight;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  public static DriveHumphrey drivehumphrey = new DriveHumphrey();
  public RobotContainer m_robotContainer;
  public int autoRuns = 0;
  public static final double reverseTimeS = 0.1;
  public static final double autonomousPeriodTimeS = 0.02;
  public static final double autonomousReverseCycles = reverseTimeS / autonomousPeriodTimeS;
  NetworkTableInstance inst;
  NetworkTable table;
  public static final double offsetDir = -0.3;
  // This is the offset amount for the robot's driving: must be changed when new
  // weight is added

  public int shootingCounter = 0;
  public boolean currentlyShooting = false;
  public static final double loadTime = 200;// Each of these are in the number of loops executed
  public static final double revTime = 50;// Each of which takes up 20 ms
  public static HumphreyShooter shooter = new HumphreyShooter();
  public double joystickSetShooterSpeed = 0;

  private void autoCargo() {
    NetworkTableEntry controlActionTable = table.getEntry("action");
    String action = controlActionTable.getString("none");
    if (action.equals("stop")) {
      System.out.println("stop");
      drivehumphrey.drive(0, 0.6);
    } else if (action.equals("right")) {
      System.out.println("right");
      drivehumphrey.drive(-0.6, 0.6);
    } else if (action.equals("left")) {
      System.out.println("left");
      drivehumphrey.drive(-0.6, -0.6);
    } else if (action.equals("center")) {
      System.out.println("center");
      drivehumphrey.drive(-0.5, 0);
    }
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    DriveHumphrey.leftSide.setInverted(true);
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("SmartDashboard");

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    System.out.println("autoinit");
    autoRuns = 0;
  }

  @Override
  public void autonomousPeriodic() {
    autoRuns += 1;
    if (autoRuns <= autonomousReverseCycles) {
      drivehumphrey.drive(0.75, 0);
    } else {
      drivehumphrey.drive(0, 0);
    }
    // orient to goal and shoot preloaded cargo before autoCargo
    if (autoRuns >= autonomousReverseCycles) {
      autoCargo();
    }
  }

  @Override
  public void teleopInit() {
    shootingCounter = 0;
    System.out.println("starting teleop");

  }

  @Override
  public void teleopPeriodic() {

    shootingCounter++;

    boolean drivingForwards = -m_robotContainer.speed > 0;
    m_robotContainer.readButtons();
    if (m_robotContainer.boostEnabled) {// Turbo mode
      if (Math.abs(m_robotContainer.direction) < 0.1 && Math.abs(m_robotContainer.speed) > 0.1) {
        // v Flip sign if offset direction is backwards
        drivehumphrey.drive(m_robotContainer.speed,
            m_robotContainer.direction - (drivingForwards ? offsetDir : -offsetDir));
        // Drive with the offset
      } else {
        drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
        // functionally the same as driving with 0 turn
      }
    } else {
      if (Math.abs(m_robotContainer.direction) < 0.1 && Math.abs(m_robotContainer.speed) > 0.1) {// Driving straight
        // v Flip sign if offset direction is backwards
        drivehumphrey.drive(m_robotContainer.speed * 0.75,
            m_robotContainer.direction - (drivingForwards ? offsetDir : -offsetDir));
        // Drive with the offset
      } else {
        drivehumphrey.drive(m_robotContainer.speed * 0.75, m_robotContainer.direction);
        // functionally the same as driving with 0 turn
      }

      if (m_robotContainer.autoCargoEnabled) {
        autoCargo();
      }
      if (m_robotContainer.aimBotEnabled) {
        limelight.getGoalPos();
        AimBot.orientToGoal(limelight.x, drivehumphrey);
      }
    }

    if (m_robotContainer.shootInitiated) {
      if (!currentlyShooting) {
        currentlyShooting = true;
        joystickSetShooterSpeed = (2125); // TODO: replace this system with a lookup table
        System.out.println(joystickSetShooterSpeed);
        shootingCounter = 0;
      }
    }
    if (currentlyShooting) {
      if (shootingCounter <= revTime) {
        // If the current point in time is between when shooting started and the time it
        // takes to rev
        HumphreyShooter.shoot(joystickSetShooterSpeed);
        System.out.println("Revving @ " + joystickSetShooterSpeed);
      } else if ((shootingCounter > revTime) && (shootingCounter <= (revTime + loadTime))) {
        // If the current point in time is between the time it takes to rev and the time
        // it takes to load
        HumphreyShooter.shoot(joystickSetShooterSpeed);
        HumphreyShooter.shooterIntake();
        // System.out.println("Intaking and shooting @ " + joystickSetShooterSpeed);
      } else if (shootingCounter > (revTime + loadTime)) {// If it is past the time to load
        currentlyShooting = false;
        HumphreyShooter.stopShooting();// Stop the system from spinning the shooter motors
        HumphreyShooter.stopShooterIntake();
        // Because the lone intake wheel in the shooter system is set by way of the
        // "motor.set" method
      }
      // TODO: change the shoot input to a look up table
      // This right now just sets the variable shooter wheel to the input from the
      // third joystick
    } else if (m_robotContainer.manualShooterIntake) {
      HumphreyShooter.load2Intake();
    } else if (m_robotContainer.shooterIntakeReverse) {
      HumphreyShooter.reverseShooterIntake();
    } else {
      HumphreyShooter.stopShooterIntake();
    }

    if (m_robotContainer.intakeReverse) {
      Intake.reverse();
    } else {
      Intake.intakeBall(m_robotContainer.intakeInitiated);
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
    m_robotContainer.readButtons();
    drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
  }
}
