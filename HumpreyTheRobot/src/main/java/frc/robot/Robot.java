// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.AimBot;
import frc.robot.subsystems.HumphreyShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.drivehumphrey;
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

  public static drivehumphrey drivehumphrey = new drivehumphrey();
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
  public int shootingStartPoint = 14400000;// The equivalent of 4 hours
  public static final double loadTime = 300;// Each of these are in the number of loops executed
  public static final double revTime = 75;// Each of which takes up 20 ms

  public double joystickSetShooterSpeed = 0;

  public void autoCargo() {
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
    drivehumphrey.leftSide.setInverted(true);
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

    double drivingForwards = -m_robotContainer.speed / Math.abs(m_robotContainer.speed);
    m_robotContainer.readButtons();
    if (m_robotContainer.boostEnabled) {// Turbo mode
      if (Math.abs(m_robotContainer.direction) < 0.1) {
        drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction - (offsetDir * drivingForwards));
        // Drive with the offset
      } else {
        drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
        // functionally the same as driving with 0 turn
      }
    } else {
      if (Math.abs(m_robotContainer.direction) < 0.1) {// Driving straight
        drivehumphrey.drive(m_robotContainer.speed * 0.75, m_robotContainer.direction - (offsetDir * drivingForwards));
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
        AimBot.orientToGoal(limelight.x);
      }
    }

    if (m_robotContainer.shootInitiated) {
      if (!currentlyShooting) {
        currentlyShooting = true;
        shootingStartPoint = shootingCounter;
        joystickSetShooterSpeed = (m_robotContainer.inputShooterSpeed); // TODO: replace this system with a lookup table
      }
    }
    if (currentlyShooting) {
      if ((shootingStartPoint < shootingCounter) && (shootingCounter <= (shootingStartPoint + revTime))) {
        // If the current point in time is between when shooting started and the time it
        // takes to rev
        HumphreyShooter.shoot(joystickSetShooterSpeed);
        System.out.println("Revving @ " + joystickSetShooterSpeed);
      } else if (((shootingStartPoint + revTime) < shootingCounter)
          && (shootingCounter <= (shootingStartPoint + revTime + loadTime))) {
        // If the current point in time is between the time it takes to rev and the time
        // it takes to load
        HumphreyShooter.shoot(joystickSetShooterSpeed);
        HumphreyShooter.shooterIntake();
        System.out.println("Intaking and shooting @ " + joystickSetShooterSpeed);
      } else if (shootingCounter > (shootingStartPoint + revTime + loadTime)) {// If it is past the time to load
        currentlyShooting = false;
        shootingStartPoint = 14400000;
        HumphreyShooter.stopShooting();// Stop the system from spinning the shooter motors
        HumphreyShooter.stopShooterIntake();
        // Because the lone intake wheel in the shooter system is set by way of the
        // "motor.set" method
      }
      // TODO: change the shoot input to a look up table
      // This right now just sets the variable shooter wheel to the input from the
      // third joystick
    }
    if (m_robotContainer.intakeInitiated) {
      Intake.go();
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
