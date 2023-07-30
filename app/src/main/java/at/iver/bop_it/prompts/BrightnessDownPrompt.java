package at.iver.bop_it.prompts;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import at.iver.bop_it.R;

public class BrightnessDownPrompt extends AbstractPrompt {
    private static final String TAG = "BrightnessDownPrompt";
    private ContentObserver observer;
    private ContentResolver resolver;
    private int maxSeenBrightness;

    public BrightnessDownPrompt() {
        super(R.layout.brightnessdown_prompt, R.raw.do_brighness_down, R.raw.brightness_down_normal);
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

                if (brightnessAmount < maxSeenBrightness) {
                    callBackVictorious();
                } else if (brightnessAmount > maxSeenBrightness) {
                    maxSeenBrightness = brightnessAmount;
                }
            }
        };
        resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), false, observer);

        maxSeenBrightness = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    @Override
    public void onPause() {
        resolver.unregisterContentObserver(observer);
        super.onPause();
    }
}
