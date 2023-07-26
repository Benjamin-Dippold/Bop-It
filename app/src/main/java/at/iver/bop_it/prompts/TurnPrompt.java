/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import at.iver.bop_it.R;

public class TurnPrompt extends AbstractPrompt {

    private static final float THRESHOLD = 10.0f; // Adjust this threshold as needed

    private float lastX, lastY, lastZ;
    private boolean isInitialized = false;
    private boolean isSideways = false;

    public TurnPrompt() {
        super(R.layout.turn_prompt, Sensor.TYPE_ACCELEROMETER, R.raw.do_turn_it, R.raw.turn_normal);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Calculate the difference between the current and last accelerometer values
        float deltaX = Math.abs(x - lastX);
        float deltaY = Math.abs(y - lastY);
        float deltaZ = Math.abs(z - lastZ);

        // Update the last accelerometer values
        lastX = x;
        lastY = y;
        lastZ = z;

        if (!isInitialized) {
            isInitialized = true;
        } else {
            // Check if the phone has been turned sideways
            if (!isSideways && (deltaX > THRESHOLD || deltaY > THRESHOLD)) {
                isSideways = true;
                callBackVictorious();
            } else if (isSideways && deltaX < THRESHOLD && deltaY < THRESHOLD) {
                isSideways = false;
                callBackVictorious();
            }
        }
    }
}
