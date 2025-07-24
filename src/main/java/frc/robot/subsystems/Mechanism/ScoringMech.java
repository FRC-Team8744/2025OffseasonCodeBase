// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanism;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ScoringMech extends SubsystemBase {
  private final SparkMax m_scoringMotorTop;
  private final SparkMax m_scoringMotorBottom;


  private final SparkBaseConfig armConfig = new SparkMaxConfig().smartCurrentLimit(40).idleMode(IdleMode.kBrake);
  /** Creates a new Arm. */
  public ScoringMech() {
     m_scoringMotorTop = new SparkMax(9, MotorType.kBrushless);

    m_scoringMotorTop.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    m_scoringMotorBottom = new SparkMax(10, MotorType.kBrushless);

    m_scoringMotorBottom.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);


  }

  public void stopMotors(){
    m_scoringMotorTop.stopMotor();
    m_scoringMotorBottom.stopMotor();
    
  }

  public void runMotor(double speed) {
    m_scoringMotorBottom.set(-speed);
    m_scoringMotorTop.set(-speed);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
