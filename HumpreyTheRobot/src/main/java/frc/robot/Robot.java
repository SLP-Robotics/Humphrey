// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.drivehumphrey;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  public static drivehumphrey drivehumphrey = new drivehumphrey();
  public RobotContainer m_robotContainer;
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  
  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    drivehumphrey.leftSide.setInverted(true);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    System.out.println("starting teleop");

  }

  @Override
  public void teleopPeriodic() {
    m_robotContainer.configureButtons();
    drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {
    m_robotContainer.configureButtons();
    drivehumphrey.drive(m_robotContainer.speed, m_robotContainer.direction);
  }
}






