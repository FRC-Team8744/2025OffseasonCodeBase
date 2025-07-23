// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanism;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
  private final SparkMax m_armMotor1;
  private final SparkMax m_armMotor2;
  private final SparkMax m_armMotor3;
  private final SparkMax m_armMotor4;
  private final RelativeEncoder m_armEncoder1;
  private final RelativeEncoder m_armEncoder2;
  private final RelativeEncoder m_armEncoder3;
  private final RelativeEncoder m_armEncoder4;

  private final SparkBaseConfig armConfig = new SparkMaxConfig().smartCurrentLimit(40);
  private final SparkBaseConfig armConfig1 = new SparkMaxConfig().smartCurrentLimit(40);

  

  /** Creates a new Arm. */
  public Arm() {
    armConfig1.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);

    m_armMotor1 = new SparkMax(Constants.SwerveConstants.kLeftArmMotor1, MotorType.kBrushless);

    m_armMotor1.configure(armConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armMotor2 = new SparkMax(Constants.SwerveConstants.kkLeftArmMotor2, MotorType.kBrushless);

    m_armMotor2.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armMotor3 = new SparkMax(Constants.SwerveConstants.kRightArmMotor1, MotorType.kBrushless);

    m_armMotor3.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armMotor4 = new SparkMax(Constants.SwerveConstants.kRightArmMotor2, MotorType.kBrushless);

    m_armMotor4.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_armEncoder1 = m_armMotor1.getEncoder();
    m_armEncoder2 = m_armMotor1.getEncoder();
    m_armEncoder3 = m_armMotor1.getEncoder();
    m_armEncoder4 = m_armMotor1.getEncoder();
  }

  public void stopMotors(){
    m_armMotor1.stopMotor();
    m_armMotor2.stopMotor();
    m_armMotor3.stopMotor();
    m_armMotor4.stopMotor();
  }

  public void runMotor(double speed) {
    m_armMotor1.set(speed);
  }

  public void setAngle(Double angle) {
    m_armEncoder1.setPosition(angle / 360);
    m_armEncoder2.setPosition(angle / 360);
    m_armEncoder3.setPosition(angle / 360);
    m_armEncoder4.setPosition(angle / 360);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
