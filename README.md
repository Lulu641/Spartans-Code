# Spartans-Code
Official FTC Spartans robotics software repository featuring Java-based drivetrain controls, autonomous navigation, sensor integration, and competition code.


# Spartans-Code

## Two robot programs

- **SpartansTeleOp.java**: mecanum driving plus one intake motor.
- **SpartansAuto.java**: a basic encoder driving routine, currently disabled.

The Java files are inside `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.

## Controls

**Gamepad 1:** left stick drives/slides, right stick turns, left bumper slows down.

**Gamepad 2:** hold right trigger to collect; hold left trigger to eject. Release both, or press both together, to stop the intake.

## Motor names

Use these exact names in the robot configuration, or change them in the code:

- `front_left_drive`
- `front_right_drive`
- `back_left_drive`
- `back_right_drive`
- `intake` (one DC motor)

Check motor directions before driving. Intake power is limited to 60%, an untested starting setting. The exact motor model you called HDX still needs to be matched to its physical label in the robot configuration. Five DC motors need more ports than one Control Hub provides; the intake needs a free port on an additional supported motor controller, such as a configured Expansion Hub.

## Autonomous

The demo drives forward 12 inches, then right 12 inches. It does not operate the intake. The example 537.7 encoder counts per output turn, 96 mm wheel diameter, 1:1 external gearing and 1.0 strafe correction are unverified. Check the actual motor/encoder model, wiring, directions and measured travel before removing `@Disabled`. It has a five-second timeout per move and stops on STOP; wheel slip and heading drift can affect accuracy.

## Upload and use

Unzip this download. Upload the contents of the inner `Spartans-Code` folder, keeping the folder paths. Replace older copies of the same two Java files. Remove the older `teamcode/spartans/` version if you are switching to these programs. If you uploaded `SpartansIntakeTest.java`, that separate test is optional and not needed by these two programs.

This ZIP contains only your robot code and this guide. To build and run it, put these files into an existing FTC SDK Android Studio project. Both Java files already passed a compile check against FTC SDK v12.0. A full Android build and physical robot testing have not been completed.
