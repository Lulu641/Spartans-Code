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
        // Configure your actual intake motor with this exact name.
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setPower(0);
        // Change FORWARD to REVERSE if collecting spins the wrong way.
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

                // Positive power collects; negative power runs the motor backward.
                if (collect && !eject) {
                    intakePower = gamepad1.right_trigger * INTAKE_MAX_POWER;
                } else if (eject && !collect) {
                    intakePower = -gamepad1.left_trigger * INTAKE_MAX_POWER;
                }

                intake.setPower(intakePower);
                telemetry.addData("Intake power", intakePower);
                telemetry.update();
                idle();
            }
        } finally {
            intake.setPower(0); // Turn off the motor when you press STOP or the code exits.
        }
    }
}
