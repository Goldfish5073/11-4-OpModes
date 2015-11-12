package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;





/**
 * Created by Jachzach on 10/21/2015.
 */
//TODO: register colorsensor in hardware, see why threw uncaught exception can't find hardware device "mr"
public class Autonomous extends Hardware
{
    String step;
    FtcConfig ftcConfig=new FtcConfig();

    public Autonomous()
    {
    }

    private int v_state = 1;

    private String beaconPosition;

    private ColorSensor sensorRGB;
    private GyroSensor sensorGyro;


    //  hardwareMap.logDevices();

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;
    int xVal, yVal, zVal = 0;
    int heading = 0;
    boolean teamColorBlue;

    ReadBeacon readBeacon1 = new ReadBeacon();
    MoveArm moveArm1 = new MoveArm();
    PressButton pressButton1 = new PressButton();
    Delay delay1 = new Delay();
    Turn turn1 = new Turn();
    Drive drive1 = new Drive();
    Drive drive2 = new Drive();
    Drive drive3 = new Drive();
    GyroTurn gyroTurn1 = new GyroTurn();
    GyroTurn gyroTurn2 = new GyroTurn();
    Pause pause1 = new Pause();
    Pause pause2 = new Pause();
    Drive driveButton1 = new Drive();

    @Override public void start ()
    {
        //TODO - have phone ask red or blue during init and save as variable
        //Hardware start method
        super.start();
        reset_drive_encoders();
        step = "start";

    try {
        sensorRGB = hardwareMap.colorSensor.get("mr");
    } catch (Exception p_exception) {
        m_warning_message("color sensor");
        DbgLog.msg(p_exception.getLocalizedMessage());

        sensorRGB = null;
    }

    try {
        sensorGyro = hardwareMap.gyroSensor.get("gyro");
    } catch (Exception p_exception) {
        m_warning_message("gyro sensor");
        DbgLog.msg(p_exception.getLocalizedMessage());

        sensorGyro = null;
    }


    ftcConfig.init(hardwareMap.appContext, this);
    // sensorRGB = hardwareMap.colorSensor.get("mr");
        sensorGyro.calibrate();


        readBeacon1.reset();
        moveArm1.reset();
        pressButton1.reset();
        delay1.reset();
        turn1.reset();
        drive1.reset();
        drive2.reset();
        drive3.reset();
        gyroTurn1.reset();
        gyroTurn2.reset();
        pause1.reset();
        pause2.reset();
        driveButton1.reset();
    } // start

    @Override public void loop ()

    {

        telemetry.clearData();
        // can use configured variables here
        telemetry.addData("ColorIsRed", Boolean.toString(ftcConfig.param.colorIsRed));
        telemetry.addData("DelayInSec", Integer.toString(ftcConfig.param.delayInSec));
        telemetry.addData("AutonType", ftcConfig.param.autonType);
        telemetry.addData("Step: ", step);
        telemetry.addData("Heading: ", heading);


       /* if (delay1.action()) {
        } else if (gyroTurn1.action(1.0f, 60)){
        } else if (drive1.action(1.0f, 10)){
        } else if (turn1.action(1.0f, 10)){
        } else if (readBeacon1.action()) {
        } else if (moveArm1.action()) {
        } else if (pressButton1.action()) {
        }*/

        if (delay1.action()) {
            step = "delay";
        } else if (drive1.action(0.5f, 30)) {
            step = "drive";
        } else if (pause1.action(1)) {
            step = "pause";
        } else if (gyroTurn1.action(0.2f, 40)) {
            step = "gyro turn";
            ///encoder in inches?
        } else if (drive2.action(0.5f, 37)) {
            step = "drive 2";
        } else if (gyroTurn2.action(0.2f, 45)){
            step = "gyro turn2";
        } else if (drive3.action(0.2f, 8)){
            step = "drive 3";
        } else if (pause2.action(5)){
            step = "pause 2";
        }
        else if (readBeacon1.action()) {
            step = "read beacon";
        } else if (moveArm1.action()) {
            step = "move arm";
        } //else if (pressButton1.action()) {}
        else if (driveButton1.action(0.5f, 2)) {
            step = "drive button";
        }

            //
            // Send telemetry data to the driver station.
            //
            color();
            update_telemetry(); // Update common telemetry
            telemetry.addData("18", "State: " + v_state);
    } // loop





    void pause ()
    {
        if (have_drive_encoders_reset())
        {
            v_state++;
        }
    }

    public void color()
    {

        sensorRGB.enableLed(false);

        // convert the RGB values to HSV values.
        //Color.RGBToHSV((sensorRGB.red() * 8), (sensorRGB.green() * 8), (sensorRGB.blue() * 8), hsvValues);
        Color.RGBToHSV(sensorRGB.red() * 8, sensorRGB.green() * 8, sensorRGB.blue() * 8, hsvValues);

        // send the info back to driver station using telemetry function.
        telemetry.addData("Clear", sensorRGB.alpha());
        telemetry.addData("Red  ", sensorRGB.red());
        telemetry.addData("Green", sensorRGB.green());
        telemetry.addData("Blue ", sensorRGB.blue());
        telemetry.addData("Hue", hsvValues[0]);


    }

    private class ReadBeacon
    {
        String color;
        //0 = unknown
        //1 = blue
        //-1 = red
        int state;
        ReadBeacon()
        {
            state = -1;
            color = "unknown";
        }

        void reset()
        {
            state = -1;
            color = "unknown";
        }

