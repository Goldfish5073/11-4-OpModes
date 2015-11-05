package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Jachzach on 10/21/2015.
 */
public class TeleOp extends Hardware {

    public TeleOp()
    {
    }

    @Override public void loop ()

    {
        //----------------------------------------------------------------------
        //
        // DC Motors
        //
        // Obtain the current values of the joystick controllers.
        //
        // Note that x and y equal -1 when the joystick is pushed all of the way
        // forward (i.e. away from the human holder's body).
        //
        // The clip method guarantees the value never exceeds the range +-1.
        //
        // The DC motors are scaled to make it easier to control them at slower
        // speeds.
        //
        // The setPower methods write the motor power values to the DcMotor
        // class, but the power levels aren't applied until this method ends.
        //

        //DRIVE
        float l_gp1_left_stick_y = -gamepad1.left_stick_y;
        float l_left_drive_power
                = (float)scale_motor_power (l_gp1_left_stick_y);

        float l_gp1_right_stick_y = -gamepad1.right_stick_y;
        float l_right_drive_power
                = scale_motor_power (l_gp1_right_stick_y);

        set_drive_power (l_left_drive_power, l_right_drive_power);


        //ARM
        float l_gp2_left_stick_y = gamepad2.left_stick_y;
        float l_first_arm_power
                = scale_motor_power (l_gp2_left_stick_y);

        float l_gp2_right_stick_y = -gamepad2.right_stick_y;
        float l_second_arm_power
                = scale_motor_power (l_gp2_right_stick_y);

        set_arm_power (l_first_arm_power, l_second_arm_power);


        //CLAW
        float l_claw_power = scale_motor_power(0);
        if (gamepad1.left_bumper)
        {
            l_claw_power = scale_motor_power(1);
        }else if (gamepad1.left_trigger > 0.0)
        {
            l_claw_power = scale_motor_power(-1);
        }
        set_claw_power(l_claw_power);



        //WINCH
        float l_winch_power = scale_motor_power(0);
        if (gamepad2.right_bumper)
        {
            l_winch_power = scale_motor_power(1);
        }else if (gamepad2.right_trigger > 0.0)
        {
            l_winch_power = scale_motor_power(-1);
        }
        set_winch_power(l_winch_power);

        //HOOK
        if (gamepad2.x){
            hook();
        }

        // TAB SLAPPER
        if (gamepad2.y){
            tab_slapper();
        }

        //push_beacon(gamepad1.a);




        //----------------------------------------------------------------------
        //
        // Servo Motors
        //
        // Obtain the current values of the gamepad 'x' and 'b' buttons.
        //
        // Note that x and b buttons have boolean values of true and false.
        //
        // The clip method guarantees the value never exceeds the allowable
        // range of [0,1].
        //
        // The setPosition methods write the motor power values to the Servo
        // class, but the positions aren't applied until this method ends.
        //


        //
        // Send telemetry data to the driver station.
        //
        update_telemetry (); // Update common telemetry
        update_gamepad_telemetry ();

       /* update_telemetry (); // Update common telemetry
        update_gamepad_telemetry ();
        telemetry.addData
                ( "13"
                        , "Push Beacon Position: " + v_servo_push_beacon.getPosition();
                );*/

    } // loop
} // Autonomous
