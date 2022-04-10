// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.AimBot;
import frc.robot.subsystems.ColorChecker;
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

  private final DriveHumphrey drivehumphrey = new DriveHumphrey();
  public RobotContainer m_robotContainer;
  private final ColorChecker cchecker = new ColorChecker();
  public int autoRuns = 0;
  private long autoStartTime = 0;
  public static final double reverseTimeS = 1;
  NetworkTableInstance inst;
  NetworkTable table;
  public static final double offsetDir = -0.3;
  // This is the offset amount for the robot's driving: must be changed when new
  // weight is added

  public int loadingCounter = 0;
  public boolean currentlyShooting = false;
  public static final double loadTime = 200;// Each of these are in the number of loops executed
  public static final double revTime = 50;// Each of which takes up 50 ms
  public static HumphreyShooter shooter = new HumphreyShooter();
  // public double joystickSetShooterSpeed = 0;

  private void autoCargo() {
    NetworkTableEntry controlActionTable = table.getEntry("action");
    String action = controlActionTable.getString("none");
    if (action.equals("stop")) {
      System.out.println("stop");
      drivehumphrey.drive(0, -0.6);
    } else if (action.equals("right")) {
      System.out.println("right");
      drivehumphrey.drive(0.6, -0.6);
    } else if (action.equals("left")) {
      System.out.println("left");
      drivehumphrey.drive(0.6, 0.6);
    } else if (action.equals("center")) {
      System.out.println("center");
      drivehumphrey.drive(0.5, 0);
    }
  }

  private void shootingRoutine() {
    shooter.shoot(shooter.getSpeed(limelight.y));
    if (loadingCounter > loadTime) {// If it is past the time to load
      currentlyShooting = false;
      shooter.stopShooting();// Stop the system from spinning the shooter motors
      shooter.stopShooterIntake();
      return;
      // Because the lone intake wheel in the shooter system is set by way of the
      // "motor.set" method
    } else if (shooter.isReadyToFeed()) {
      loadingCounter++;
      // If the current point in time is between the time it takes to rev and the time
      // it takes to load
      shooter.shooterIntake();
      // System.out.println("Intaking and shooting @ " + joystickSetShooterSpeed);
    }
    // This right now just sets the variable shooter wheel to the input from the
    // third joystick

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
    autoStartTime = System.currentTimeMillis();
    autoRuns = 0;
  }

  @Override
  public void autonomousPeriodic() {
    limelight.getGoalPos();
    final long currentAutoTime = System.currentTimeMillis() - autoStartTime;
    if (currentAutoTime < (reverseTimeS * 1000)) {
      drivehumphrey.drive(0.75, 0);
    } else {
      // orient to goal and shoot preloaded cargo before autoCargo
      if (cchecker.ballPresent()) {
        // Ball is here
        if (!currentlyShooting) {
          limelight.getGoalPos();
          if (AimBot.orientToGoal(limelight.x, drivehumphrey)) {
            currentlyShooting = true;
            loadingCounter = 0;
            System.out.println("Attempting to auto shoot");
          } else {
            System.out.println("Attempting to align");
          }
        } else {
          shootingRoutine();
          drivehumphrey.drive(0, 0);
        }
      }
    }
  }

  @Override
  public void teleopInit() {
    loadingCounter = 0;
    System.out.println("starting teleop");
    currentlyShooting = false;
  }

  @Override
  public void teleopPeriodic() {
    limelight.getGoalPos();
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
        // joystickSetShooterSpeed = (2125);
        // System.out.println(joystickSetShooterSpeed);
        loadingCounter = 0;
      }
    }
    if (currentlyShooting) {
      shootingRoutine();
    } else if (m_robotContainer.manualShooterIntake) {
      shooter.load2Intake();
    } else if (m_robotContainer.shooterIntakeReverse) {
      shooter.reverseShooterIntake();
    } else {
      shooter.stopShooterIntake();
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

  private long startedRunning = 0;

  @Override
  public void testInit() {
    startedRunning = System.currentTimeMillis();
  }

  private double target_rpm = 2000;
  private boolean wasAtSpeed = false;

  @Override
  public void testPeriodic() {
    m_robotContainer.readButtons();
    // drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
    shooter.shoot(target_rpm);
    if (!wasAtSpeed && shooter.isReadyToFeed()) {
      wasAtSpeed = true;
      System.out.println("Spinup took " + (System.currentTimeMillis() - startedRunning));
    }

  }
}
