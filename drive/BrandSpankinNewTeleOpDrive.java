import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;23

public class BrandSpankinNewTeleOpDrive extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight;

    public void driveInDirection(double x, double y, double rx){
        double fl = y + x + rx;
        double fr = y - x - rx;
        double bl = y - x + rx;
        double br = y + x - rx;

        // Normalize so no value exceeds 1
        double max = Math.max(Math.abs(fl), Math.max(Math.abs(fr),
                    Math.max(Math.abs(bl), Math.abs(br))));
        if (max > 1.0) {
            fl /= max;
            fr /= max;
            bl /= max;
            br /= max;
        }

        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);
    }

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            double yl1 = Math.abs(gamepad1.left_stick_y) > 0.05 ? -gamepad1.left_stick_y : 0; // forward/back
            double xl1 = Math.abs(gamepad1.left_stick_x) > 0.05 ? gamepad1.left_stick_x : 0; // strafe
            double xr1 = Math.abs(gamepad1.right_stick_x) > 0.05 ? gamepad1.right_stick_x : 0; // rotation

            driveInDirection(xl1, yl1, xr1);

            telemetry.addData("Joystick X", xl1);
            telemetry.addData("Joystick Y", yl1);
            telemetry.addData("Joystick Rotation", xr1);

            telemetry.addData("Front Left Power", frontLeft.getPower());
            
            telemetry.update();
        }
    }
}