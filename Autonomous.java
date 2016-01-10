package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;


public class Autonomous extends Hardware
{
    String step;
    FtcConfig ftcConfig=new FtcConfig();
    float gyroTurnSpeed = 0.3f;
    float grey = 5f;
    float white = 3f;
    String shoulderStartTime;
    boolean doneCalibrating;


    public Autonomous()
    {
    }

    private String beaconPosition;
    private String color = "";

    private ColorSensor firstRGB;
   // private ColorSensor secondRGB;
    private ColorSensor sensorRGB;
    private GyroSensor sensorGyro;
    private OpticalDistanceSensor sensorODS;
    float hsvValuesFirst[] = {0F,0F,0F};
    float hsvValuesSecond[] = {0F,0F,0F};
    float hsvValuesSensor[] = {0F,0F,0F};

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F,0F,0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;
    int xVal, yVal, zVal = 0;
    int heading = 0;

    ReadBeacon readBeacon1 = new ReadBeacon();

    MoveArm moveArmForReset = new MoveArm();
    MoveArm moveArmForBeacon = new MoveArm();
    MoveArm moveArmForTeleOp = new MoveArm();

    Delay delayBeacon = new Delay();
    Delay delayM = new Delay();

    Turn turnToHitBeacon = new Turn(); //NOT CURRENTLY IN USE

    DropPusherClimber dropClimbersForButtonPusher = new DropPusherClimber();
    DropClimbersComplete dropClimbersComplete = new DropClimbersComplete();



    DriveStraight initialDriveStraight = new DriveStraight();
    DriveStraight driveStraightToBeaconZone = new DriveStraight();
    DriveStraight driveFarther = new DriveStraight();
    DriveStraightColor initialDriveStraightColor = new DriveStraightColor();



    Drive driveToPushAwayDebris = new Drive();
    Drive driveToAlignAfterODS = new Drive();
    Drive driveToReadBeacon = new Drive();
    Drive driveBackToMoveArm = new Drive();
    Drive driveToPressButton = new Drive();
    Drive driveBackFinal = new Drive();
    Drive driveToDoubleCheckButton = new Drive();
    Drive driveToSetUp = new Drive();
    Drive driveToPress = new Drive();
    Drive driveBackAfterReadBeacon = new Drive();



    Drive driveM_ToMountain = new Drive();
    Drive driveM_OntoMountain = new Drive();
    Drive driveM_BackToAlign = new Drive();

    Drive drive1 = new Drive(); //NOT CURRENTLY IN USE
    Drive driveButton1 = new Drive(); //NOT CURRENTLY IN USE
    Drive driveButton2 = new Drive(); //NOT CURRENTLY IN USE


    ODSReverse odsReverseBeacon = new ODSReverse();
    ColorReverse colorReverse = new ColorReverse();

    ODSTurn odsTurn1 = new ODSTurn(); //NOT CURRENTLY IN USE


    GyroTurnCompass gyroTurnToPushAwayDebris = new GyroTurnCompass();
    GyroTurn gyroTurnM_ToFaceMountain = new GyroTurn();
    GyroTurnCompass initialGyroTurn = new GyroTurnCompass();



    Pause pauseGyro = new Pause();
    Pause pauseAfterODSAlign = new Pause();
    Pause pauseAfterFaceBeaconTurn = new Pause();
    Pause pauseToReadBeacon = new Pause();
    Pause pauseForBeaconPusher = new Pause();

    Pause pauseM = new Pause();
    Pause pauseM2 = new Pause();

    GyroTurnCompass gyroTurnCompassToLine = new GyroTurnCompass();
    BeaconAlign beaconAlign = new BeaconAlign();

    MoveShoulder moveBigArmUp = new MoveShoulder();

