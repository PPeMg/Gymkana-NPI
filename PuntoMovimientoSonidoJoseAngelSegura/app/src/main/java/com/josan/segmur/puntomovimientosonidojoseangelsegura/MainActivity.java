/*
 *  Copyright (C) 2014, 2015 - Jose Angel Segura Muros <shaljas@correo.ugr.es>, Jose Delgado Dolset <jdeldo@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Actividad principal para la aplicación que acepta un patron de movimiento y lee un codigo QR
 * @author: Jose Angel Segura Muros, Jose Delgado Dolset
 * @version: 1.0
 * @date: 25/01/2015
 */


package com.josan.segmur.puntomovimientosonidojoseangelsegura;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.io.IOException;


public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private MediaPlayer mp;
    boolean reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mp = new MediaPlayer();

        reset = false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xVal = event.values[0];
            float yVal = event.values[1];
            float zVal = event.values[2];
            if(xVal > -1.0 && xVal < 1.0){
                if(yVal > -1.0 && yVal < 1.0){
                    if(zVal < -9.6 && zVal > -10.0){
                        if(reset){
                            reset = false;
                            //pitamos

                            try {
                                AssetFileDescriptor afd;
                                afd = getAssets().openFd("zelda.mp3");
                                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                mp.prepare();
                                mp.start();

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if(xVal > -1.0 && xVal < 1.0) {
                if (yVal > -1.0 && yVal < 1.0) {
                    if (zVal > 9.6 && zVal < 10.0) {
                        mp.reset();
                        reset = true;
                    }
                }
            }
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }


}
