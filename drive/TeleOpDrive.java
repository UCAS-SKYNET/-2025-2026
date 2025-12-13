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
    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    //private DcMotor leftShoulder;
    //private DcMotor rightShoulder;
    //private DcMotor elbow;
    //private Servo elbow1;
    //private Servo elbow2;
    //private Servo claw;
    
    private IMU imu;
    
    
    
    
    
    public void driveInDirection(double direction, double power, double right_stick) {
        double x = power*Math.cos(direction);
        double y = power*Math.sin(direction);
        
        double fl = y + x + right_stick;
        double fr = y - x - right_stick;
        double bl = y - x + right_stick;
        double br = y + x - right_stick;

        double max = Math.max(1.0,
            Math.max(Math.abs(fl),
            Math.max(Math.abs(fr),
            Math.max(Math.abs(bl), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        frontRight.setPower(fr / max);
        backLeft.setPower(bl / max);
        backRight.setPower(br / max);
    }
    
    //public void shoulderRotation(double amount) {
        //leftShoulder.setPower(amount);
        //rightShoulder.setPower(-amount);
   // }
    
    //public void elbowRotation(double direction) {
        
        //if (direction == 1){
            //elbow.setPower(0.8);
        //} else if (direction == -1) {
            //elbow.setPower(-0.1);
        //} else {
            //elbow.setPower(0.25);
            //elbow.setPower(0);
        //}
        
        // Correction if needed
        /*if (Math.abs(elbow1.getPosition() + elbow2.getPosition() - 0.5) > 0.03) {
            telemetry.addData("ouch", "ies");
            elbow1.setPosition(0.5 - elbow2.getPosition());
        }*/
    //}

    @Override
    public void runOpMode() {
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        // Reverse left side motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Right side stays forward
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        //leftShoulder = hardwareMap.get(DcMotor.class, "left shoulder");
        //rightShoulder = hardwareMap.get(DcMotor.class, "right shoulder");
        //elbow = hardwareMap.get(DcMotor.class, "elbow");
        //elbow1 = hardwareMap.get(Servo.class, "elbow1");
        //elbow2 = hardwareMap.get(Servo.class, "elbow2");
        //claw = hardwareMap.get(Servo.class, "claw");


        
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
        
        double jiggler = 0; // THIS WAS ETHAN'S IDEA

        // run until the end of the match (driver presses STOP)
        
        
        while (opModeIsActive()) {
            robotOrientation = imu.getRobotYawPitchRollAngles();
            x = (double)this.gamepad1.left_stick_x;
            y = (double)-this.gamepad1.left_stick_y;
            
            direction = Math.atan2(y, x);
            Yaw = robotOrientation.getYaw(AngleUnit.RADIANS);
            
            Yaw = -Yaw; // These next few lines are all for translating the yaw into the same format as the joystick
            
            /*
            Yaw -= Math.PI/2;
            if (Yaw < -Math.PI) {
                Yaw += 2*Math.PI;
            
            } else if (Yaw > Math.PI) {
                Yaw -= 2*Math.PI; 
            }
            */
            relativeDirection = direction-Yaw;
            if (relativeDirection < -Math.PI) {
                relativeDirection += 2*Math.PI;
            } else if (relativeDirection > Math.PI) {
                relativeDirection -= 2*Math.PI;
            }
            
            
    
    
            if(this.gamepad1.left_trigger > 0.3) {
                power = Math.min(1.0, Math.sqrt(x*x + y*y) * 1.5); // this is if we need to go fast
            } else {
                power = Math.sqrt(x*x+y*y) * 0.5; // this is without the button, so it moves a little slower for precision
            }
            
            if(this.gamepad1.start && this.gamepad1.dpad_up) {
                imu.resetYaw();
            }
            
            telemetry.addData("Joystick Direction", String.valueOf(direction));
            //telemetry.addData("Yaw", String.valueOf(Yaw));
            //telemetry.addData("Relative Direction", String.valueOf(relativeDirection));
            telemetry.addData("Drive Power", String.valueOf(power));
            
            
            
            if(this.gamepad1.left_trigger > 0.3) {
                double turn = this.gamepad1.right_stick_x * 0.6;
                driveInDirection(relativeDirection, power, turn);
            } else {
                driveInDirection(relativeDirection, power, (double)this.gamepad1.right_stick_x);
            }
            
            
            
            
            
            //if (Math.abs(this.gamepad2.left_stick_y) < 0.5) {
                //if (this.gamepad2.left_trigger > 0.4) {
                    //elbowRotation(-1);
                //} else if (this.gamepad2.left_bumper) {
                    //elbowRotation(1);
                //} else {
                    //elbowRotation(0);
                //}
                
                //if (this.gamepad2.right_trigger > 0.3) {
                    //shoulderRotation(0.3);
                //} else if (this.gamepad2.right_bumper) {
                    //shoulderRotation(-0.5);
               // } else {
                    /*
                    if (jiggler >= 3) {
                        shoulderRotation(-0.2);
                    } else if (jiggler >= 6) {
                        jiggler = 1;
                        shoulderRotation(0.2);
                    } else {
                        shoulderRotation(0.2);
                    }*/
                    //shoulderRotation(-0.1);
                //}
            //jiggler++;
                
            //} else {
                //if (this.gamepad2.left_stick_y > 0) {
                    //shoulderRotation(0.5);
                    //elbowRotation(1);
                //} else {
                   // elbowRotation(-1);
                    //shoulderRotation(-0.5);
                //}
            //}
            
            //if (this.gamepad2.dpad_down) {
               // elbow.setPower(-1.2);
           // }
            
            // Set claw position
            //if (this.gamepad2.a) {
               // claw.setPosition(0.62);
            //} else if (this.gamepad2.b) {
                //claw.setPosition(0.12);
            //}
            
            
            
            
            //telemetry.addData("Elbow", (double)elbow1.getPosition());
            //telemetry.addData("Left stick", (double)this.gamepad2.left_stick_y);
            
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
        
    }

    
}





