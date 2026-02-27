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


@TeleOp


public class AutonomousModeLeft_Copy extends LinearOpMode {
    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private Servo servoRight;
    private Servo servoLeft;
    //private DcMotor leftShoulder;
    //private DcMotor rightShoulder;
    //private DcMotor elbow;
   
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
        servoRight = hardwareMap.get(Servo.class, "servoRight");
        servoLeft = hardwareMap.get(Servo.class, "servoLeft");
        //leftShoulder = hardwareMap.get(DcMotor.class, "left shoulder");
        //rightShoulder = hardwareMap.get(DcMotor.class, "right shoulder");
        //elbow = hardwareMap.get(DcMotor.class, "elbow");
       
        waitForStart();
       
        double timer;
       
        double forward = Math.PI/2;
        double right = Math.PI;
        double left = 0;
        double backward = Math.PI/-2;
       
        double delayedRun = runtime.seconds();
       
        while (opModeIsActive()) {


            if (gamepad1.a) {
                servoLeft.setPosition(0.0);
                servoRight.setPosition(.2);
            }
           
            double timeInSeconds = runtime.seconds() - delayedRun;
           
            if(timeInSeconds <= 0.5) {
                driveInDirection(forward, 0.2, 0);
                telemetry.addData("Phase", 1);
            } else if (timeInSeconds <= 2) {
                driveInDirection(left, 0.6, 0);
                telemetry.addData("Phase", 2);
            } else if (timeInSeconds <= 3) {
                driveInDirection(backward, 0.2, 0);
                telemetry.addData("Phase", 3);
            } else {
                driveInDirection(0, 0, 0);
                telemetry.addData("Phase", 4);
            }
           
            telemetry.addData("Time", runtime.seconds());
           
            telemetry.update();
        }
   
    }
   
   
   
   
   
   
   
   
   
   
   
}