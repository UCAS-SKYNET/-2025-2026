
package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.lang.Math;

@TeleOp
public class TeleOpDrive_copy extends LinearOpMode {
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    private DcMotor flywheel;
    private DcMotor funnel;
    private DcMotor cycler1;
    private DcMotor cycler2;
    
    private IMU imu;

    public void driveInDirection(double direction, double power, double right_stick) {
        
        
    }

    public void cycleBall(double power) {
        cycler1.setPower(power);
        cycler2.setPower(power);
    }
    
    public void spinFlywheel(double power) {
        flywheel.setPower(power);
    }

    @Override
    public void runOpMode() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        funnel = hardwareMap.get(DcMotor.class, "funnel");
        cycler1 = hardwareMap.get(DcMotor.class, "cycler1");
        cycler2 = hardwareMap.get(DcMotor.class, "cycler2");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        cycler1.setDirection(DcMotorSimple.Direction.REVERSE);
        cycler2.setDirection(DcMotorSimple.Direction.REVERSE);
        

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters params;

        params = new IMU.Parameters(
             new RevHubOrientationOnRobot(
                  RevHubOrientationOnRobot.LogoFacingDirection.UP,
                  RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
             )
        );
        
        imu.initialize(params);
        
        YawPitchRollAngles robotOrientation;
        double Yaw;
        double relativeDirection;
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        funnel.setPower(1);

        double direction;
        double power;

        boolean lastStart = false;

        telemetry.addData("Status", "Running");
        // run until the end of the match (driver presses STOP)
        
        while (opModeIsActive()) {
            robotOrientation = imu.getRobotYawPitchRollAngles();

            double turn = Math.abs(gamepad1.right_stick_x) > 0.05
                ? gamepad1.right_stick_x * 0.6
                : 0;
            
            double xl1 = -gamepad1.left_stick_x;
            double yl1 = gamepad1.left_stick_y;
            
            if (Math.hypot(xl1, yl1) < 0.05) {
                driveInDirection(0, 0, turn);
            } else {
                direction = Math.atan2(yl1, xl1);
                Yaw = robotOrientation.getYaw(AngleUnit.RADIANS);
                relativeDirection = direction - Yaw;

                if (relativeDirection < -Math.PI) {
                    relativeDirection += 2*Math.PI;
                } else if (relativeDirection > Math.PI) {
                    relativeDirection -= 2*Math.PI;
                }
                    
                power = Math.min(1.0, Math.hypot(xl1,yl1));
                
                telemetry.addData("Joystick Direction", String.valueOf(direction));
                telemetry.addData("Yaw", String.valueOf(Yaw));
                telemetry.addData("Relative Direction", String.valueOf(relativeDirection));
                telemetry.addData("Drive Power", String.valueOf(power));
                
                //driveInDirection(relativeDirection, power, turn);
            }

            double yl2 = gamepad2.left_stick_y;
            double yr2 = gamepad2.right_stick_y;

            if (Math.abs(yr2) > 0.05) {
                cycleBall(yr2);
            } else {
                cycleBall(0);
            }

            if (Math.abs(yl2) > 0.05) {
                spinFlywheel(yl2);
            } else {
                spinFlywheel(0);
            }            

            if (gamepad1.start && !lastStart) {
                imu.resetYaw();
            }
            lastStart = gamepad1.start;

            telemetry.update();
        }
    }
}