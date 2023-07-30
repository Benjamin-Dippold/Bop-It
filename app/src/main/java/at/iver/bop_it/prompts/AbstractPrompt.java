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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import at.iver.bop_it.MainActivity;
import at.iver.bop_it.sound.SoundProvider;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import at.iver.bop_it.R;

public abstract class AbstractPrompt extends Fragment implements SensorEventListener, Serializable {

    private static final String TAG = "AbstractPrompt";
    public static final long maxTimePerPrompt = 5000L;
    private boolean isVictorious = false;

    protected long startTime;
    protected int layout;
    protected int sensorId;
    private SensorManager sensorManager;
    private boolean isSimon;

    protected int simonVoiceClip;
    protected int otherVoiceClip;

    public AbstractPrompt(int layout) {
        this(layout, -1);
    }

    public AbstractPrompt(int layout, int sensorId) {
        this(layout, sensorId, -1, -1);
    }

    public AbstractPrompt(int layout, int simonVoiceClip, int otherVoiceClip) {
        this(layout, -1, simonVoiceClip, otherVoiceClip);
    }

    public AbstractPrompt(int layout, int sensorId, int simonVoiceClip, int otherVoiceClip) {
        this.layout = layout;
        this.sensorId = sensorId;
        this.simonVoiceClip = simonVoiceClip;
        this.otherVoiceClip = otherVoiceClip;
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startTime = SystemClock.elapsedRealtime();

        new Timer().schedule(failAfterMaxTime, maxTimePerPrompt);

        isSimon = getArguments().getBoolean("isSimon");

        playSound();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        failAfterMaxTime.cancel();
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    protected void callBackVictorious() {
        if (!isSimon) {
            isSimon = true;
            callBackFailure();
            return;
        }
        if (isVictorious) return;
        isVictorious = true;
        long endTime = SystemClock.elapsedRealtime();
        try {
            ((MainActivity) getActivity()).promptComplete(endTime - startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void callBackFailure() {
        if (!isSimon) {
            isSimon = true;
            callBackVictorious();
            return;
        }
        try {
            ((MainActivity) getActivity()).promptComplete(-2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void playSound() {
        if (isSimon) {
            SoundProvider.getInstance().playVoiceLine(simonVoiceClip, getContext());
        } else {
            SoundProvider.getInstance().playVoiceLine(otherVoiceClip, getContext());
        }
    }

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
    @Override
    public void onResume() {
        super.onResume();
        Log.d("AbstractPrompt", "onResume");

        View view = getView();
        if (view != null) {
            ImageView imageView = view.findViewById(R.id.imageView);
            Animation anim = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse);
            imageView.startAnimation(anim);

            TextView textView = view.findViewById(R.id.textView);

            ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(),
                    Color.parseColor("#8B4000") , Color.BLUE, Color.RED, Color.DKGRAY, Color.parseColor("#800080"));
            colorAnim.setDuration(3000);
            colorAnim.setRepeatCount(ValueAnimator.INFINITE);
            colorAnim.addUpdateListener(animation -> { int color = (int) animation.getAnimatedValue();
                textView.setTextColor(color);
            });
            colorAnim.start();

        }
    }
}
