/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import java.io.IOException;

import at.iver.bop_it.R;

public class ShakePrompt extends AbstractPrompt {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;

    public ShakePrompt() {
        super(R.layout.shake_promt, Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            try {
                callBackVictorious();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
