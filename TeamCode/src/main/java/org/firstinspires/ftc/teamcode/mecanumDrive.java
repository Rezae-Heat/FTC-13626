package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorEx.*;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.util.ElapsedTime;



import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

// Author : Aadi and Kenul from Team 13626
// Password to the control hub is in the pre-showcase update section of the onenote
/* This software should be implemented on the 3/05/2023 and we should be able to figure out wht we need to adjust*/
@TeleOp()
public class mecanumDrive extends OpMode {
    DcMotorEx backL = null;
    DcMotorEx backR = null;
    DcMotorEx frontL = null;
    DcMotorEx frontR = null;

   // RevBlinkinLedDriver newLighting = null;
    private ElapsedTime runtime = new ElapsedTime();

    public void init() {
        // passing hardware setups to the motors
        backL = hardwareMap.get(DcMotorEx.class, "leftRear");
        backR = hardwareMap.get(DcMotorEx.class, "rightRear");
        backR.setDirection(DcMotorSimple.Direction.REVERSE);
        frontL = hardwareMap.get(DcMotorEx.class, "leftFront");
        frontR = hardwareMap.get(DcMotorEx.class, "rightFront");

        telemetry.addData("Status","Initialised");
        telemetry.update();
     //   newLighting = hardwareMap.get(RevBlinkinLedDriver.class,"lighting");
      //  newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_WHITE);
    }

    public void loop(){


        backL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        // the above 4 lines, set the motor to brake while no power by the controller
        // assign speed modifier

        double speedMod = 2;
        // half the full throttle, can be adjusted as necessary

        if (gamepad1.right_bumper) {
            // should speed it up
            speedMod = 1.2;
           // newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED);
        }
        else if (gamepad1.left_bumper) {
            // should slow it down
            speedMod = 3.5;
           // newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.TWINKLES_FOREST_PALETTE);
        }
        else {
           // newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_WHITE);
        }

// we need documentation so people trying to learn, can understand what is happening
        // Mecanum Drive
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.right_stick_x);
        double robotAngle = Math.atan2(-1*gamepad1.right_stick_x, gamepad1.left_stick_x) - Math.PI / 4; // idk why the -1 yet
        double rightX = gamepad1.left_stick_y;
        final double v1 = r * Math.cos(-robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(-robotAngle) - rightX;

        frontL.setPower(v3/speedMod);
        frontR.setPower(v2/speedMod);
        backL.setPower(v1/speedMod);
        backR.setPower(v4/speedMod);

        /*from https://ftcforum.firstinspires.org/forum/
        ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
         */
        int backLSpeed = (int) backL.getVelocity();
        int backRspeed = (int) backR.getVelocity();
        int frontLspeed= (int) frontL.getVelocity();
        int frontRspeed= (int) frontR.getVelocity();
        telemetry.addData("backLeft speed",backLSpeed);
        telemetry.addData("backRight speed",backRspeed);
        telemetry.addData("frontLeft speed",frontLspeed);
        telemetry.addData("frontRight speed",frontRspeed);



        double backLcurrent = backL.getCurrent(CurrentUnit.MILLIAMPS);
        double backRcurrent = backR.getCurrent(CurrentUnit.MILLIAMPS);
        double frontLcurrent = frontL.getCurrent(CurrentUnit.MILLIAMPS);
        double frontRcurrent = frontR.getCurrent(CurrentUnit.MILLIAMPS);
        telemetry.addData("backLeft current draw:",backLcurrent);
        telemetry.addData("backRight current draw:",backRcurrent);
        telemetry.addData("frontLeft current draw:",frontLcurrent);
        telemetry.addData("frontRight current draw:",frontRcurrent);
        // the above code should allow us to see the current draw of each motor
        telemetry.update();
        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }
}