/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class TextViewOutlined extends androidx.appcompat.widget.AppCompatTextView {

    private static final int DEFAULT_STROKE_WIDTH = 0;
    private int strokeColor;
    private float strokeWidth;

    public TextViewOutlined(Context context) {
        this(context, null, R.attr.textViewOutlinedStyle);
    }

    public TextViewOutlined(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.textViewOutlinedStyle);
    }

    public TextViewOutlined(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray a =
                    context.obtainStyledAttributes(
                            attrs,
                            R.styleable.TextViewOutlined,
                            R.attr.textViewOutlinedStyle,
                            R.style.TextViewWithShadow);
            strokeColor =
                    a.getColor(R.styleable.TextViewOutlined_textStrokeColor, getCurrentTextColor());
            strokeWidth =
                    a.getDimensionPixelSize(
                            R.styleable.TextViewOutlined_textStrokeWidth, DEFAULT_STROKE_WIDTH);

            a.recycle();
        } else {
            strokeColor = getCurrentTextColor();
            strokeWidth = DEFAULT_STROKE_WIDTH;
        }
    }

    // The onDraw uses setTextColor() to swap between Text- and outline color,
    // but setTextColor invalidates the View, causing it to be drawn again
    // resulting in an infinite draw loop. Disabling the ability to be invalidated,
    // while drawing fixes the issue.

    private boolean isInvalidateAllowed = true;

    @Override
    public void invalidate() {
        if (!isInvalidateAllowed) return;
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (strokeWidth > 0) {
            isInvalidateAllowed = false;
            Paint p = getPaint();
            // save the text color
            int currentTextColor = getCurrentTextColor();

            // set paint to stroke mode and specify
            // stroke color and width
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            // draw text stroke
            super.onDraw(canvas);

            // revert the color back to the one
            // initially specified
            setTextColor(currentTextColor);
            // set paint to fill mode
            p.setStyle(Paint.Style.FILL);
            // draw the fill part of text

            isInvalidateAllowed = true;
            super.onDraw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }
}
