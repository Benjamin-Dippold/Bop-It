/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import at.iver.bop_it.MainActivity;

public abstract class AbstractPrompt extends Fragment {

    protected long startTime;
    protected int layout;

    public AbstractPrompt(int layout) {
        this.layout = layout;
    }

    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout, null, false);
        view.setOnTouchListener(
                new View.OnTouchListener() {
                    private GestureDetectorCompat gestureDetector =
                            new GestureDetectorCompat(requireContext(), new GestureListener());

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });
        startTime = SystemClock.elapsedRealtime();
        return view;
    }

    protected void callBackVictorious() {
        long endTime = SystemClock.elapsedRealtime();
        ((MainActivity) getActivity()).promptComplete(endTime - startTime);
    }

    protected void onSingleTapUp() {
        Log.v("AbstractPrompt", "onSingleTapUp");
    }

    protected void onFling() {
        Log.v("AbstractPrompt", "onFling");
    }

    protected void onDoubleTap() {
        Log.v("AbstractPrompt", "onDoubleTap");
    }

    protected void onLongPress() {
        Log.v("AbstractPrompt", "onLongPress");
    }

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
            AbstractPrompt.this.onDoubleTap();
            return true;
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            AbstractPrompt.this.onLongPress();
        }
    }
}
