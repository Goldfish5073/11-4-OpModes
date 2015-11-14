package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Jachzach on 10/21/2015.
 */
import com.qualcomm.robotcore.util.Range;

public class TeleOp extends Hardware {

    int driveScale = 0;
    double[] driveSpeeds = {0.6, 0.8, 1.0};
    double armSpeed = 0.5;
    double armSpeedBack = 0.25;
    double winchSpeed = 1.0;
    double clawSpeed = 1.0;

    /////////////////////////////////////////////////////////////////////////////////////
    //CONTROLS
    /*

    GAMEPAD 1
    left joystick = left drive
    right joystick = right drive
    right trigger = up speed
    right bumper = down speed
    left trigger = claws
    left bumper = claws back
    a = winch
    b = winch back

    GAMEPAD 2
    left joystick = first arm
    right joystick = second arm
    right trigger = hook
    right bumper = hook in
    x = tab slapper
    y = tab slapper back
    start = button pusher
    back = button pusher back
    left trigger = climber dropper
    left bumper = climber dropper in

    */


    public TeleOp()
    {
    }

    @Override public void loop ()

    {
        //////////////////////////////////////////////////////////////////////////////
        //DRIVE
        //DRIVE SCALE
        if (gamepad1.right_trigger > 0.1 && driveScale <= 2) {
            driveScale++;
        } else if (gamepad1.right_bumper && driveScale >= 0) {
            driveScale--;
        }

        //LEFT
        double speed = driveSpeeds[driveScale];

        if (Range.clip(-gamepad1.left_stick_y, -1, 1) > 0.1) {
            set_left_power(speed);
        } else if (Range.clip(-gamepad1.left_stick_y, -1, 1) < 0.1) {
            set_left_power(-speed);
        } else {
            set_left_power(0.0);
        }
        //RIGHT
        if(Range.clip(-gamepad1.right_stick_y, -1, 1) > 0.1) {
            set_right_power(speed);
        } else if (Range.clip(-gamepad1.right_stick_y, -1, 1) < 0.1) {
            set_right_power(-speed);
        } else {
            set_right_power(0.0);
        }


        //////////////////////////////////////////////////////////////////////////////
        //ARM
        //FIRST
        if (Range.clip(gamepad2.left_stick_y, -1, 1) > 0.1) {
            set_first_arm_power(armSpeed);
        } else if (Range.clip(gamepad2.left_stick_y, -1, 1) < 0.1) {
            set_first_arm_power(-armSpeedBack);
        } else {
            set_first_arm_power (0.0);
        }
        //SECOND
        if(Range.clip(-gamepad2.right_stick_y, -1, 1) > 0.1) {
            set_second_arm_power(armSpeed);
        } else if (Range.clip(-gamepad2.right_stick_y, -1, 1) < 0.1) {
            set_second_arm_power(-armSpeedBack);
        } else {
            set_second_arm_power(0.0);
        }


        //////////////////////////////////////////////////////////////////////////////
        //CLAW
        if (gamepad1.left_bumper) {
            set_claw_power(clawSpeed);
        }else if (gamepad1.left_trigger > 0.05) {
            set_claw_power(-clawSpeed);
        } else {
            set_claw_power(0.0);
        }


        //////////////////////////////////////////////////////////////////////////////
        //WINCH
        if (gamepad1.a) {
            set_winch_power(winchSpeed);
        }else if (gamepad1.b) {
            set_winch_power(-winchSpeed);
        } else {
            set_winch_power(0.0);
        }


        //////////////////////////////////////////////////////////////////////////////
        //HOOK
        //TODO: make this less sensitive? use a current and a past state, have equal
        if (gamepad2.right_bumper){
            hookBack();
        } else if (gamepad2.right_trigger > 0.1){
            hook();
        }


        //////////////////////////////////////////////////////////////////////////////
        // TAB SLAPPER
        if (gamepad2.x){
            tab_slapper();
        }else if (gamepad2.y){
            tab_slapper_back();
        }


        //////////////////////////////////////////////////////////////////////////////
        // PUSH BEACON
        if (gamepad2.start) {
            push_beacon(true);
        } else if (gamepad2.back) {
            push_beacon(false);
        }


        /////////////////////////////////////////////////////////////////////////////
        //CLIMBER DROPPER
        if(gamepad2.left_trigger > 0.1) {
            climber_dropper();
        } else if (gamepad2.left_bumper) {
            climber_dropper_back();
        }


        //////////////////////////////////////////////////////////////////////////////
        // TELEMETRY
        update_telemetry ();
        //left drive, right drive, first arm, second arm, winch, claw, tab slapper
        update_gamepad_telemetry ();
        /*
        telemetry.addData
                ( "13"
                        , "Push Beacon Position: " + v_servo_push_beacon.getPosition();
                );*/

    } // loop
} // Autonomous
