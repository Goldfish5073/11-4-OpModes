package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Jachzach on 10/21/2015.
 */
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

public class CRServoTest extends Hardware {


    FtcConfig ftcConfig=new FtcConfig();

    public CRServoTest() {}
    private Servo v_servo;
    private TouchSensor v_touch;

    @Override public void start () {
        ftcConfig.init(hardwareMap.appContext, this);

        try {
            v_servo = hardwareMap.servo.get ("servo");
            v_servo.setPosition(0.5);
        } catch (Exception p_exception) {
            m_warning_message ("servo");
            DbgLog.msg (p_exception.getLocalizedMessage ());
            v_servo = null;
        }

        try {
            v_touch = hardwareMap.touchSensor.get("touch");
        } catch (Exception p_exception) {
            m_warning_message ("touch");
            DbgLog.msg (p_exception.getLocalizedMessage ());
            v_servo = null;
        }
    }

    @Override public void loop ()

    {
        if(v_touch.isPressed()) {
            v_servo.setPosition(0.5);
        } else {
            if (gamepad1.a) {
                if (!(v_servo == null)) {
                    v_servo.setPosition(1.0);
                }
            } else if (gamepad1.b) {
                if (!(v_servo == null)) {
                    v_servo.setPosition(0.0);
                }
            } else {
                if (!(v_servo == null)) {
                    v_servo.setPosition(0.5);
                }
            }
        }

        telemetry.addData("Servo", v_servo.getPosition());
        update_gamepad_telemetry();

    } // loop
} // Autonomous
