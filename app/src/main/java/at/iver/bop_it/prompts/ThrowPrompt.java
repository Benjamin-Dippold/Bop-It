package at.iver.bop_it.prompts;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import at.iver.bop_it.R;

public class ThrowPrompt extends AbstractPrompt implements SensorEventListener {

    private static final float THROW_THRESHOLD = 15.0f; // Threshold for detecting a throw
    private static final int THROW_DELAY_MS = 1000; // Delay after detecting a throw

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isThrowDetected;
    private Handler handler;

    public ThrowPrompt() {
        super(R.layout.throw_prompt);
        isThrowDetected = false;
        handler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireContext().getSystemService(requireContext().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate the total acceleration magnitude
            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

            // Check if the acceleration is above the throw threshold
            if (acceleration >= THROW_THRESHOLD) {
                // If a throw is detected, mark it and trigger the callback after the delay
                if (!isThrowDetected) {
                    isThrowDetected = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callBackVictorious();
                        }
                    }, THROW_DELAY_MS);
                }
            } else {
                // Reset the throw detection if the acceleration drops below the threshold
                isThrowDetected = false;
            }
        }
    }

    @Override
    protected void callBackVictorious() {
        super.callBackVictorious();
    }
}
