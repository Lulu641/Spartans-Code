# Spartans — Mecanum Drive Only

`SpartansTeleOp.java` controls four mecanum drive motors. The intake is disabled in this version: there is no intake hardware lookup or intake power command. Gamepad 2 does nothing in this OpMode. You can leave the intake in your robot configuration; it is not required by this program.

## Install

- Android Studio: replace the existing `SpartansTeleOp.java` in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/` with the included file, then build and deploy your FTC SDK project.
- OnBot Java: replace the contents of your existing `SpartansTeleOp.java` with the included source, or upload the file, then click **Build Everything**. The filename must be exactly `SpartansTeleOp.java` (capitalization matters).
- Keep only one class named `org.firstinspires.ftc.teamcode.SpartansTeleOp`; do not leave duplicate copies in other folders. Paste actual Java, without the backslashes or `&#x20;` formatting from the chat text.
- On Driver Station, select **Spartans Drive Only** under TeleOp. `SpartansIntakeTest` is a separate program that still operates the intake if you select it; do not select that test to drive. This download does not modify or include that separate file.

## Configuration

Match these four motor names exactly in your active robot configuration. Each name must point to its actual wheel location, viewed with the robot's front facing forward:

| Wheel | Configuration name |
| --- | --- |
| Front left | `front_left_drive` |
| Front right | `front_right_drive` |
| Back left | `back_left_drive` |
| Back right | `back_right_drive` |

Select the motor type that matches the physical motor label. This TeleOp uses open-loop motor power; it does not require encoder cables, encoder calibration, an IMU, or a fifth motor. Mecanum wheel rollers must form an **X when viewed from above**, as described in FIRST's sample.

## Controls — Gamepad 1

- Left stick forward/back: drive forward/back.
- Left stick left/right: slide left/right.
- Right stick left/right: rotate counterclockwise/clockwise.
- Hold left bumper: limit power to 25%; otherwise power is limited to 50%.
- Release the sticks: command zero power with BRAKE behavior.
- Driver Station STOP: exit the loop and command zero power.

Directions are relative to the robot, not the field. Normalization preserves the proportions between wheel commands during combined movements.

## First check on the robot

1. Support the robot securely with all wheels off the ground. Keep hands clear of the wheels.
2. Select **Spartans Drive Only**, press INIT, then START. With centered sticks, wheels should remain still.
3. Hold the left bumper and gently push the left stick forward. All four wheels must turn so their bottom surfaces would push the floor backward, moving the robot forward.
4. If a wheel turns backward, press STOP, flip that motor's `setDirection` between `FORWARD` and `REVERSE`, rebuild, and repeat. The supplied left-reversed/right-forward settings are a starting assumption.
5. Test on clear floor at low input: forward, backward, strafe right, strafe left, then turn. If forward works but strafing does not, verify the configured wheel locations and the mecanum roller X pattern.
6. Check that releasing the sticks and pressing STOP stop the motors before increasing speed.

## Verification

The included Java file compiled successfully against the real FTC RobotCore SDK **12.0.0**, using Java 8 source/target settings. Its extracted drive calculations passed nine directional/deadband/slow-mode checks and 10,000 mixed-input power-limit checks. A full Android app build, deployment, and physical robot testing have not been performed. Wiring, configured wheel locations, wheel installation, and motor directions must be checked on your robot.

## Official references

- [FIRST four-motor mecanum/omni sample](https://github.com/FIRST-Tech-Challenge/FtcRobotController/blob/master/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples/BasicOmniOpMode_Linear.java)
- [FIRST OnBot Java build and run guide](https://ftc-docs.firstinspires.org/en/latest/programming_resources/tutorial_specific/onbot_java/creating_op_modes/Creating-and-Running-an-Op-Mode-(OnBot-Java).html)

This ZIP contains the drive-only source and this guide. It is not a standalone FTC SDK Android Studio project.
