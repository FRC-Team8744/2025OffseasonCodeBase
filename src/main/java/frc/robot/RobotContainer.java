// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Commands.ArmClimb;
import frc.robot.Commands.ArmDown;
import frc.robot.Commands.ArmStow;
import frc.robot.Commands.ArmUp;
import frc.robot.Commands.Intake;
import frc.robot.Commands.Score;
import frc.robot.Constants.ConstantsOffboard;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Mechanism.Arm;
import frc.robot.subsystems.Mechanism.ScoringMech;
import frc.robot.subsystems.alignment.AlignToPole;
import frc.robot.subsystems.alignment.AlignToPoleX;
// import frc.robot.subsystems.mechanisms.AlgaeMechanism;
import frc.robot.subsystems.vision.PhotonVisionGS;
import frc.robot.subsystems.vision.PhotonVisionGS2;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;



/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private PhotonVisionGS m_vision = new PhotonVisionGS();
  private PhotonVisionGS2 m_vision2 = new PhotonVisionGS2();
  private AlignToPoleX m_alignToPoleX = new AlignToPoleX();
  private AlignToPole m_alignToPoleY = new AlignToPole();
  private DriveSubsystem m_robotDrive = new DriveSubsystem(m_vision, m_vision2, m_alignToPoleX);
  private Arm m_arm = new Arm();
  private ScoringMech m_scoringMech = new ScoringMech();
  // The driver's controller
  private CommandXboxController m_driver = new CommandXboxController(OIConstants.kDriverControllerPort);
  private CommandXboxController m_coDriver = new CommandXboxController(1);
  private AutoCommandManager m_autoManager = new AutoCommandManager(m_arm, m_scoringMech);
  
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

  // Configure default commands
      m_robotDrive.setDefaultCommand(
          // The left stick controls translation of the robot.
          // Turning is controlled by the X axis of the right stick.
          new RunCommand(
              () ->
                  m_robotDrive.drive(
                      -m_driver.getLeftY()  * SwerveConstants.kMaxSpeedTeleop,
                      -m_driver.getLeftX()  * SwerveConstants.kMaxSpeedTeleop,
                      m_driver.getRightX() * ConstantsOffboard.MAX_ANGULAR_RADIANS_PER_SECOND,
                      true),
              m_robotDrive));
    // m_autoChooser = AutoBuilder.buildAutoChooser();  // Default auto will be 'Commands.none()'

    // SmartDashboard.putData("Auto Mode", m_autoChooser);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  
  private void configureButtonBindings() {
    m_driver.back().onTrue(Commands.runOnce (() -> m_robotDrive.zeroGyro()));
    m_driver.rightStick()
    .toggleOnTrue(Commands.runOnce(() -> m_robotDrive.isAutoRotate = m_robotDrive.isAutoRotate == RotationEnum.STRAFEONTARGET ? RotationEnum.NONE : RotationEnum.STRAFEONTARGET));

    m_driver.pov(0)
    .whileTrue(Commands.runOnce(() -> Constants.visionElevator = !Constants.visionElevator));

    m_driver.a()
    .whileTrue(new ArmDown(m_arm));

    m_driver.y()
    .whileTrue(new ArmUp(m_arm));

    m_driver.leftBumper()
    .whileTrue(new Intake(m_scoringMech, m_arm));

    m_driver.rightBumper()
    .whileTrue(new Score(m_scoringMech));

    m_driver.x()
    .whileTrue(new ArmClimb(m_arm));

    // m_driver.b()
    // .whileTrue(new ArmStow(m_arm));

    
    // m_coDriver.rightBumper()
    // .toggleOnTrue(Commands.runOnce(() -> m_robotDrive.leftPoint = false));

    // m_coDriver.rightTrigger()
    // .toggleOnTrue(Commands.runOnce(() -> m_robotDrive.leftPoint = false));
    
    // m_coDriver.leftBumper()
    // .toggleOnTrue(Commands.runOnce(() -> m_robotDrive.leftPoint = true));

    // m_coDriver.leftTrigger()
    // .toggleOnTrue(Commands.runOnce(() -> m_robotDrive.leftPoint = true));

    // m_coDriver.rightTrigger()
    // .toggleOnTrue(Commands.runOnce(() -> Constants.scoringMode = "Coral")
    // .alongWith(Commands.runOnce(() -> m_leds.SetSegmentByIntakeMech(Color.kWhite, 50))));

    // m_coDriver.leftTrigger()
    // .toggleOnTrue(Commands.runOnce(() -> Constants.scoringMode = "Algae")
    // .alongWith(Commands.runOnce(() -> m_leds.SetSegmentByIntakeMech(ColorInterface.Algae, 50))));

    // vision
    // m_driver.b()
    // .whileTrue(Commands.runOnce(() -> m_robotDrive.isAutoYSpeed = false).alongWith(Commands.runOnce(() -> m_robotDrive.isAutoXSpeed = false).alongWith(Commands.runOnce(() -> m_robotDrive.isAutoRotate = RotationEnum.NONE))));


    // m_coDriver.a()
    // .whileTrue(new TimerTest());
  }

  public Command getAutonomousCommand() {
    Command autoCommand = m_autoManager.getAutoManagerSelected();
    return autoCommand;
  }
}