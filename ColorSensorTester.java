package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.ColorSensor;
/**
 * Created by house on 11/13/2015.
 */
public class ColorSensorTester extends Hardware {
    private ColorSensor sensorRGB;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;
    String color = "unknown";

    @Override public void start ()
    {
        super.start();
        try {
            sensorRGB = hardwareMap.colorSensor.get("mr");
        } catch (Exception p_exception) {
            m_warning_message("color sensor");
            DbgLog.msg(p_exception.getLocalizedMessage());
            sensorRGB = null;
        }
    } // start

    public void color()
    {
        sensorRGB.enableLed(false);
        Color.RGBToHSV(sensorRGB.red() * 8, sensorRGB.green() * 8, sensorRGB.blue() * 8, hsvValues);
        telemetry.addData("Clear", sensorRGB.alpha());
        telemetry.addData("Red  ", sensorRGB.red());
        telemetry.addData("Green", sensorRGB.green());
        telemetry.addData("Blue ", sensorRGB.blue());
        telemetry.addData("Hue", hsvValues[0]);
    }


    @Override public void loop () {
        telemetry.clearData();
        color();
        if (hsvValues[0] > 50)
        {
            color = "blue";
        } else if (sensorRGB.red() > 0)
        {
            color = "red";
        } else
        {
            color = "unknown";
        }
        telemetry.addData("color", color);
        update_telemetry(); // Update common telemetry
    }

    public ColorSensorTester() {
    }
}
