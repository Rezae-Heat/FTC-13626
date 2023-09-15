    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorEx;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.util.ElapsedTime;
    import com.vuforia.CameraDevice;
    import com.vuforia.CameraDevice.*;


    import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

    // Author : Aadi and Kenul from Team 13626
    // Password to the control hub is in the pre-showcase update section of the onenote
    /* This software should be implemented on the 3/05/2023 and we should be able to figure out wht we need to adjust*/
    // The viper slide code is rudimentary and will need to updated.
    @TeleOp()
    public class teleOP1VPR extends OpMode {
        DcMotorEx backL = null;
        CameraDevice cam = null;
        DcMotorEx backR = null;
        DcMotorEx frontL = null;
        DcMotorEx frontR = null;
        DcMotorEx slide1 = null; // viper slides can easily be swapped, if part is broken
                                // in under 30 mins with spare viper slide
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
                    frontR.setCurrentAlert(3.50, CurrentUnit.AMPS);
            // slide stuff below
            slide1 = hardwareMap.get(DcMotorEx.class,"slide1");
            claw  = hardwareMap.get(Servo.class,"claw");
            telemetry.addData("init position",slide1.getCurrentPosition());
            slide1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addData("Status","Initialised");
            telemetry.update();
            //   newLighting = hardwareMap.get(RevBlinkinLedDriver.class,"lighting");
            //  newLighting.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_WHITE);
            slide1.setTargetPosition(0);

            claw.setDirection(Servo.Direction.FORWARD);

        }

            // adjusted for 1 driver to operate the entire robot on gamepad 1
        public void loop(){

            int currentPos = slide1.getCurrentPosition();
            telemetry.addData("Current Slide 1 pos", currentPos);
            int targetPositionlow = 700;
            int targetPositionmid = 1450;
            int targetPositionhigh = 2800;
            int targetPositionstart = 0;
            double currentPOs= claw.getPosition();

            telemetry.addData("currentPOS",currentPOs);
            telemetry.update();
            if (gamepad2.b){
                claw.setPosition(0.25); // should be just enough
            } // the position values will need to be adjusted for this claw
             if (gamepad2.a){
                 claw.setPosition(0.0);


            }
            if(gamepad2.dpad_up){
                slide1.setPower(1);// check slides before you run code
                slide1.setTargetPosition(targetPositionmid);
            }
            if(gamepad2.dpad_left){
                slide1.setPower(1);
                slide1.setTargetPosition(targetPositionlow);
            }
            if(gamepad2.dpad_right){
                slide1.setPower(1);
                slide1.setTargetPosition(targetPositionhigh);
            }
            if(gamepad2.dpad_down){
                slide1.setPower(0.6);
                slide1.setTargetPosition(targetPositionstart);
            }
            slide1.setPower(gamepad2.right_trigger);
            slide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData("targetpos",slide1.getTargetPosition());

            backL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            backR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frontR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            // the above 4 lines, set the motor to brake while no power by the controller
            // assign speed modifier

            float speedMod = gamepad1.left_trigger;
            // half the full throttle, can be adjusted as necessary



    // we need documentation so people trying to learn, can understand what is happening
            // Mecanum Drive
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.right_stick_x);
            double robotAngle = Math.atan2(-1*gamepad1.right_stick_x, gamepad1.left_stick_x) - Math.PI / 4; // idk why the -1 yet
            double rightX = gamepad1.left_stick_y;
            final double v1 = r * Math.cos(-robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(-robotAngle) - rightX;

            frontL.setPower(v3*speedMod);
            frontR.setPower(v2*speedMod);
            backL.setPower(v1*speedMod);
            backR.setPower(v4*speedMod);

            /*from https://ftcforum.firstinspires.org/forum/
            ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
             */
            int backLSpeed = (int) backL.getVelocity();
            int backRspeed = (int) backR.getVelocity();
            int frontLspeed= (int) frontL.getVelocity();
            int frontRspeed= (int) frontR.getVelocity();

            double backLcurrent = backL.getCurrent(CurrentUnit.AMPS);
            double backRcurrent = backR.getCurrent(CurrentUnit.AMPS);
            double frontLcurrent = frontL.getCurrent(CurrentUnit.AMPS);
            double frontRcurrent = frontR.getCurrent(CurrentUnit.AMPS);
            double slideCurrent= slide1.getCurrent(CurrentUnit.AMPS);
            double totalDTcurrent = backLcurrent+backRcurrent+frontLcurrent+frontRcurrent;
            telemetry.addData("backLeft current draw:",backLcurrent); /// mid pos needs to be lower
            telemetry.addData("backRight current draw:",backRcurrent);
            telemetry.addData("frontLeft current draw:",frontLcurrent);
            telemetry.addData("frontRight current draw:",frontRcurrent);
            telemetry.addData("slide current draw", slideCurrent);
            telemetry.addData("total current draw drivetrain",totalDTcurrent);

            // the above code should allow us to see the current draw of each motor
            telemetry.update();
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            if (slideCurrent > 5){
                slide1.setMotorDisable();
            }
        }

        @Override
        public void stop() {
            super.stop();
            frontR.setMotorDisable();
            frontL.setMotorDisable();
            backL.setMotorDisable();
            backR.setMotorDisable();
            slide1.setMotorDisable();
        }
    }