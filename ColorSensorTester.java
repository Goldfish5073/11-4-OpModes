package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

import com.qualcomm.robotcore.util.Range;
/**
 * Created by house on 11/13/2015.
 */
public class ColorSensorTester extends Hardware {
    private ColorSensor firstRGB;
    private ColorSensor secondRGB;
    private ColorSensor beacon;
    private DeviceInterfaceModule cdim;
    float hsvValuesFirst[] = {0F,0F,0F};
    float hsvValuesSecond[] = {0F,0F,0F};
    float hsvValuesB[] = {0F, 0F, 0F};
    String color1 = "unknown";
    String color2 = "unknown";
    String colorB = "unknown";

    @Override public void init ()
    {
        super.init();


        try {
            secondRGB = hardwareMap.colorSensor.get("mr2");
            secondRGB.setI2cAddress(0x4c);

            secondRGB.enableLed(false);

        } catch (Exception p_exception) {
            m_warning_message("color sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            secondRGB = null;
        }

        try {
            firstRGB = hardwareMap.colorSensor.get("mr");
            firstRGB.setI2cAddress(0x5c);
            firstRGB.enableLed(false);
        } catch (Exception p_exception) {
            m_warning_message("color sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            firstRGB = null;
        }

        try {
            beacon = hardwareMap.colorSensor.get("mr3");
            beacon.enableLed(false);
        } catch (Exception p_exception) {
            m_warning_message("beacon");
            DbgLog.msg(p_exception.getLocalizedMessage());
            beacon = null;
        }

        try {
            cdim = hardwareMap.deviceInterfaceModule.get("sensors");
        } catch (Exception p_exception) {
            m_warning_message("sensors");
            DbgLog.msg(p_exception.getLocalizedMessage());
            cdim = null;
        }


    } // init


    @Override public void start () {
        super.start();
        firstRGB.enableLed(true);
        secondRGB.enableLed(true);
        beacon.enableLed(false);

    } // start




    public void color()
    {
        firstRGB.enableLed(true);
        secondRGB.enableLed(true);
        beacon.enableLed(false);

        Color.RGBToHSV(beacon.red() * 8, beacon.green() * 8, beacon.blue() * 8, hsvValuesB);
        telemetry.addData("B Alpha", beacon.alpha());
        telemetry.addData("B Red  ", beacon.red());
        telemetry.addData("B Green", beacon.green());
        telemetry.addData("B Blue ", beacon.blue());
        telemetry.addData("B Hue1 ", hsvValuesB[0]);
        telemetry.addData("B Hue2 ", hsvValuesB[1]);
        telemetry.addData("B Hue3 ", hsvValuesB[2]);

        Color.RGBToHSV(firstRGB.red() * 8, firstRGB.green() * 8, firstRGB.blue() * 8, hsvValuesFirst);
        telemetry.addData("Clear1", firstRGB.alpha());
        telemetry.addData("Red1  ", firstRGB.red());
        telemetry.addData("Green1", firstRGB.green());
        telemetry.addData("Blue1 ", firstRGB.blue());
        telemetry.addData("Hue1", hsvValuesFirst[0]);

        Color.RGBToHSV(secondRGB.red() * 8, secondRGB.green() * 8, secondRGB.blue() * 8, hsvValuesSecond);
        telemetry.addData("Clear2", secondRGB.alpha());
        telemetry.addData("Red2  ", secondRGB.red());
        telemetry.addData("Green2", secondRGB.green());
        telemetry.addData("Blue2 ", secondRGB.blue());
        telemetry.addData("Hue2", hsvValuesSecond[0]);
    }


    @Override public void loop () {
        telemetry.clearData();
        color();
        if (hsvValuesFirst[0] > 50) {
            color1 = "blue";
        } else if (firstRGB.red() > 0) {
            color1 = "red";
        } else {
            color1 = "unknown";
        }
        telemetry.addData("color1", color1);
        update_telemetry(); // Update common telemetry

        if (hsvValuesSecond[0] > 50) {
            color2 = "blue";
        } else if (secondRGB.red() > 0) {
            color2 = "red";
        } else {
            color2 = "unknown";
        }
        telemetry.addData("color2", color2);
        update_telemetry(); // Update common telemetry
    }

    public ColorSensorTester() {
    }
}
