package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Spartans Auto", group = "Spartans")
@Disabled // Remove this line ONLY after checking the hardware and numbers below.
public class SpartansAuto extends LinearOpMode {

    // These are examples, NOT measurements of your actual robot.
    // Find your motor's encoder counts per OUTPUT-shaft revolution.
    private static final double COUNTS_PER_REV = 537.7;
    private static final double WHEEL_DIAMETER_INCHES = 96.0 / 25.4;
    private static final double GEAR_RATIO = 1.0; // Motor output turns per wheel turn.
    private static final double STRAFE_CORRECTION = 1.0; // Adjust after measuring side travel.

    private static final double COUNTS_PER_INCH =
            (COUNTS_PER_REV * GEAR_RATIO) / (Math.PI * WHEEL_DIAMETER_INCHES);

    private static final double DRIVE_POWER = 0.25;
    private static final double MOVE_TIMEOUT_SECONDS = 5.0;

    // Fields let the methods below use the same four motors.
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeft = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Keep these directions consistent with your tested TeleOp directions.
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setDrivePower(0);

        telemetry.addLine("Demo: forward 12 inches, then right 12 inches.");
        telemetry.addLine("Check calibration and clear the area before START.");
        telemetry.update();

        try {
            waitForStart();
            if (!opModeIsActive()) return;

            // Read these two lines as the robot's route.
            // Arguments: inches forward, inches right.
            if (!moveInches(12, 0)) return; // Quit if the first move fails.
            if (!moveInches(0, 12)) return;

            telemetry.addLine("Route finished.");
            telemetry.update();
        } finally {
            setDrivePower(0);
        }
    }

    // Returns true if every wheel reaches its target, false on STOP or timeout.
    private boolean moveInches(double forwardInches, double rightInches) {
        if (!opModeIsActive()) return false;

        // Encoders count motor rotation. Convert inches into encoder counts.
        int forwardCounts = (int) Math.round(forwardInches * COUNTS_PER_INCH);
        int strafeCounts = (int) Math.round(rightInches * COUNTS_PER_INCH * STRAFE_CORRECTION);

        setDrivePower(0);
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Start each move at zero.

        // For forward motion all targets match. Right strafe uses +, -, -, +.
        frontLeft.setTargetPosition(forwardCounts + strafeCounts);
        frontRight.setTargetPosition(forwardCounts - strafeCounts);
        backLeft.setTargetPosition(forwardCounts - strafeCounts);
        backRight.setTargetPosition(forwardCounts + strafeCounts);

        ElapsedTime timer = new ElapsedTime();
        try {
            setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
            // In this mode targets choose the direction; power is a positive limit.
            setDrivePower(DRIVE_POWER);
            timer.reset();

            // Keep waiting while ANY wheel still has a target to reach.
            while (opModeIsActive() && anyMotorBusy()
                    && timer.seconds() < MOVE_TIMEOUT_SECONDS) {
                telemetry.addData("Front left encoder", frontLeft.getCurrentPosition());
                telemetry.addData("Front right encoder", frontRight.getCurrentPosition());
                telemetry.addData("Back left encoder", backLeft.getCurrentPosition());
                telemetry.addData("Back right encoder", backRight.getCurrentPosition());
                telemetry.update();
                idle();
            }

            if (!opModeIsActive()) return false;
            if (anyMotorBusy()) {
                telemetry.addLine("Move timed out. Check for blocked wheels or encoder problems.");
                telemetry.update();
                return false;
            }
            return true;
        } finally {
            setDrivePower(0);
            setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    // || means OR: true if at least one motor is still moving toward its target.
    private boolean anyMotorBusy() {
        return frontLeft.isBusy() || frontRight.isBusy()
                || backLeft.isBusy() || backRight.isBusy();
    }

    // These two short methods save us from repeating the same four lines.
    private void setDrivePower(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    private void setDriveMode(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
    }
}
