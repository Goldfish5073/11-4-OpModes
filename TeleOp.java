package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Jachzach on 10/21/2015.
 */
import com.qualcomm.robotcore.util.Range;

public class TeleOp extends Hardware {

    /////////////////////////////////////////////////////////////////////////////////////
    //SPEED VARIABLES
    double[] driveSpeeds = {0.3, 0.6, 0.8, 1.0};
    double firstArmSpeed = 0.3; //0.3 top
    double firstArmSpeedBack = 0.3; //0.2
    double secondArmSpeed = 0.4;//0.5
    double secondArmSpeedBack = 0.2;//0.25 bottom
    double winchSpeed = 1.0;
    double clawSpeed = 1.0;

    int driveScale = driveSpeeds.length - 1;
    //firstArm is foremarm (top), secondArm is bicep (bottom)


    FtcConfig ftcConfig=new FtcConfig();

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
    right trigger = hook out
    right bumper = hook in
    x = tab slapper in
    y = tab slapper out
    start = button pusher
    back = button pusher back
    left trigger = climber dropper out
    left bumper = climber dropper in

    //TODO - make sure that all triggers are coded for GREATER THAN 0.5 - weird start error if not connected

    */


    public TeleOp()
    {
    }

    @Override public void start () {
        ftcConfig.init(hardwareMap.appContext, this);
    }

    @Override public void loop ()

    {
        //////////////////////////////////////////////////////////////////////////////
        //DRIVE
        //DRIVE SCALE
        if (gamepad1.right_trigger > 0.1 && driveScale < driveSpeeds.length - 1) {
            driveScale++;
        } else if (gamepad1.right_bumper && driveScale > 0) {
            driveScale--;
        }

        //LEFT
        double speed = driveSpeeds[driveScale];
        //^ negative because robot goes backwards compared to original testing bot

        if (Range.clip(-gamepad1.left_stick_y, -1, 1) > 0.1) {
            set_left_power(speed);
        } else if (Range.clip(-gamepad1.left_stick_y, -1, 1) < -0.1) {
            set_left_power(-speed);
        } else {
            set_left_power(0.0);
        }
        //RIGHT
        if(Range.clip(-gamepad1.right_stick_y, -1, 1) > 0.1) {
            set_right_power(speed);
        } else if (Range.clip(-gamepad1.right_stick_y, -1, 1) < -0.1) {
            set_right_power(-speed);
        } else {
            set_right_power(0.0);
        }


        //////////////////////////////////////////////////////////////////////////////
        //ARM
        //FIRST
        //SWITCH LEFT AND RIGHT FOR JIM -- and start controller as start x?
        if(ftcConfig.param.secondDriverIsZach) {
            if (Range.clip(gamepad2.left_stick_y, -1, 1) > 0.1) {
                set_first_arm_power(firstArmSpeed);
            } else if (Range.clip(gamepad2.left_stick_y, -1, 1) < -0.1) {
                set_first_arm_power(-firstArmSpeedBack);
            } else {
                set_first_arm_power(0.0);
            }
            //SECOND
            if (Range.clip(-gamepad2.right_stick_y, -1, 1) > 0.1) {
                set_second_arm_power(secondArmSpeed);
            } else if (Range.clip(-gamepad2.right_stick_y, -1, 1) < -0.1) {
                set_second_arm_power(-secondArmSpeedBack);
            } else {
                set_second_arm_power(0.0);
            }
        } else {
            if (Range.clip(gamepad2.right_stick_y, -1, 1) > 0.1) {
                set_first_arm_power(firstArmSpeed);
            } else if (Range.clip(gamepad2.right_stick_y, -1, 1) < -0.1) {
                set_first_arm_power(-firstArmSpeedBack);
            } else {
                set_first_arm_power(0.0);
            }
            //SECOND
            if (Range.clip(-gamepad2.left_stick_y, -1, 1) > 0.1) {
                set_second_arm_power(secondArmSpeed);
            } else if (Range.clip(-gamepad2.left_stick_y, -1, 1) < -0.1) {
                set_second_arm_power(-secondArmSpeedBack);
            } else {
                set_second_arm_power(0.0);
            }
        }


        //////////////////////////////////////////////////////////////////////////////
        //RATCHET
        if (gamepad1.y) {
            ratchet_deploy();
        } else if (gamepad1.x) {
            ratchet_release();
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
            hook_in();
        } else if (gamepad2.right_trigger > 0.1){
            hook_out();
        }


        //////////////////////////////////////////////////////////////////////////////
        // TAB SLAPPER
        if (gamepad2.x){
            tab_slapper_in();
        }else if (gamepad2.y){
            tab_slapper_out();
        }


        //////////////////////////////////////////////////////////////////////////////
        // PUSH BEACON
        if (gamepad2.start) {
            push_beacon(true);
        } else if (gamepad1.start) {
            push_beacon(false);
        }

        telemetry.addData("Push Beacon", v_servo_push_beacon.getPosition());


        /////////////////////////////////////////////////////////////////////////////
        //CLIMBER DROPPER
        if(gamepad2.left_trigger > 0.1) {
            climber_dropper_out();
        } else if (gamepad2.left_bumper) {
            climber_dropper_in();
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



        //INVERSE KINEMATICS
        /*

        - track how far we've rotated
            - encoder positions --> angle
        - NO - track point in space at end of arm
            - x and y coordinate
        - calculate between
            - figure out how far to move
        - move toward it code (iterations, checking to see if there yet?)
            - BUT - if we do this, what if delay causes Zach to move it too far?
            - have it sense if Zach is moving up or moving sideways
                based on that, do a motion to extend uniformly up or down/sideways



         */

    } // loop
} // Autonomous
