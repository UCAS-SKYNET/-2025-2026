package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.lang.Math;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous

public class AutonomousModeLeft extends LinearOpMode {
    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    
    private DcMotor flywheel;
    private DcMotor funnel;
    private DcMotor cycler1;
    private DcMotor cycler2;
    
    private Servo servoLeft;
    private Servo servoRight;
    
    private ElapsedTime runtime = new ElapsedTime();
    
    public void driveInDirection(double direction, double power, double right_stick) {
        double x = power * Math.sin(direction);
        double y = power * Math.cos(direction);
        
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
    
    public void runOpMode() {
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        funnel = hardwareMap.get(DcMotor.class, "funnel");
        cycler1 = hardwareMap.get(DcMotor.class, "cycler1");
        cycler2 = hardwareMap.get(DcMotor.class, "cycler2");

        servoLeft = hardwareMap.get(Servo.class, "servoLeft");
        servoRight = hardwareMap.get(Servo.class, "servoRight");

        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        cycler1.setDirection(DcMotorSimple.Direction.REVERSE);
        cycler2.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        servoLeft.setPosition(0.8);
        servoRight.setPosition(.2);
        
        double timer;
        
        double forward = Math.PI/2; // These may need to be modified
        double right = Math.PI;
        double left = 0;
        double backward = Math.PI/-2;
        
        double delayedRun = runtime.seconds();
        
        while (opModeIsActive()) {
            
            double timeInSeconds = runtime.seconds() - delayedRun;
            if(timeInSeconds <= 1.6) {
                driveInDirection(forward, 1, 0);
                telemetry.addData("Phase", 1);

            } else if (timeInSeconds <= 3.2) {
                driveInDirection(0, 0, -1);
                telemetry.addData("Phase", 2);

            } else if (timeInSeconds <= 5.2) {
                driveInDirection(forward, 1, 0);
                telemetry.addData("Phase", 3);

            } else if (timeInSeconds <= 8.0) {
                driveInDirection(backward, 1, 0);
                telemetry.addData("Phase", 4);

            } else if (timeInSeconds <= 9.6) {
                driveInDirection(0, 0, 1);
                telemetry.addData("Phase", 5);

            } else if (timeInSeconds <= 12.0) {
                driveInDirection(forward, 1, 0);
                telemetry.addData("Phase", 6);

            } else if (timeInSeconds <= 12.8) {
                driveInDirection(0, 0, -1);
                telemetry.addData("Phase", 7);
            //pew pew function x3
            } else if (timeInSeconds <= 13.6) {
                driveInDirection(0, 0, 1);
                telemetry.addData("Phase", 8);

            } else if (timeInSeconds <= 17.6) {
                driveInDirection(backward, 1, 0);
                telemetry.addData("Phase", 9);

            } else if (timeInSeconds <= 20.0) {
                driveInDirection(left, 1, 0);
                telemetry.addData("Phase", 10);

            } else {
                driveInDirection(0, 0, 0);
                telemetry.addData("Phase", 11);
            }
            
            telemetry.addData("Time", runtime.seconds());
            
            telemetry.update();
        }
    
    }
    
    
    
    
    
    
    
    
    
    
    
}


