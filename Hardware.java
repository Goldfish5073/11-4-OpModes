package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Jachzach on 10/18/2015.
 */

//TODO: register ColorSensor sensorRGB in here
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.Range;

public class Hardware extends OpMode {

    final double diameter = 4;

    public Hardware() {

    }


    @Override
    public void init()

    {
        //
        // Use the hardwareMap to associate class members to hardware ports.
        //
        // Note that the names of the devices (i.e. arguments to the get method)
        // must match the names specified in the configuration file created by
        // the FTC Robot Controller (Settings-->Configure Robot).
        //
        // The variable below is used to provide telemetry data to a class user.
        //
        v_warning_generated = false;
        v_warning_message = "Can't map; ";

        //
        // Connect the drive wheel motors.
        //
        // The direction of the right motor is reversed, so joystick inputs can
        // be more generically applied.


        try {
            v_drive_controller = hardwareMap.dcMotorController.get ("drive_controller");
        }
        catch (Exception p_exception)
        {
            m_warning_message ("drive_controller");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_drive_controller = null;
        }
        try {
            v_drive_controller_back = hardwareMap.dcMotorController.get ("drive_controller_back");
        }
        catch (Exception p_exception)
        {
            m_warning_message ("drive_controller_back");
            DbgLog.msg(p_exception.getLocalizedMessage());

            v_drive_controller_back = null;
        }


        try
        {
            v_motor_left_drive = hardwareMap.dcMotor.get ("left_drive");

        }
        catch (Exception p_exception)
        {
            m_warning_message ("left_drive");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_motor_left_drive = null;
        }

        try
        {
            v_motor_left_drive_back = hardwareMap.dcMotor.get ("left_drive_back");

        }
        catch (Exception p_exception)
        {
            m_warning_message("left_drive_back");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_motor_left_drive_back = null;
            //  v_motor_left_drive_back.setDirection (DcMotor.Direction.REVERSE);
        }

        try
        {
            v_motor_right_drive = hardwareMap.dcMotor.get ("right_drive");
            v_motor_right_drive.setDirection (DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exception)
        {
            m_warning_message ("right_drive");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_motor_right_drive = null;
        }

        try
        {
            v_motor_right_drive_back = hardwareMap.dcMotor.get ("right_drive_back");
            //v_motor_right_drive_back.setDirection (DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exception)
        {
            m_warning_message ("right_drive_back");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_motor_right_drive_back = null;
        }
        try
        {
            v_servo_push_beacon = hardwareMap.servo.get ("push_beacon");
            v_servo_push_beacon.setPosition (0.0D);
        }
        catch (Exception p_exception)
        {
            m_warning_message ("push_beacon");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_servo_push_beacon = null;
        }

        try
        {
            v_first_arm = hardwareMap.dcMotor.get ("first_arm");
            //v_first_arm.setDirection (DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exception)
        {
            m_warning_message ("first_arm");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_first_arm = null;
        }

        try
        {
            v_second_arm = hardwareMap.dcMotor.get ("second_arm");
            //v_first_arm.setDirection (DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exception)
        {
            m_warning_message ("second_arm");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_second_arm = null;
        }
        try
        {
            v_claw = hardwareMap.dcMotor.get("claw");
        }
        catch (Exception p_exception)
        {
            m_warning_message ("claw");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_claw = null;
        }
        try
        {
            v_winch = hardwareMap.dcMotor.get ("winch");

        }
        catch (Exception p_exception)
        {
            m_warning_message ("winch");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_winch = null;
        }
        try
        {
            v_hook = hardwareMap.servo.get ("hook");
            v_hook.setPosition(0.25D);

        }
        catch (Exception p_exception)
        {
            m_warning_message ("hook");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_hook = null;
        }
        try
        {
            v_tab_slapper = hardwareMap.servo.get ("tab_slapper");
            v_tab_slapper.setPosition(0.0D);

        }
        catch (Exception p_exception)
        {
            m_warning_message ("tab_slapper");
            DbgLog.msg (p_exception.getLocalizedMessage ());

            v_tab_slapper = null;
        }


        /*v_motor_left_drive = hardwareMap.dcMotor.get("left_drive");

        v_motor_left_drive_back = hardwareMap.dcMotor.get("left_drive_back");
        v_motor_left_drive_back.setDirection(DcMotor.Direction.REVERSE);

        v_motor_right_drive = hardwareMap.dcMotor.get("right_drive");
        v_motor_right_drive.setDirection(DcMotor.Direction.REVERSE);

        v_motor_right_drive_back = hardwareMap.dcMotor.get("right_drive_back");

        v_first_arm = hardwareMap.dcMotor.get("first_arm");

        v_second_arm = hardwareMap.dcMotor.get("second_arm");

        v_claw = hardwareMap.dcMotor.get("claw");

        v_servo_push_beacon = hardwareMap.servo.get("push_beacon");
        v_servo_push_beacon.setPosition(0.0D);*/

        reset_drive_encoders ();
        run_using_encoders ();
    } // init

    //WARNING MESSAGES - PREVENT CRASH - TAKE OUT LATER IF POSSIBLE
    void m_warning_message (String p_exception_message)

    {
        if (v_warning_generated)
        {
            v_warning_message += ", ";
        }
        v_warning_generated = true;
        v_warning_message += p_exception_message;

    }
    private boolean v_warning_generated = false;
    private String v_warning_message;

    String a_warning_message () //goes into telemetry to report

    {
        return v_warning_message;

    }
    boolean a_warning_generated ()

    {
        return v_warning_generated;

    }

    //TELEMETRY
    public void update_telemetry ()

    {
        if (a_warning_generated ())
        {
            set_first_message (a_warning_message ());
        }
        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ( "01"
                        , "Left Drive: "
                                + a_left_drive_power ()
                                + ", "
                                + a_left_encoder_count()
                                + ", "
                                + v_motor_left_drive.getChannelMode()
                );
        telemetry.addData
                ( "02"
                        , "Right Drive: "
                                + a_right_drive_power ()
                                + ", "
                                + a_right_encoder_count ()
                );
        if(v_winch != null) {
            telemetry.addData("03", "Winch Drive: " + a_winch_power());
        }
    }

    public void set_first_message (String p_message)

    {
        telemetry.addData ( "00", p_message);

    }
    public void set_error_message (String p_message)

    {
        set_first_message("ERROR: " + p_message);

    }
    public void update_gamepad_telemetry ()

    {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData ("05", "GP1 Left: " + -gamepad1.left_stick_y);
        telemetry.addData ("06", "GP1 Right: " + -gamepad1.right_stick_y);
        telemetry.addData ("07", "GP2 Left: " + -gamepad2.left_stick_y);
        telemetry.addData ("08", "GP2 X: " + gamepad2.x);
        telemetry.addData ("09", "GP2 Y: " + gamepad2.y);
        telemetry.addData ("10", "GP1 LT: " + gamepad1.left_trigger);
        telemetry.addData ("11", "GP1 RT: " + gamepad1.right_trigger);
        telemetry.addData ("12", "GP1 LB: " + gamepad1.left_bumper);
        telemetry.addData ("13", "GP2 RT: " + gamepad2.right_trigger);
        telemetry.addData ("14", "GP2 RB: " + gamepad2.right_bumper);


        telemetry.addData("100", "GP1 A: " + gamepad1.a);
    } // update_gamepad_telemetry




    //--------------------------------------------------------------------------
    //
    // start
    //
    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     *
     * The system calls this member once when the OpMode is enabled.
     */
    @Override public void start ()

    {
        //
        // Only actions that are common to all Op-Modes (i.e. both automatic and
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // start

    //--------------------------------------------------------------------------
    //
    // loop
    //
    /**
     * Perform any actions that are necessary while the OpMode is running.
     *
     * The system calls this member repeatedly while the OpMode is running.
     */
    @Override public void loop ()

    {
        //
        // Only actions that are common to all OpModes (i.e. both auto and\
        // manual) should be implemented here.
        //
        // This method is designed to be overridden.
        //

    } // loop


    //--------------------------------------------------------------------------
    //The system calls this member once when the OpMode is disabled.
    @Override public void stop ()
    {
    } // stop

    //--------------------------------------------------------------------------
    //
    // scale_motor_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scale_motor_power (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    } // scale_motor_power

    //--------------------------------------------------------------------------
    //
    // a_left_drive_power
    //
    /**
     * Access the left drive motor's power level.
     */
    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_left_drive != null)
        {
            l_return = v_motor_left_drive.getPower ();
        }

        return l_return;

    } // a_left_drive_power

    //--------------------------------------------------------------------------
    //
    // a_right_drive_power
    //
    /**
     * Access the right drive motor's power level.
     */
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (v_motor_right_drive != null)
        {
            l_return = v_motor_right_drive.getPower ();
        }

        return l_return;

    } // a_right_drive_power

    double a_claw_power ()
    {
        double l_return = -999;
        if (v_claw != null)
        {
            l_return = v_claw.getPower() / 4;
        }
        return l_return;
    }

    double a_winch_power()
    {
        double l_return = -999;
        if (v_winch != null)
        {
            l_return = v_winch.getPower();
        }
        return l_return;
    }
    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //
    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_drive_power (double p_left_power, double p_right_power)

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setPower (p_left_power);
        }
        if (v_motor_left_drive_back != null)
        {
            v_motor_left_drive_back.setPower (-p_left_power);
        }
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setPower (p_right_power);
        }
        if (v_motor_right_drive_back != null)
        {
            v_motor_right_drive_back.setPower (p_right_power);
        }


    } // set_drive_power



    //set_arm_power
    void set_arm_power (double p_first_power, double p_second_power)

    {
        if (v_first_arm != null)
        {
            v_first_arm.setPower(p_first_power);
        }
        if (v_second_arm != null)
        {
            v_second_arm.setPower (p_second_power);
        }
    }

    void set_winch_power(double p_power)
    {
        if(v_winch != null)
        {
            v_winch.setPower(p_power);
        }
    }

    void set_claw_power(double p_power)
    {
        if(v_claw != null)
        {
            v_claw.setPower(p_power);
        }
    }

    //--------------------------------------------------------------------------
    //
    // run_using_left_drive_encoder
    //
    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setChannelMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }
    } // run_using_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_right_drive_encoder
    //
    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setChannelMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_encoders
    //
    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_using_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_using_left_drive_encoder ();
        run_using_right_drive_encoder ();

    } // run_using_encoders

    //--------------------------------------------------------------------------
    //
    // run_without_left_drive_encoder
    //
    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            if (v_motor_left_drive.getChannelMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                v_motor_left_drive.setChannelMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    } // run_without_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_right_drive_encoder
    //
    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            if (v_motor_right_drive.getChannelMode () ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                v_motor_right_drive.setChannelMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    } // run_without_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_drive_encoders
    //
    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_without_drive_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_without_left_drive_encoder ();
        run_without_right_drive_encoder ();

    } // run_without_drive_encoders

    //--------------------------------------------------------------------------
    //
    // reset_left_drive_encoder
    //
    /**
     * Reset the left drive wheel encoder.
     */
    public void reset_left_drive_encoder ()

    {
        if (v_motor_left_drive != null)
        {
            v_motor_left_drive.setChannelMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_right_drive_encoder
    //
    /**
     * Reset the right drive wheel encoder.
     */
    public void reset_right_drive_encoder ()

    {
        if (v_motor_right_drive != null)
        {
            v_motor_right_drive.setChannelMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_drive_encoders
    //
    /**
     * Reset both drive wheel encoders.
     */
    public void reset_drive_encoders ()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_left_drive_encoder ();
        reset_right_drive_encoder ();

    } // reset_drive_encoders

    //--------------------------------------------------------------------------
    //
    // a_left_encoder_count
    //
    /**
     * Access the left encoder's count.
     */
    int a_left_encoder_count ()
    {
        int l_return = 0;

        if (v_motor_left_drive != null)
        {
            l_return = (int) (v_motor_left_drive.getCurrentPosition () * (diameter * Math.PI) / 1120);
        }

        return l_return;

    } // a_left_encoder_count

    //--------------------------------------------------------------------------
    //
    // a_right_encoder_count
    //
    /**
     * Access the right encoder's count.
     */
    int a_right_encoder_count ()

    {
        int l_return = 0;

        if (v_motor_right_drive != null)
        {
            l_return = (int) (v_motor_right_drive.getCurrentPosition () * (diameter * Math.PI) / 1120);
        }

        return l_return;

    } // a_right_encoder_count

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reached
    //
    /**
     * Indicate whether the left drive motor's encoder has reached a value.
     */
    boolean has_left_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_left_drive != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (v_motor_left_drive.getCurrentPosition () * (diameter * Math.PI) / 1120) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reached
    //
    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (v_motor_right_drive != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (v_motor_right_drive.getCurrentPosition () * (diameter * Math.PI) / 1120) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reached
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */



    boolean have_drive_encoders_reached
    ( double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_drive_encoder_reached (p_left_count) &&
                has_right_drive_encoder_reached (p_right_count))
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_encoders_reached

    //--------------------------------------------------------------------------
    //
    // drive_using_encoders
    //
    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean drive_using_encoders
    ( double p_left_power
            , double p_right_power
            , double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume the encoders have not reached the limit.
        //
        boolean l_return = false;

        //
        // Tell the system that motor encoders will be used.
        //
        run_using_encoders ();

        //
        // Start the drive wheel motors at full power.
        //
        set_drive_power (p_left_power, p_right_power);

        //
        // Have the motor shafts turned the required amount?
        //
        // If they haven't, then the op-mode remains in this state (i.e this
        // block will be executed the next time this method is called).
        //
        if (have_drive_encoders_reached (p_left_count, p_right_count))
        {
            //
            // Reset the encoders to ensure they are at a known good value.
            //
            reset_drive_encoders ();

            //
            // Stop the motors.
            //
            set_drive_power (0.0f, 0.0f);

            //
            // Transition to the next state when this method is called
            // again.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // drive_using_encoders

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_left_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the left encoder reached zero?
        //
        if (a_left_encoder_count () == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_right_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the right encoder reached zero?
        //
        if (a_right_encoder_count () == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reset
    //
    /**
     * Indicate whether the encoders have been completely reset.
     */
    boolean have_drive_encoders_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached zero?
        //
        if (has_left_drive_encoder_reset () && has_right_drive_encoder_reset ())
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_drive_encoders_reset

    void push_beacon (boolean isLeft)
    {
        if (isLeft)
        {
            v_servo_push_beacon.setPosition (0.0D);
        }
        else
        {
            v_servo_push_beacon.setPosition (1.0D);
        }
    }

    void hook (){
        if (v_hook.getPosition() < 0.9) {
            v_hook.setPosition(v_hook.getPosition() + .05);
        }
    }
    void hookBack (){
        if (v_hook.getPosition() > 0.25) {
            v_hook.setPosition (v_hook.getPosition() - .05);
        }
    }

    void tab_slapper (){
        if (v_tab_slapper.getPosition() < 1.0D){
            v_tab_slapper.setPosition (1.0D);
        }
        else{
            v_tab_slapper.setPosition (0.0D);
        }
    }

    private DcMotor v_motor_left_drive;

    private DcMotor v_motor_left_drive_back;

    private DcMotor v_motor_right_drive;

    private DcMotor v_motor_right_drive_back;

    public Servo v_servo_push_beacon;

    private DcMotor v_first_arm;

    private DcMotor v_second_arm;

    private DcMotor v_claw;

    private ColorSensor v_sensorRGB;

    private DcMotor v_winch;

    private DcMotorController v_drive_controller;

    private DcMotorController v_drive_controller_back;

    public Servo v_hook;

    public Servo v_tab_slapper;
}


