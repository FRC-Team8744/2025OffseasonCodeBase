// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Mechanism.Arm;
import frc.robot.subsystems.Mechanism.ScoringMech;
/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Intake extends Command {
  private final ScoringMech m_scoringMech;
  private final Arm m_arm;
  /** Creates a new Intake. */
  public Intake(ScoringMech sm, Arm ar) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_scoringMech = sm;
    addRequirements(m_scoringMech);
     m_arm = ar;
    addRequirements(m_arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_scoringMech.runMotor(-.5);
    m_arm.stopMotors();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_scoringMech.stopMotors();
    m_arm.setStow();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
