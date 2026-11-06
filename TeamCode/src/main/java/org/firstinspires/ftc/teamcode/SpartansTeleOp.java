package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/** Four-motor, robot-relative mecanum drive. No intake or IMU required. */
@TeleOp(name = "Spartans Drive Only", group = "Spartans")
public class SpartansTeleOp extends LinearOpMode {
    private static final double DRIVE_SPEED = 0.50;
    private static final double SLOW_SPEED = 0.25;
    private static final double STICK_DEADBAND = 0.05;

    @Override
    public void runOpMode() throws InterruptedException {
        // Names must match the active robot configuration exactly.
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "front_left_drive");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "back_left_drive");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "back_right_drive");
        DcMotor[] motors = {frontLeft, frontRight, backLeft, backRight};

        try {
            for (DcMotor motor : motors) {
                motor.setPower(0);
            }

            // Starting directions: verify with the wheels off the ground.
            // With forward input, ALL wheels must propel the robot forward.
            // Flip FORWARD <-> REVERSE only on any motor that runs backward.
            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);
            frontRight.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.FORWARD);

            for (DcMotor motor : motors) {
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            telemetry.addLine("Ready: FOUR drive motors only. Mecanum wheels required.");
            telemetry.addLine("Gamepad 1: left stick = drive/strafe; right stick X = turn.");
            telemetry.addLine("Hold left bumper for 25% speed; normal limit is 50%.");
            telemetry.update();

            waitForStart();

            while (opModeIsActive()) {
                // These directions are relative to the FRONT OF THE ROBOT.
                double forward = deadband(-gamepad1.left_stick_y);
                double strafe = deadband(gamepad1.left_stick_x);
                double turn = deadband(gamepad1.right_stick_x);

                double frontLeftPower = forward + strafe + turn;
                double frontRightPower = forward - strafe - turn;
                double backLeftPower = forward - strafe + turn;
                double backRightPower = forward + strafe - turn;

                // Scale all wheels together to preserve the requested motion.
                double largest = Math.max(1.0,
                        Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
                double speed = gamepad1.left_bumper ? SLOW_SPEED : DRIVE_SPEED;
                frontLeftPower = frontLeftPower / largest * speed;
                frontRightPower = frontRightPower / largest * speed;
                backLeftPower = backLeftPower / largest * speed;
                backRightPower = backRightPower / largest * speed;

                frontLeft.setPower(frontLeftPower);
                frontRight.setPower(frontRightPower);
                backLeft.setPower(backLeftPower);
                backRight.setPower(backRightPower);

                telemetry.addData("Speed limit", "%.0f%%", speed * 100);
                telemetry.addData("Front L / R", "%.2f / %.2f", frontLeftPower, frontRightPower);
                telemetry.addData("Back L / R", "%.2f / %.2f", backLeftPower, backRightPower);
                telemetry.update();
                idle();
            }
        } finally {
            // Stop the drivetrain on STOP or when the control loop exits.
            for (DcMotor motor : motors) {
                motor.setPower(0);
            }
        }
    }

    private static double deadband(double input) {
        return Math.abs(input) < STICK_DEADBAND ? 0.0 : input;
    }
}
