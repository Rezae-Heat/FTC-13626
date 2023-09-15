package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.opencv.*;

public class motorTesting extends OpMode {

    DcMotorEx test1 = null;

    public void init (){
        test1 = hardwareMap.get(DcMotorEx.class,"test");
    }

    public void loop(){
        test1.setPower(gamepad1.left_stick_y);
        test1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }



}
