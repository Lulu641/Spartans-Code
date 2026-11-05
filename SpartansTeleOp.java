package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// This name appears in the Driver Station's TeleOp list.
@TeleOp(name = "Spartans TeleOp", group = "Spartans")
public class SpartansTeleOp extends LinearOpMode {

    // Change this after testing how fast your actual intake should run.
    private static final double INTAKE_MAX_POWER = 0.60;

    @Override
    public void runOpMode() throws InterruptedException {
        // These names MUST match the motor names in your robot configuration.
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "front_left_drive");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "back_left_drive");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "back_right_drive");

        // One DC intake motor. This name must match your Hub configuration.
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setPower(0);
        // If positive power pushes pieces OUT, change FORWARD to REVERSE here.
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Starting assumption: reverse the left side. Check this on your robot.
        // Change REVERSE to FORWARD (or vice versa) if a wheel runs backward.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // BRAKE helps the wheels resist rolling when their power is zero.
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // For TeleOp, the joystick directly controls motor power.
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        telemetry.addLine("Ready. Check motor names and directions before driving.");
        telemetry.addLine("Gamepad 2: right trigger = intake, left trigger = reverse.");
        telemetry.update();

        try {
            waitForStart(); // Nothing drives until you press START.

            while (opModeIsActive()) { // Repeat until you press STOP.
                // Pushing a stick forward gives a negative Y value, so flip its sign.
                double forward = -gamepad1.left_stick_y;
                double strafe = gamepad1.left_stick_x; // Positive = right.
                double turn = gamepad1.right_stick_x;  // Positive = clockwise.

                // Ignore tiny stick movements near the center.
                if (Math.abs(forward) < 0.05) forward = 0;
                if (Math.abs(strafe) < 0.05) strafe = 0;
                if (Math.abs(turn) < 0.05) turn = 0;

                // Each wheel gets a different combination to make mecanum motion.
                double frontLeftPower = forward + strafe + turn;
                double frontRightPower = forward - strafe - turn;
                double backLeftPower = forward - strafe + turn;
                double backRightPower = forward + strafe - turn;

                // Motor power must stay between -1 and 1. Divide all four equally
                // so their proportions stay correct during combined movements.
                double largest = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
                largest = Math.max(largest, Math.abs(backLeftPower));
                largest = Math.max(largest, Math.abs(backRightPower));
                if (largest > 1) {
                    frontLeftPower /= largest;
                    frontRightPower /= largest;
                    backLeftPower /= largest;
                    backRightPower /= largest;
                }

                // Normal speed is 50%. Hold the left bumper for 25% speed.
                double speed = 0.50;
                if (gamepad1.left_bumper) speed = 0.25;

                frontLeft.setPower(frontLeftPower * speed);
                frontRight.setPower(frontRightPower * speed);
                backLeft.setPower(backLeftPower * speed);
                backRight.setPower(backRightPower * speed);

                // Intake uses GAMEPAD 2 so the driver can keep driving normally.
                // Hold right trigger to collect, or left trigger to spit pieces out.
                // Releasing both (or pressing both) stops the intake.
                double intakePower = 0;
                boolean collect = gamepad2.right_trigger > 0.05;
                boolean eject = gamepad2.left_trigger > 0.05;
                if (collect && !eject) {
                    intakePower = gamepad2.right_trigger * INTAKE_MAX_POWER;
                } else if (eject && !collect) {
                    intakePower = -gamepad2.left_trigger * INTAKE_MAX_POWER;
                }
                intake.setPower(intakePower);
                telemetry.addData("Intake power", intakePower);

                // These numbers help you check that every encoder is connected.
                telemetry.addData("Front left encoder", frontLeft.getCurrentPosition());
                telemetry.addData("Front right encoder", frontRight.getCurrentPosition());
                telemetry.addData("Back left encoder", backLeft.getCurrentPosition());
                telemetry.addData("Back right encoder", backRight.getCurrentPosition());
                telemetry.update();
                idle(); // Give the rest of the FTC app time to run.
            }
        } finally {
            // Always stop the intake and wheels when this program ends.
            intake.setPower(0);
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
    }
}
