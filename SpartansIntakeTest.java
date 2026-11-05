package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// Run this first to check the intake without commanding the drive motors.
@TeleOp(name = "Spartans Intake Test", group = "Spartans")
public class SpartansIntakeTest extends LinearOpMode {

    // 0.60 means a maximum of 60% power. Start with small trigger presses.
    private static final double INTAKE_MAX_POWER = 0.60;

    @Override
    public void runOpMode() throws InterruptedException {
        // Two hex intake motors. These names must match your Hub configuration.
        DcMotor intakeLeft = hardwareMap.get(DcMotor.class, "intake_left");
        DcMotor intakeRight = hardwareMap.get(DcMotor.class, "intake_right");
        intakeLeft.setPower(0);
        intakeRight.setPower(0);

        // Starting guess for mirrored motors. Check EACH roller pulls inward.
        // If one roller pushes outward, flip only that motor's direction here.
        // Keep these directions the same in the test and driving programs.
        intakeLeft.setDirection(DcMotor.Direction.FORWARD);
        intakeRight.setDirection(DcMotor.Direction.REVERSE);
        intakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("GAMEPAD 1: right trigger = collect, left trigger = eject.");
        telemetry.addLine("Release both triggers to stop. Both together also stop.");
        telemetry.update();

        try {
            waitForStart();

            while (opModeIsActive()) {
                // Ignore tiny trigger readings near zero.
                boolean collect = gamepad1.right_trigger > 0.05;
                boolean eject = gamepad1.left_trigger > 0.05;
                double intakePower = 0;

                // Positive power collects; negative power reverses both rollers.
                if (collect && !eject) {
                    intakePower = gamepad1.right_trigger * INTAKE_MAX_POWER;
                } else if (eject && !collect) {
                    intakePower = -gamepad1.left_trigger * INTAKE_MAX_POWER;
                }

                // Both motors receive the same command. Their direction settings
                // above account for the way each motor is physically mounted.
                intakeLeft.setPower(intakePower);
                intakeRight.setPower(intakePower);
                telemetry.addData("Intake power", intakePower);
                telemetry.update();
                idle();
            }
        } finally {
            // Turn off BOTH intake motors when the program ends.
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
        }
    }
}