    @Override public void init() {
        super.init();

        try {
            firstRGB = hardwareMap.colorSensor.get("mr");
            firstRGB.setI2cAddress(0x5c);
            firstRGB.enableLed(false);
        } catch (Exception p_exception) {
            m_warning_message("color sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            firstRGB = null;
        }

        moveBigArmUp.reset();
    }


    @Override public void start () {
        super.start();

        doneCalibrating = false;

        reset_drive_encoders();

        step = "start";
        beaconPosition = "unknown";

        try {
            sensorRGB = hardwareMap.colorSensor.get("mr3");
            sensorRGB.enableLed(false);
        } catch (Exception p_exception) {
            m_warning_message("color sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            sensorRGB = null;
        }



       /* try {
            cdim = hardwareMap.deviceInterfaceModule.get("sensors");
        } catch (Exception p_exception) {
            m_warning_message("sensors");
            DbgLog.msg(p_exception.getLocalizedMessage());
            cdim = null;
        }*/


        try {
            sensorGyro = hardwareMap.gyroSensor.get("gyro");
        } catch (Exception p_exception) {
            m_warning_message("gyro sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            sensorGyro = null;
        }
        try {
            sensorODS = hardwareMap.opticalDistanceSensor.get("ODS");
        } catch (Exception p_exception) {
            m_warning_message("ods sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            sensorODS = null;
        }

        update_telemetry();


        ftcConfig.init(hardwareMap.appContext, this);

        // //sensorRGB = hardwareMap.colorSensor.get("mr");
        sensorGyro.resetZAxisIntegrator();
        sensorGyro.calibrate();
        firstRGB.enableLed(true);
       // secondRGB.enableLed(true);


        readBeacon1.reset();

        moveArmForReset.reset();
        moveArmForBeacon.reset();
        moveArmForTeleOp.reset();

        delayBeacon.reset();

        delayM.reset();

        turnToHitBeacon.reset();
        //toBeacon.reset();
        drive1.reset();
        driveToPushAwayDebris.reset();
        driveToAlignAfterODS.reset();
        driveToReadBeacon.reset();
        driveBackToMoveArm.reset();
        driveToPressButton.reset();
        driveToDoubleCheckButton.reset();
        driveBackFinal.reset();
        driveToSetUp.reset();
        driveToPress.reset();

        driveM_ToMountain.reset();
        driveM_OntoMountain.reset();
        driveM_BackToAlign.reset();

        odsReverseBeacon.reset();
        colorReverse.reset();

        gyroTurnToPushAwayDebris.reset();
        //gyroTurnToFaceBeacon.reset();
        gyroTurnM_ToFaceMountain.reset();
        initialGyroTurn.reset();


        odsTurn1.reset();

        pauseAfterODSAlign.reset();
        pauseAfterFaceBeaconTurn.reset();
        pauseToReadBeacon.reset();
        pauseM.reset();
        pauseM2.reset();
        pauseForBeaconPusher.reset();

        pauseGyro.reset();

        driveButton1.reset();
        driveButton2.reset();

        driveBackAfterReadBeacon.reset();


        dropClimbersForButtonPusher.reset();
        dropClimbersComplete.reset();


        driveStraightToBeaconZone.reset();
        initialDriveStraight.reset();
        driveFarther.reset();
        initialDriveStraightColor.reset();

       // alignSwivle.reset();
        gyroTurnCompassToLine.reset();
        beaconAlign.reset();

        moveBigArmUp.reset();


    } // start


    @Override public void loop ()

    {
        telemetry.clearData();

        //telemetry.addData("000000000 Big Arm Encoder Count", finalArmEncoderCount);
        telemetry.addData("000000 Cycle Count", shoulderStartTime);

        telemetry.addData("ColorIsRed", Boolean.toString(ftcConfig.param.colorIsRed));
        telemetry.addData("DelayInSec", Integer.toString(ftcConfig.param.delayInSec));
        telemetry.addData("AutonType", ftcConfig.param.autonType);

        telemetry.addData("0000000000 Step: ", step);
        telemetry.addData("Heading: ", heading);
        // telemetry.addData("ods",sensorODS.getLightDetected() * 100 );
        telemetry.addData("beacon position", beaconPosition);
        telemetry.addData("beacon color", color);

        color();
        beaconColor();
        // telemetry.addData("color", color);

        if (sensorGyro.isCalibrating()) {
            return;
        }
        /*if (sensorGyro.isCalibrating() && !doneCalibrating) {
            return;
        } else {
            doneCalibrating = true;
        }*/


        if (ftcConfig.param.autonType == ftcConfig.param.autonType.GO_FOR_BEACON) {
            if /*(moveArmForReset.action(beaconPosition)) {
                step = "move arm 1 to reset";
            } else if*/ (pauseM.action(1)) {
                step = "pause";
            } else if (delayBeacon.action()) {
                step = "delay";
            } else if (initialDriveStraight.action(0.3f, 12, 0)) {
                step = "drive";
            } else if (initialGyroTurn.action(0.2f, 315, 45)) {
                step = "first turn";
            } else if (initialDriveStraightColor.action(0.25f, 87, 315)) {
                step = "drive straight color 1";
            } else if (driveFarther.action(0.3f, 12, 315)) {
                step = "drive straight a little";
            } else if (gyroTurnToPushAwayDebris.action(0.2f, 0, 0)) {
                step = "gyro to push away debris";
            } else if (driveToPushAwayDebris.action(0.3f, 20)) {
                step = "drive to push away debris";
            } else if (colorReverse.action(-0.2f)) {
                step = "color reverse";
            } else if (driveToAlignAfterODS.action(0.2f, getAlignDistance())) { //OUT //amount was getAlignDistance() (3 red and 10 blue)
                step = "drive forward a little bit to align";
            } else if (gyroTurnCompassToLine.action(0.25f, 275, 85)) { //should be 270 and 90 not 275 and 85
                step = "gyro turn compass to line";
            } else if (moveBigArmUp.action(0.3f, 80)) {
                step = "move big arm up";
            } else if (driveToReadBeacon.action(0.2f, getReadDistance())) { //OUT
                step = "drive forward to read beacon";
            } else if (pauseToReadBeacon.action(1)) {
                step = "pause to read beacon";
            } else if (readBeacon1.action()) {
                step = "read beacon";
            } else if (dropClimbersComplete.action()) {
                step = "drop climbers complete";
            } else if (driveBackAfterReadBeacon.action(-0.2f, 1)) {
                step = "drive back after read beacon";
            }else if (beaconAlign.action()) {
                step = "beacon align";
            } else if (dropClimbersForButtonPusher.action(true)) {
                step = "button pusher";
            } else if (pauseForBeaconPusher.action(3)) {
                step = "button pusher pause";
            } else if (!color.equals("unknown") && driveToPressButton.action(0.25f, pressButtonDistance())) {
                step = "press the button!!!";
            } else if (!color.equals("unknown") && driveBackFinal.action(-0.2f, 5)) {
                step = "drive back final!!!";
            }
        } else if (ftcConfig.param.autonType == ftcConfig.param.autonType.GO_FOR_MOUNTAIN) {
            if (delayM.action()) {
                step = "delayM";
            } else if (driveM_ToMountain.action(-0.3f, 40)) {
                step = "driveM";
            } else if (pauseM.action(1)) {
                step = "pauseM";
            } else if (driveM_BackToAlign.action(0.3f, 5)) {
                step = "pauseMBack";
            } else if (pauseM2.action(1)) {
                step = "pauseM2";
            } else if (gyroTurnM_ToFaceMountain.action(gyroTurnSpeed, 80)) {
                step = "gyroTurnM";
            } else if (driveM_OntoMountain.action(-0.5f, 25)) {
                step = "drive M2";
            }

        }

        update_telemetry();

    }



    public int pressButtonDistance() {
        //TODO - make another set for team colors
        if (ftcConfig.param.colorIsRed){
            if (color.equals("blue")) {
                return 7;
            } else {
                return 6;
            }
        } else {
            return 6; //for blue - will probably change
        }
    }


    public int getAlignDistance() { //TODO - may have to change back to just normal
        if (ftcConfig.param.colorIsRed) {
            return 4;
        } else {
            return 5; //maybe should 9
        }
    }

    public int getReadDistance() { //TODO - may have to change back to just normal
        if (ftcConfig.param.colorIsRed) {
            return 18;
        } else {
            return 20; //maybe should 9
        }
    }


 // loop


    /*
    one function
    rotate till of, go straight until find it
    on a timer

     */

    /*public class ToWhiteLine {
        int state;
        long startTime;
        double currentValue;

        ToWhiteLine() {
            state = -1;
        }

        void reset() {
            reset_drive_encoders();
            state = -1;
        }

        boolean action(float speed, int sec) {
            speed = -speed;

            if (state == 1) {
                return false;
            }

            if (state == 13) {
                return Stop();
            }

            currentValue = (sensorODS.getLightDetected() * 100);

            if (state == -1) {
                reset_drive_encoders();
                state = 3;
                return true;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }


            if (state == 0) {
                run_using_encoders();

                if (currentValue > white) {
                    if (ftcConfig.param.colorIsRed) {
                        set_drive_power(speed, -speed);
                    } else {
                        set_drive_power(-speed, speed);
                    }
                } else {
                    set_drive_power(speed, speed);
                }

                color();

                /*if (hsvValues[0] > 50) {
                color = "blue";
            } else if (sensorRGB.red() > 0) {
                color = "red";
            } else {
                color = "unknown";
            }


                if ((System.currentTimeMillis() > startTime + sec * 1000) || hsvValues[0] > 50 || sensorRGB.red() > 0) {
                    state = 2;
                    set_drive_power(0.0f, 0.0f);
                    reset_drive_encoders();
                }
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }

            return true;
        }
    }*/


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //HELPER METHODS

    public void beaconColor()
    {
        sensorRGB.enableLed(false);


        Color.RGBToHSV(sensorRGB.red() * 8, sensorRGB.green() * 8, sensorRGB.blue() * 8, hsvValuesSensor);

        telemetry.addData("AlphaBeacon", sensorRGB.alpha());
        telemetry.addData("RedBeacon  ", sensorRGB.red());
        telemetry.addData("GreenBeacon", sensorRGB.green());
        telemetry.addData("BlueBeacon ", sensorRGB.blue());
        telemetry.addData("HueBeacon", hsvValuesSensor[0]);
    }


    public void color()
    {
        /*secondRGB.enableLed(true);

        //Color.RGBToHSV(secondRGB.red() * 8, secondRGB.green() * 8, secondRGB.blue() * 8, hsvValuesSecond);
        telemetry.addData("AlphaLeft", secondRGB.alpha());
        telemetry.addData("RedLeft  ", secondRGB.red());
        telemetry.addData("GreenLeft", secondRGB.green());
        telemetry.addData("BlueLeft ", secondRGB.blue());
        telemetry.addData("HueLeft", hsvValuesSecond[0]); */

        firstRGB.enableLed(true);

        telemetry.addData("AlphaRight", firstRGB.alpha());
        telemetry.addData("RedRight  ", firstRGB.red());
        telemetry.addData("GreenRight", firstRGB.green());
        telemetry.addData("BlueRight ", firstRGB.blue());
        telemetry.addData("HueRight", hsvValuesFirst[0]);

    }
    private class BeaconAlign {
        GyroTurnCompass alignGyroTurn1Blue = new GyroTurnCompass();
        GyroTurnCompass alignGyroTurn2Blue = new GyroTurnCompass();
        GyroTurnCompass alignGyroTurn1Red = new GyroTurnCompass();
        GyroTurnCompass alignGyroTurn2Red = new GyroTurnCompass();
        Drive driveToAlignBlue = new Drive();
        Drive driveToAlignRed = new Drive();
        BeaconAlign(){

        }
        void reset(){
            driveToAlignBlue.reset();
            driveToAlignRed.reset();
            alignGyroTurn1Blue.reset();
            alignGyroTurn2Blue.reset();
            alignGyroTurn1Red.reset();
            alignGyroTurn2Red.reset();
        }
        boolean action(){
             if (color.equals("blue")){
                if (alignGyroTurn1Blue.action (0.2f, 245, 100)){
                    step = "15 degree to align 1 blue";
                    return true;
                } else if (driveToAlignBlue.action(-0.2f, 6)){
                    step = "drive to align blue";
                    return true;
                } else if (alignGyroTurn2Blue.action (0.2f, 270, 90)){
                    step = " 15 degree to align 2 blue";
                    return true;
                } else {
                    return false;
                }
             } else if (color.equals("red")){
                if (alignGyroTurn1Red.action (0.2f, 280, 80)){
                    step = "15 degree to align 1 red";
                    return true;
                }else if (driveToAlignRed.action(-0.2f, 6)){
                    step = "drive to align blue";
                    return true;
                } else if (alignGyroTurn2Red.action (0.2f, 270, 90)){
                    step = "15 degree to align 2 red";
                    return true;
                } else {
                    return false;
                }
            } else { //when color is unknown
                 return false;
             }
        }
    }
    // finds color of the beacon

    private class ReadBeacon
    {
        int state;

        ReadBeacon() {
            state = -1;
            color = "unknown";
            beaconPosition = "unknown";
        }

        void reset() {
            state = -1;
            color = "unknown";
            beaconPosition = "unknown";
        }

        boolean action() {
            if (state == 1) {
                return false;
            }

            beaconColor();

            if (sensorRGB.blue() > 0 && sensorRGB.red() == 0) {
                color = "blue";
            } else if (sensorRGB.red() > 0 && sensorRGB.blue() == 0) {
                color = "red";
            } else {
                color = "unknown";
            }

            if (ftcConfig.param.colorIsRed && color.equals("red")) {
                beaconPosition = "left";
            } else if (ftcConfig.param.colorIsRed && color.equals("blue")) {
                beaconPosition = "right";
            } else if (!ftcConfig.param.colorIsRed && color.equals("blue")) {
                beaconPosition = "left";
            } else if (!ftcConfig.param.colorIsRed && color.equals("red")) {
                beaconPosition = "right";
            } else {
                beaconPosition = "unknown";
            }

            state = 1;
            return true;
        }
    }


    private class MoveArm { //move the beacon bumper
        int state;
        MoveArm(){
            state = -1;
        }
        void reset() {
            state = -1;
        }

        boolean action(String beaconPosition){
            if (state == 1) {
                return false;
            }
            if(!beaconPosition.equals("unknown")){
                //push_beacon(beaconPosition.equals("left"));
            }
            else{
                //push_beacon_up();
            }

            state = 1;
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
//DRIVE STRAIGHT
    private class DriveStraight {
        int state;
        String drift;
        long startTime;

        DriveStraight() {
            state = -1;
            drift = "none";
        }
        void reset() {
            state = -1;
            drift = "none";
            reset_drive_encoders();
        }
        boolean action(float speed, int encoderCount, int direction) {
            if (state == 1) {
                return false;
            }

            telemetry.addData("17", "State: " + state);

            xVal = sensorGyro.rawX();
            yVal = sensorGyro.rawY();
            zVal = sensorGyro.rawZ();

            heading = sensorGyro.getHeading();

            if (state == -1) {
                reset_drive_encoders();
                startTime = System.currentTimeMillis();
                state = 3;
                drift = "none";
                return true;
            }

            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }

            float rightSpeed = speed;
            float leftSpeed = speed;

            run_using_encoders();

            int opposite = direction + 180;
            if (opposite > 360) {
                opposite -= 360;
            }

            int rightLim = direction + 1;
            if (rightLim > 360) {
                rightLim -= 360;
            }

            int leftLim = direction - 1;
            if (leftLim < 0) {
                leftLim += 360;
            }

            if (heading < opposite && heading > rightLim) {
                drift = "right";
            } else if (heading > opposite && heading < leftLim) {
                drift = "left";
            } else {
                drift = "none";
            }

            set_drive_power(-leftSpeed, -rightSpeed);

            telemetry.addData("Drift", drift);

            if (have_drive_encoders_reached(encoderCount, encoderCount)) {
                reset_drive_encoders();
                set_drive_power(0.0f, 0.0f);
                state = 2;
            }

            return true;
        }

    }

    private class DriveStraightColor {
        int state;
        String drift;
        long startTime;

        DriveStraightColor() {
            state = -1;
            drift = "none";
        }
        void reset() {
            state = -1;
            drift = "none";
            reset_drive_encoders();
        }
        boolean action(float speed, int encoderCount, int direction) {
            if (state == 1) {
                return false;
            }

            telemetry.addData("17", "State: " + state);

            //TODO - make a gyro() to contain four lines below
            xVal = sensorGyro.rawX();
            yVal = sensorGyro.rawY();
            zVal = sensorGyro.rawZ();

            heading = sensorGyro.getHeading();

            if (state == -1) {
                reset_drive_encoders();
                startTime = System.currentTimeMillis();
                state = 3;
                drift = "none";
                return true;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                }
                return true;
            }
            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }


            float rightSpeed = speed;
            float leftSpeed = speed;

            run_using_encoders();

            int opposite = direction + 180;
            if (opposite > 360) {
                opposite -= 360;
            }

            int rightLim = direction + 1;
            if (rightLim > 360) {
                rightLim -= 360;
            }

            int leftLim = direction - 1;
            if (leftLim < 0) {
                leftLim += 360;
            }

            if (heading < opposite && heading > rightLim) {
                drift = "right";
            } else if (heading > opposite && heading < leftLim) {
                drift = "left";
            } else {
                drift = "none";
            }

           /* if (drift.equals("right")) {
                leftSpeed += 0.2D;
            } else if (drift.equals("left")) {
                rightSpeed += 0.2D;
            }*/

            set_drive_power(-leftSpeed, -rightSpeed);

            telemetry.addData("00000000 Drift", drift);

            color();

            if (have_drive_encoders_reached(encoderCount, encoderCount)) {
                reset_drive_encoders();
                set_drive_power(0.0f, 0.0f);
                state = 2;
            }
            if (have_drive_encoders_reached(5, 5) && ((firstRGB.red() > 0 && firstRGB.red() < 250)
                    || (firstRGB.blue() > 0 && firstRGB.blue() < 250))) {
           //(secondRGB.red() >= 0 && secondRGB.red() < 250) ||(secondRGB.blue() >= 0 && secondRGB.blue() < 250)
            //TODO make 2 a variable
                reset_drive_encoders();
                set_drive_power(0.0f, 0.0f);
                state = 2;
            }

            return true;
        }

    }

    private class DropClimbersComplete {
        Drive driveBackForClimbers = new Drive();
        Pause pauseBeforeClimbers = new Pause();
        DropClimbers dropClimbers = new DropClimbers();
        Pause pauseToDropClimbers = new Pause();
        DropClimbers dropClimbersIn = new DropClimbers();

        int state;
        DropClimbersComplete(){
            state = -1;
        }
        void reset() {
            state = -1;
            driveBackForClimbers.reset();
            pauseBeforeClimbers.reset();
            pauseToDropClimbers.reset();
            dropClimbers.reset();
            dropClimbersIn.reset();
        }

        boolean action() {
            if (driveBackForClimbers.action(-0.3f, 1)) {
                step = "back up for climbers";
            } else if (pauseBeforeClimbers.action(2)) {
                step = "pause before climbers";
            } else if (dropClimbers.action(true)) {
                step = "drop climbers";
            } else if (pauseToDropClimbers.action(3)) {
                step = "last pause";
            } else if (dropClimbersIn.action(false)) {
                step = "last drive back!";
            } else {
                return false;
            }
            return true;
        }
    }

    private class DropClimbers {
    int state;
    DropClimbers(){
        state = -1;}
    void reset(){
        state = -1;
    }
    boolean action(boolean deploy){
        if (state == 1){
            return false;
        }
        if ( color != "unknown" && deploy){
            climber_dropper_out();
        } else {
            climber_dropper_in();
        }
        state = 1;
        return true;
    }
}
    private class DropPusherClimber {
        int state;
        DropPusherClimber(){
            state = -1;}
        void reset(){
            state = -1;
        }
        boolean action(boolean deploy){
            if (state == 1){
                return false;
            }
            if ( color != "unknown" && deploy){
                climber_dropper_pusher_out();
            } else {
                climber_dropper_pusher_in();
            }
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

    private boolean Stop() {
        reset_drive_encoders();
        set_drive_power (0.0f, 0.0f);
        return true;
    }


    private class Drive {
        int state;
        float  b = 0;

        Drive() {
            state = -1;
        }

        void reset() {
            reset_drive_encoders();
            state = -1;
        }

        boolean action(float speed, int encoderCount) {
            speed = -speed; //robot is backwards

            if (state == 1) {
                b = 3;
                return false;
            }

            if (state == -1){
                reset_drive_encoders();
                b = speed;
                state = 3;
                return true;
            }

            if (state == 3){
                if (have_drive_encoders_reset()){
                    state = 0;
                }
                return true;
            }

            if (state == 2) {
                b = 2;
                if (have_drive_encoders_reset()){
                    b = 3;
                    state = 1;
                }
                return true;
            }

            run_using_encoders();
            set_drive_power(speed, speed);
            b = speed;

            if (have_drive_encoders_reached (encoderCount, encoderCount)) {
                reset_drive_encoders ();
                b = 4;
                set_drive_power (0.0f, 0.0f);
                state = 2;
            }

            telemetry.addData("17", "State: " + state);
            telemetry.addData("0000", "b: " + b);

            return true;
        }
    }





    private class MoveShoulder {
        int state;
        int count;

        MoveShoulder() {
            state = -1;
            count = 0;
            shoulderStartTime = "0";
        }

        void reset() {
            shoulderStartTime = "0";
            state = -1;
            count = 0;
        }

        boolean action(float armSpeed, double cycleCount) {
            shoulderStartTime = "" + count;
            if (state == 1) {
                return false;
            }
            if (state == -1) {
                count = 0;
                state = 0;
                return true;
            }
            if (state == 0) {
                if (count > cycleCount) {
                    set_second_arm_power(0);
                    state = 1;
                } else {
                    set_second_arm_power(0.2);
                }
                count++;
                return true;
            }
            return true;
        }
    }
   /* private class AlignSwivle {
        int state;
        AlignSwivle() {
            state = -1;
        }
        void reset()
        {
            state = -1;
        }
        boolean action(float speed){
            if (state == 1){
                return false;
            }

            // if the right sensor reads white then it will rotate
            if ( firstRGB.red() >= 3) {
                set_drive_power(0, -speed);
            }
            // if the right sensor read grey then it will back up
            else if (firstRGB.red() == 0) {
                set_drive_power(-speed, -speed);
               // if both are grey then stop
                if (secondRGB.red() == 0){
                    set_drive_power (0,0);
                }
            }
            //state = 1;
            return true;
        }
    }*/


    private class ODSReverse {
        int state;
        int pass;
        long startTime;
        double currentValue;

        ODSReverse(){
            state = -1;
            pass = -1;
        }
        void reset() {
            state = -1;
            pass = -1;
        }
        boolean action (float speed) {
            speed = -speed;

            if (state == 1) {
                return false;
            }

            if (state == 13) {
                return Stop();
            }

            currentValue = (sensorODS.getLightDetected() * 100);

            if (state == -1) {
                reset_drive_encoders();
                state = 3;
                return true;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }

            if (state == 0) {
                run_using_encoders();
                set_drive_power(speed, speed);
            }

            if (currentValue > grey && pass == -1){
                pass = 0;
            } else if (currentValue <grey && pass == 0){
                pass = 1;
            }

            if (((pass == 1 || !ftcConfig.param.colorIsRed) && currentValue > grey)) {
                state = 2;
                set_drive_power(0.0f, 0.0f);
                reset_drive_encoders();
            } else if (System.currentTimeMillis() > (startTime + 10 * 1000)) {
                state = 13;
            }

            return true;
        }
    }

    private class ColorReverse {
        int state;
        long startTime;
        double currentGreen1;
        double currentGreen2;
        double currentRed1;
        double currentRed2;
        double maxGreen;
        double maxRed;

        ColorReverse(){
            state = -1;
        }

        void reset() {
            state = -1;
            firstRGB.enableLed(false);
            firstRGB.enableLed(false);
            telemetry.addData("Location", state);
            maxGreen = 0;
            maxRed = 0;
        }

        boolean action (float speed) {
            speed = -speed;
            telemetry.addData("Location", state);
            telemetry.addData("Max Green Seen", maxGreen);
            telemetry.addData("Max Red Seen", maxRed);

            firstRGB.enableLed(true);

            if (state == 1) {
                firstRGB.enableLed(false);
                return false;
            }

            currentGreen1 = firstRGB.green();
            currentRed1 = firstRGB.red();

            if (currentGreen1 > maxGreen) {
                maxGreen = currentGreen1;
            }
            if (currentGreen2 > maxGreen) {
                maxGreen = currentGreen2;
            }
            if (currentRed1 > maxRed) {
                maxRed = currentRed1;
            }
            if (currentRed2 > maxRed) {
                maxRed = currentRed2;
            }

            if (state == 13) {
                telemetry.addData("Bad", "stop in color reverse");
                return Stop();
            }

            if (state == -1) {
                reset_drive_encoders();
                state = 3;
                return true;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }

            if (firstRGB.green() > 250 ) {
                climber_dropper_mid();
                set_drive_power(0.0, 0.0);
            } else if (state == 0) {
                climber_dropper_in();
                run_using_encoders();
                set_drive_power(speed, speed);
            }

            if ((currentGreen1 >= 2 && currentGreen1 < 250)|| (currentGreen2 >= 2 && currentGreen2 < 250)) {
                state = 2;
                set_drive_power(0.0f, 0.0f);
                reset_drive_encoders();
            } else if (System.currentTimeMillis() > (startTime + 10 * 1000)) {
                state = 13;
            }

            return true;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //TURNS

    private class GyroTurn {
        int state;
        long startTime;

        GyroTurn() {
            state = -1;
            // run_using_encoders();
        }

        void reset() {
            state = -1;
            if(sensorGyro != null){
                sensorGyro.resetZAxisIntegrator();
            }
        }

        boolean action(float speed, int desiredHeading) {
            speed = -speed; //robot is backwards
            stop();

            if (state == 1){
                return false;
            }

            xVal = sensorGyro.rawX();
            yVal = sensorGyro.rawY();
            zVal = sensorGyro.rawZ();

            heading = sensorGyro.getHeading();

            if (state == -1){
                reset_drive_encoders();
                sensorGyro.resetZAxisIntegrator();
                state = 3;
                return true;
            }

            if (state == 3) {
                if (have_drive_encoders_reset() && (System.currentTimeMillis() > startTime + 2 * 1000) && heading == 0){
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }

            if (state == 0) {
                run_using_encoders();
                if (!ftcConfig.param.colorIsRed) {
                    set_drive_power(-speed, speed);
                } else {
                    set_drive_power(speed, -speed);
                }

                if (ftcConfig.param.colorIsRed) {
                    desiredHeading = 360 - desiredHeading;
                }

                if (Math.abs(heading - desiredHeading) < 5 || System.currentTimeMillis() > (startTime + 13 * 1000)) {
                    state = 2;
                    sensorGyro.resetZAxisIntegrator();
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                }
            }

            telemetry.addData("Desired Heading", desiredHeading);
            telemetry.addData("Color", ftcConfig.param.colorIsRed);

            return true;
        }
    }

    private class GyroTurnCompass {
        int state;
        long startTime;
        float direction; //goal direction
        boolean done;
        float buffer;
        float difference;
        boolean turnCW; // turn clockwise
        float stopping;


        GyroTurnCompass() {
            state = -1;
        }

        void reset() {
            state = -1;
            done = false;
            stopping = 0;
        }

        boolean action(float speed, int directionRed, int directionBlue) {
            speed = -speed; //robot is backwards

            if (!ftcConfig.param.colorIsRed){ // if blue
                direction = directionBlue;
            } else if (ftcConfig.param.colorIsRed){ // if red
                direction = directionRed;
            }

            telemetry.addData("Gyro Compass Stop", stopping);

            if (state == 1){
                return false;
            }

            xVal = sensorGyro.rawX();
            yVal = sensorGyro.rawY();
            zVal = sensorGyro.rawZ();
            heading = sensorGyro.getHeading();

            buffer = 5;

            //TODO - ONLY reset drive encoders in the INIT?

            if (state == -1){
                reset_drive_encoders();
                done = false;
                state = 3;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()){
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }


            if (state == 0) {
                run_using_encoders();
                if (direction < 5 || direction > 355) { // case for if we want to turn to 0
                    if (heading < buffer || heading > 360 - buffer) {
                        done = true;
                        stopping = heading;
                    }
                }
                else {
                    if (Math.abs(heading - direction) < buffer) {
                        done = true;
                        stopping = heading;
                    }
                }


                if (!done){
                    difference = heading - direction;
                    if (difference > 0){ // to go shortest distance
                        if (difference > 180){
                            turnCW = true; // 270 to 0
                        }else{
                            turnCW = false; // 90 to 0
                        }
                    }
                    else {
                        if (difference < -180){
                            turnCW = false; // turns counterclockwise cuz 30 - 270 = -240 so will turn back
                        }
                        else{
                            turnCW = true;
                        }
                    }
                }
                //set_drive_power(speed, -speed); red
                if (done){
                    set_drive_power(0,0);
                    state = 1;
                } else {
                    if (turnCW){
                        set_drive_power(-speed, speed);
                    }
                    else {
                        set_drive_power(speed, -speed);
                    }
                }
            }



            telemetry.addData("Desired Heading", direction);
            telemetry.addData("Color", ftcConfig.param.colorIsRed);

            return true;
        }
    }

    private class ODSTurn {
        int state;
        long startTime;
        double currentValue;

        ODSTurn(){
            state = -1;
        }
        void reset() {
            state = -1;
        }
        boolean action (float speedFast, float speedSlow) {
            speedFast = -speedFast;
            speedSlow = -speedSlow;

            if (state == 1) {
                return false;
            }
            if (state == 13) {
                return Stop();
            }

            currentValue = (sensorODS.getLightDetected() * 100);

            if (state == -1) {
                reset_drive_encoders();
                state = 3;
                return true;
            }
            if (state == 3) {
                if (have_drive_encoders_reset()) {
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 2) {
                if (have_drive_encoders_reset()) {
                    state = 1;
                }
                return true;
            }
            if (state == 0) {
                run_using_encoders();
                if (!ftcConfig.param.colorIsRed) {
                    set_back_power(-speedFast, speedSlow);
                } else {
                    set_back_power(speedSlow, -speedFast);
                }


            }
            if (currentValue > white) {
                state = 2;
                reset_drive_encoders();
                set_back_power(0.0f, 0.0f);
            } else if (System.currentTimeMillis() > (startTime + 10 * 1000)) {
                state = 13;
            }

            telemetry.addData("Desired Heading ODS", currentValue);
            return true;
        }
    }

    private class Turn {
        int state;
        long startTime;

        Turn() {
            state = -1;
            run_using_encoders();
        }

        void reset() {
            state = -1;
        }

        boolean action(float speed, int sec) {
            speed = -speed; //robot is backwards

            if (state == 1) {
                return false;
            }

            if (state == -1){
                reset_drive_encoders();
                state = -2;
                return true;
            }

            if (state == -2) {
                if (have_drive_encoders_reset()){
                    state = 0;
                    startTime = System.currentTimeMillis();
                }
                return true;
            }

            if (state == 0) {
                run_using_encoders();

                if (beaconPosition.equals("left")) {
                    set_drive_power(0, -speed);
                } else if (beaconPosition.equals("right")) {
                    set_drive_power(-speed, 0);
                }

                if (System.currentTimeMillis() > startTime + (sec * 1000)) {
                    reset_drive_encoders ();
                    set_drive_power (0.0f, 0.0f);
                    state = 2;
                    return true;
                }
            }

            if (state == 2) {
                if(have_drive_encoders_reset()) {
                    state = 1;
                }
            }

            return true;
        }
    }





///////////////////////////////////////////////////////////////////////////////////////////////////
// not sure needed


    //read beacon
    //move arm
    //move forward to press
}



///////////////////////////////////////////////////////////////////////////////////////////////////
//OLD ELSE IF CODE

            /*else if (alignSwivle.action(0.3f)){
                step = "align swivle";
            } else if (readBeacon1.action()) {
                step = "read beacon";
            } /*else if (driveBackForClimbers.action(-0.3f, 1)) {
                step = "back up for climbers";
            } else if (pauseBeforeClimbers.action(2)){
                step = "pause before climbers";
            }else if (dropClimbers.action(true)) {
                step = "drop climbers";
            } else if (pauseToDropClimbers.action(3)) {
                step = "last pause";
            } else if (dropClimbersIn.action(false)) {
                step = "last drive back!";
            } */
           /* else if (dropClimbersComplete.action()){
                // step = "drop climbers complete";
            }
            /*
            else if (color.equals("blue")){
                if (alignGyroTurn1Blue.action (0.3f, 15)){
                    step = "15 degree to align 1 blue";
                } else if (driveToAlignBlue.action(-0.3f, 12)){
                    step = "drive to align blue";
                } else if (alignGyroTurn2Blue.action (-0.3f, 360-15)){
                    step = " 15 degree to align 2 blue";
            }} else if (color.equals("red")){
                if (alignGyroTurn1Red.action (-0.3f, 360-15)){
                    step = "15 degree to align 1 red";
                }else if (driveToAlignRed.action(-0.3f, 6)){
                    step = "drive to align blue";
                } else if (alignGyroTurn2Red.action (0.3f, 15)){
                    step = "15 degree to align 2 red";
                }
            } else if (dropClimbersForButtonPusher.action (true)){
                step = "climber dropper button pusher";
            } else if (driveToPress.action(0.2f, 10)){
                step = "drive to press button";
            }

            /*
            } else if (driveStraightToBeaconZone.action(0.3f, 87)) { //driveStraight automatically makes go backward
                step = "drive straight 1";
            } else if (gyroTurnToPushAwayDebris.action(-0.3f, 320)){
                step = "gyro to push away debris";
            } else if (driveToPushAwayDebris.action(0.3f, 20)){
                step = "drive to push away debris";
            } else if(odsReverseBeacon.action(-0.3f)){
                step = "ods reverse";
            } /*
            else if (toBeacon.action(0.2f, 6)) {
                step = "to Beacon";
            } */
                /*

            else if (driveToAlignAfterODS.action(0.3f, getAlignDistance())) { //OUT
                step = "drive forward a little bit to align";
            }else if (pauseAfterODSAlign.action(1)){
                step = "pause after drive 1";
            }else if (gyroTurnToFaceBeacon.action(0.3f, 84)){ //OUT
                step = "gyro to face beacon";
            } else if (pauseAfterFaceBeaconTurn.action(1)) {
                step = "pause after turn 2";
            }else if ( driveToReadBeacon.action(0.3f, 18)){ //OUT
                step = "drive forward to read beacon";
            } else if (pauseToReadBeacon.action(1)){
                step = "pause to read beacon";
            }else if (readBeacon1.action()) {
                step = "read beacon";
            } else if (driveBackForClimbers.action(-0.3f, 1)) {
                step = "back up for climbers";
            } else if (pauseBeforeClimbers.action(2)){
                step = "pause before climbers";
            }else if (dropClimbers.action(true)) {
                step = "drop climbers";
            } else if (pauseToDropClimbers.action(3)) {
                step = "last pause";
            } else if (dropClimbersIn.action(false)) {
                step = "last drive back!";
            }else if (driveBackToMoveArm.action(-0.3f, 8)){
                step = "back up to move arm";
            } else if (moveArmForBeacon.action(beaconPosition)){
                step = "move arm for beacon";
            } else if (!color.equals("unknown") && driveToPressButton.action(0.25f, 14)) {
                step = "press the button!!!";
            } /*else if (turnToHitBeacon.action(-1.0f, 3)) {
                step = "turn to hit beacon";
            }

*/

