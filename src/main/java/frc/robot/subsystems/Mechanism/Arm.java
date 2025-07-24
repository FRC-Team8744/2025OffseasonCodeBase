// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanism;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase {
  private final SparkMax m_armMotor1;
  private final SparkMax m_armMotor2;
  private final SparkMax m_armMotor3;
  private final SparkMax m_armMotor4;
  private final RelativeEncoder m_armEncoder1;
  private final RelativeEncoder m_armEncoder2;
  private final RelativeEncoder m_armEncoder3;
  private final RelativeEncoder m_armEncoder4;

  private final SparkClosedLoopController m_armPID;

  private final double armGearRatio = (54/12) * (60/14);
  private final double L1 = 0.115 / 1.5;
  private final double intake = 0 / 1.5;
  private final double climb = 0.294 / 2.1;
  private final double stow = 0.195 / 1.8;
  private final double startingPosition = 0.877;

  private double minimumAngle = 0;
  private double maximumAngle = .75;

  private final SparkBaseConfig armConfig = new SparkMaxConfig().smartCurrentLimit(40).follow(16, false).idleMode(IdleMode.kBrake);
  private final SparkBaseConfig armConfigRight = new SparkMaxConfig().smartCurrentLimit(40).follow(16, true).idleMode(IdleMode.kBrake);
  private final SparkBaseConfig armConfig2 = new SparkMaxConfig().smartCurrentLimit(40).idleMode(IdleMode.kBrake).inverted(true);
//  private final boolean henrikrules = true;
  /** Creates a new Arm. */
  public Arm() {
    armConfig2.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);

    // armConfig2.encoder.positionConversionFactor(360);
    armConfig2.absoluteEncoder.inverted(true);

    armConfig.closedLoop.pidf(2,0,0.001,2);
    armConfig2.closedLoop.pidf(2,0,0.001,2);
    armConfigRight.closedLoop.pidf(2,0,0.001,2);

    // armConfig2.closedLoop.positionWrappingEnabled(true);
    // armConfig2.closedLoop.positionWrappingMaxInput(1);
    // armConfig2.closedLoop.positionWrappingMinInput(0);

    m_armMotor1 = new SparkMax(Constants.SwerveConstants.kLeftArmMotor1, MotorType.kBrushless);

    m_armMotor1.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armMotor2 = new SparkMax(Constants.SwerveConstants.kLeftArmMotor2, MotorType.kBrushless);

    m_armMotor2.configure(armConfig2, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armPID = m_armMotor2.getClosedLoopController();

    m_armMotor3 = new SparkMax(Constants.SwerveConstants.kRightArmMotor1, MotorType.kBrushless);

    m_armMotor3.configure(armConfigRight, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armMotor4 = new SparkMax(Constants.SwerveConstants.kRightArmMotor2, MotorType.kBrushless);

    m_armMotor4.configure(armConfigRight, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armEncoder1 = m_armMotor1.getEncoder();
    m_armEncoder2 = m_armMotor2.getEncoder();
    m_armEncoder3 = m_armMotor3.getEncoder();
    m_armEncoder4 = m_armMotor4.getEncoder();
  }

  public void stopMotors(){
    m_armMotor1.stopMotor();
    m_armMotor2.stopMotor();
    m_armMotor3.stopMotor();
    m_armMotor4.stopMotor();
  }

  public void runMotor(double speed) {
    m_armMotor2.set(speed);
  }

  public void setAngle(double angle) {
    if (angle >= maximumAngle) angle = maximumAngle;
    if (angle <= minimumAngle) angle = minimumAngle;
    // m_armEncoder1.setPosition(angle / 360);
    m_armPID.setReference(angle / 360, ControlType.kMAXMotionPositionControl);
    // m_armEncoder3.setPosition(angle / 360);
    // m_armEncoder4.setPosition(angle / 360);
  }

  public void setL1() {
    m_armPID.setReference(L1, ControlType.kPosition);
  
  }
  
  public void setClimb(){
    m_armPID.setReference(climb, ControlType.kPosition);
  }

  public void setStow(){
    m_armPID.setReference(stow, ControlType.kPosition);
  }

  public void setIntake(){
    m_armPID.setReference(intake, ControlType.kPosition);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Arm encoder 2", m_armMotor2.getAbsoluteEncoder().getPosition());
    SmartDashboard.putNumber("Arm encoder with gearRatio", m_armMotor2.getAbsoluteEncoder().getPosition());
    SmartDashboard.putBoolean("Motor1Follower", m_armMotor1.isFollower());
    SmartDashboard.putBoolean("Motor3Follower", m_armMotor3.isFollower());
    SmartDashboard.putBoolean("Motor4Follower", m_armMotor4.isFollower());
    SmartDashboard.putNumber("Motor 1", m_armMotor1.getAppliedOutput());
    SmartDashboard.putNumber("Motor 2", m_armMotor2.getAppliedOutput());
    SmartDashboard.putNumber("Motor 3", m_armMotor3.getAppliedOutput());
    SmartDashboard.putNumber("Motor 4", m_armMotor4.getAppliedOutput());
    // SmartDashboard.putNumber("PID reference", m_armMotor2.get)
    // SmartDashboard.putNumber("minuim and"(), L1);
  }
}