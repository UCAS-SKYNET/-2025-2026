/*
Copyright 2024 FIRST Tech Challenge Team 21881

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

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

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this OpMode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */

@TeleOp
public class TeleOpDrive extends LinearOpMode {
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    private DcMotor flywheel;
    private DcMotor funnelLeft;
    private DcMotor funnelRight;
    private DcMotor cycler;
    
    private IMU imu;

    public void driveInDirection(double direction, double power, double right_stick) {
        double x = power*Math.cos(direction);
        double y = power*Math.sin(direction);
        
        double fl = y + x - right_stick;
        double fr = y - x + right_stick;
        double bl = y - x - right_stick;
        double br = y + x + right_stick;

        double max = Math.max(1.0,
            Math.max(Math.abs(fl),
            Math.max(Math.abs(fr),
            Math.max(Math.abs(bl), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        frontRight.setPower(fr / max);
        backLeft.setPower(bl / max);
        backRight.setPower(br / max);
    }

    public void cycleBall() {
        ;
    }

    @Override
    public void runOpMode() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        funnelLeft = hardwareMap.get(DcMotor.class, "funnelLeft");
        funnelRight = hardwareMap.get(DcMotor.class, "funnelRight");
        cycler = hardwareMap.get(DcMotor.class, "cycler");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        funnelLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        funnelLeft.setPower(1);
        funnelRight.setPower(1);

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
        
        double x;
        double y;
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
            
            double rawX = -gamepad1.left_stick_x;
            double rawY = gamepad1.left_stick_y;

            if (Math.hypot(rawX, rawY) < 0.05) {
                driveInDirection(0, 0, turn);
                continue;
            }

            x = rawX;
            y = rawY;

            direction = Math.atan2(x, y);
            Yaw = robotOrientation.getYaw(AngleUnit.RADIANS);
            relativeDirection = direction - Yaw;

            if (relativeDirection < -Math.PI) {
                relativeDirection += 2*Math.PI;
            } else if (relativeDirection > Math.PI) {
                relativeDirection -= 2*Math.PI;
            }
                
            if(this.gamepad1.left_trigger > 0.3) {
                power = Math.min(1.0, Math.hypot(x,y) * 1.5); // this is if we need to go fast
            } else {
                power = Math.min(1.0, Math.hypot(x,y) * 0.5); // this is without the button, so it moves a little slower for precision
            }
            
            if (gamepad1.start && !lastStart) {
                imu.resetYaw();
            }
            lastStart = gamepad1.start;
            
            telemetry.addData("Joystick Direction", String.valueOf(direction));
            telemetry.addData("Yaw", String.valueOf(Yaw));
            telemetry.addData("Relative Direction", String.valueOf(relativeDirection));
            telemetry.addData("Drive Power", String.valueOf(power));

            driveInDirection(relativeDirection, power, turn);            
            
            telemetry.update();
        }
    }
}





