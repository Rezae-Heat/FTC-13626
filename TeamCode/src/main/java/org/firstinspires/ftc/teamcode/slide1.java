    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.hardware.DcMotorEx.*;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.HardwareDevice;
    import com.qualcomm.robotcore.hardware.VoltageSensor;
    import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
    import com.qualcomm.robotcore.util.ElapsedTime;



    import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

    // Author : Aadi from Team 13626
    // Password to the control hub is in the pre-showcase update section of the onenote
    /* This software should be implemented on the 3/05/2023 and we should be able to figure out wht we need to adjust*/
    // The viper slide code is rudimentary and will need to updated. Just letting you know Kenul and
    @TeleOp()
    public class slide1 extends OpMode {
        DcMotorEx backL = null; 
        DcMotorEx backR = null;
        DcMotorEx frontL = null;
        DcMotorEx frontR = null;

        DcMotorEx slide1 = null;

        Servo claw;




        // RevBlinkinLedDriver newLighting = null;
        private ElapsedTime runtime = new ElapsedTime();

        public void init() {
            // passing hardware setups to the motors
            backL = hardwareMap.get(DcMotorEx.class, "leftRear");
            backR = hardwareMap.get(DcMotorEx.class, "rightRear");
            backR.setDirection(DcMotorSimple.Direction.REVERSE);
            frontL = hardwareMap.get(DcMotorEx.class, "leftFront");
            frontR = hardwareMap.get(DcMotorEx.class, "rightFront");

            slide1 = hardwareMap.get(DcMotorEx.class,"slide1");
            claw  = hardwareMap.get(Servo.class,"claw");
            telemetry.addData("init position",slide1.getCurrentPosition());
            slide1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addData("Status","Initialised");
            telemetry.update();
            //   newLighting = hardwareMap.get(RevBlinkinLedDriver.class,"lighting");
            //  newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_WHITE);
            slide1.setTargetPosition(0);
            claw.setDirection(Servo.Direction.REVERSE);
            double currentclawPOS = claw.getPosition();
            claw.setPosition(0);


        }


        public void loop(){

            int currentPos = slide1.getCurrentPosition();
            telemetry.addData("Current Slide 1 pos", currentPos);
            int targetPositionlow = 1000;
            int targetPositionmid = 1750;
            int targetPositionhigh = 3100;
            int targetPositionstart = 0;
            if (gamepad2.b){
                claw.setPosition(0.2);
            } // don't know if this code works
            if (gamepad2.a){
                claw.setPosition(0);
            }
            if(gamepad2.dpad_up){
                slide1.setTargetPosition(-targetPositionlow);
            }
            if(gamepad2.dpad_left){
                slide1.setTargetPosition(-targetPositionhigh);
            }
            if(gamepad2.dpad_right){
                slide1.setTargetPosition(-targetPositionmid);
            }
            if(gamepad2.dpad_down){
                slide1.setTargetPosition(targetPositionstart);
            }
            slide1.setPower(1);
            slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData("targetpos",slide1.getTargetPosition());

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
                speedMod = 1.1; // 90% speed
                // newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED);
            }
            else if (gamepad1.left_bumper) {
                // should slow it down
                speedMod = 3.5; // 29% speed
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



            double backLcurrent = backL.getCurrent(CurrentUnit.AMPS);
            double backRcurrent = backR.getCurrent(CurrentUnit.AMPS);
            double frontLcurrent = frontL.getCurrent(CurrentUnit.AMPS);
            double frontRcurrent = frontR.getCurrent(CurrentUnit.AMPS);
            double totalDTcurrent = backLcurrent+backRcurrent+frontLcurrent+frontRcurrent;
            telemetry.addData("backLeft current draw:",backLcurrent);
            telemetry.addData("backRight current draw:",backRcurrent);
            telemetry.addData("frontLeft current draw:",frontLcurrent);
            telemetry.addData("frontRight current draw:",frontRcurrent);
            telemetry.addData("total current draw drivetrain",totalDTcurrent);

            // the above code should allow us to see the current draw of each motor
            telemetry.update();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
        }
    }