        boolean action()
        {
            if (state == 1) {
                return false;
            }

            color();

            if (hsvValues[0] > 10)
            {
                color = "blue";
            } else if (false) //determines if is blue
            {
                color = "red";
            }

            if (ftcConfig.param.colorIsRed && color.equals("red")) {
                beaconPosition = "left";
            } else if (ftcConfig.param.colorIsRed && color.equals("blue"))
            {
                beaconPosition = "right";
            } else if (!ftcConfig.param.colorIsRed && color.equals("blue"))
            {
                beaconPosition = "left";
            } else if (!ftcConfig.param.colorIsRed && color.equals("red"))
            {
                beaconPosition = "right";
            } else
            {
                beaconPosition = "unknown";
            }

            state = 1;

            return true;

        }
    }
    private class MoveArm {
        int state;
        MoveArm(){
            state = -1;
        }
        void reset()
        {
            state = -1;
        }
        boolean action(){
            if (state == 1) {
                return false;
            }
            if(!beaconPosition.equals("unknown")){
                push_beacon(beaconPosition.equals("left"));
            }

            state = 1;

            return true;
        }
    }
    private class PressButton {
        int state;
        PressButton() {
            state = -1;
        }
        void reset()
        {
            state = -1;
        }
        boolean action(){
            if (state == 1){
                return false;
            }

            drive(1.0f, 2);

            state = 1;
            return true;
        }
    }

    private class Delay {
        int state;
        long startTime;
        Delay() {
            state = -1;
        }
        void reset() { state = -1; }
        boolean action() {
            if (state == -1){
                startTime = System.currentTimeMillis();
                state = 0;
            }
            if (state == 1) {
                return false;
            }


            if (System.currentTimeMillis() > (startTime + ftcConfig.param.delayInSec * 1000)) {
                state = 1;
            }

            return true;
        }
    }

    private class Pause {
        int state;
        long startTime;
        Pause() {
            state = -1;
        }
        void reset() { state = -1; }
        boolean action(int seconds) {
            if (state == -1){
                startTime = System.currentTimeMillis();
                state = 0;
            }
            if (state == 1) {
                return false;
            }


            if (System.currentTimeMillis() > (startTime + seconds * 1000)) {
                state = 1;
            }

            return true;
        }
    }


    private class Drive {
        int state;

        Drive() {
            state = -1;
            run_using_encoders();
        }

        void reset() {
            state = -1;
        }

        boolean action(float speed, int encoderCount) {
            run_using_encoders();
            if (state == -1){
                set_drive_power(speed, speed);
                state = 0;
            }
            if (state == 1)
            {
                return false;
            }


            if (have_drive_encoders_reached (encoderCount, encoderCount))
            {
                reset_drive_encoders ();
                set_drive_power (0.0f, 0.0f);
                state = 2;
            }

            if (state == 2 && have_drive_encoders_reset ())
            {
               state = 1;
            }
            telemetry.addData("17", "State: " + state);

            return true;
        }
    }


    private class Turn {
        int state;

        Turn() {
            state = -1;
            run_using_encoders();
        }

        void reset() {
            state = -1;
        }

        boolean action(float speed, int encoderCount) {
            if (state == -1){
                if (ftcConfig.param.colorIsRed){
                    set_drive_power(speed, -speed);
                    state = 0;
                }
                else{
                    set_drive_power(-speed, speed);
                    state = 0;
                }
                //if blue turn one way if red turn other

            }

            if (state == 1)
            {
                return false;
            }

            state = 0;

            if (have_drive_encoders_reached (encoderCount, encoderCount))
            {
                reset_drive_encoders ();
                set_drive_power (0.0f, 0.0f);
                state = 1;
            }

            return true;
        }
    }


    private class GyroTurn {
        int state;

        GyroTurn() {
            state = -1;
            run_using_encoders();
        }

        void reset() {
            state = -1;
            if(sensorGyro != null){
                sensorGyro.resetZAxisIntegrator();
            }
        }

        boolean action(float speed, int desiredHeading) {
            if (state == -1){
                //sets turning direction based on color
                sensorGyro.resetZAxisIntegrator();
                if (ftcConfig.param.colorIsRed){
                    set_drive_power(-speed, speed);
                }
                else {
                    set_drive_power(speed, -speed);
                }
                state = 0;
            }

            if (state == 1)
            {
                return false;
            }

            if (ftcConfig.param.colorIsRed)
            {
                desiredHeading = 360 - desiredHeading;
            }

            xVal = sensorGyro.rawX();
            yVal = sensorGyro.rawY();
            zVal = sensorGyro.rawZ();

            heading = sensorGyro.getHeading();

            //turn until gyro reaches approximate correct place
            //heading
            if (Math.abs(heading - desiredHeading) < 5){
                reset_drive_encoders ();
                set_drive_power (0.0f, 0.0f);
                state = 1;
            }
            telemetry.addData("Desired Heading", desiredHeading);
            telemetry.addData("Color", ftcConfig.param.colorIsRed);

//abs of current - desired
            //if ^ is less than a given approximate range 2
            //then stop!

        return true;
        }
    }

    void drive (float speed, int encoderCount)
    {

        run_using_encoders();
        set_drive_power(speed, speed);
        if (have_drive_encoders_reached (encoderCount, encoderCount))
        {
            reset_drive_encoders ();
            set_drive_power (0.0f, 0.0f);
            v_state++;
        }
    }
    //read beacon
    //move arm
    //move forward to press
}



