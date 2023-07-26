/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.view.ScaleGestureDetector;
import at.iver.bop_it.R;

public class PinchPrompt extends AbstractPrompt {
    private static final float SPAN_THRESHOLD = 500f;
    private float startingSpan = 0;

    public PinchPrompt() {
        super(R.layout.pinch_prompt, R.raw.do_pinch, R.raw.pinch_normal);
    }

    @Override
    protected void onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        float currentSpan = scaleGestureDetector.getCurrentSpan();
        if (startingSpan == 0 || currentSpan > startingSpan) {
            startingSpan = currentSpan;
        } else {
            if (currentSpan < startingSpan - SPAN_THRESHOLD) {
                callBackVictorious();
            }
        }
    }
}
