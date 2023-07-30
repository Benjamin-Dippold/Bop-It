package at.iver.bop_it.prompts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;

import at.iver.bop_it.R;

public class BrightnessUpPrompt extends AbstractPrompt {
    private static final String TAG = "BrightnessDownPrompt";
    private ContentObserver observer;
    private ContentResolver resolver;
    private int minSeenBrightness;

    public BrightnessUpPrompt() {
        super(R.layout.brightnessup_prompt, R.raw.do_brighness_up, R.raw.brightness_up_normal);
    }

    @Override
    public void onResume() {
        super.onResume();

        resolver = requireContext().getContentResolver();
        observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                int brightnessAmount = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 0);
                Log.i(TAG, "Brightness: " + brightnessAmount);

                if (brightnessAmount > minSeenBrightness) {
                    callBackVictorious();
                } else if (brightnessAmount < minSeenBrightness) {
                    minSeenBrightness = brightnessAmount;
                }
            }
        };
        resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, observer);

        minSeenBrightness = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    @Override
    public void onPause() {
        resolver.unregisterContentObserver(observer);
        super.onPause();
    }
}
