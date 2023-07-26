/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import at.iver.bop_it.MainActivity;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractPrompt extends Fragment implements SensorEventListener, Serializable {

    private static final String TAG = "AbstractPrompt";
    private static final long maxTimePerPrompt = 10000L;
    private boolean isVictorious = false;

    protected long startTime;
    protected int layout;
    protected int sensorId;
    private SensorManager sensorManager;

    public AbstractPrompt(int layout) {
        this.layout = layout;
        this.sensorId = -1;
    }

    public AbstractPrompt(int layout, int sensorId) {
        this.layout = layout;
        this.sensorId = sensorId;
    }

    private final TimerTask failAfterMaxTime =
            new TimerTask() {
                @Override
                public void run() {
                    Log.e(TAG, "TIMES UP!");
                    callBackFailure();
                }
            };

    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout, null, false);
        view.setOnTouchListener(
                new View.OnTouchListener() {
                    private final GestureDetectorCompat gestureDetector =
                            new GestureDetectorCompat(requireContext(), new GestureListener());

                    private final ScaleGestureDetector scaleGestureDetector =
                            new ScaleGestureDetector(requireContext(), new ScaleGestureListener());

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event)
                                || scaleGestureDetector.onTouchEvent(event);
                    }
                });

        if (sensorId != -1) {
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Log.e(TAG, "Error getting sensor with id: " + sensorId);
            }
        }
        startTime = SystemClock.elapsedRealtime();
        playSound();

        new Timer().schedule(failAfterMaxTime, maxTimePerPrompt);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        failAfterMaxTime.cancel();
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    protected void callBackVictorious() {
        if(isVictorious) return;
        isVictorious = true;
        long endTime = SystemClock.elapsedRealtime();
        try {
            ((MainActivity) getActivity()).promptComplete(endTime - startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void callBackFailure() {
        try {
            ((MainActivity) getActivity()).promptComplete(-2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void playSound();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    protected void onSingleTapUp() {
        Log.v(TAG, "onSingleTapUp");
    }

    protected void onFling() {
        Log.v(TAG, "onFling");
    }

    protected void onDoubleTap() throws IOException {
        Log.v(TAG, "onDoubleTap");
    }

    protected void onLongPress() {
        Log.v(TAG, "onLongPress");
    }

    protected void onScaleBegin(ScaleGestureDetector scaleGestureDetector) {}

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            AbstractPrompt.this.onSingleTapUp();
            return true;
        }

        @Override
        public boolean onFling(
                @NonNull MotionEvent e1,
                @NonNull MotionEvent e2,
                float velocityX,
                float velocityY) {
            AbstractPrompt.this.onFling();
            return true;
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            try {
                AbstractPrompt.this.onDoubleTap();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            AbstractPrompt.this.onLongPress();
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(@NonNull ScaleGestureDetector scaleGestureDetector) {
            AbstractPrompt.this.onScaleBegin(scaleGestureDetector);
            return false;
        }
    }
}
