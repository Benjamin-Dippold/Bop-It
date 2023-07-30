package at.iver.bop_it.prompts;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;

import at.iver.bop_it.R;

public class BrightnessUpPrompt extends AbstractPrompt {
    private boolean isPromptCompleted;
    private static final String TAG = "BrightnessUpPrompt";
    private HandlerThread handlerThread;
    private Handler handler;

    public BrightnessUpPrompt() {
        super(R.layout.brightnessup_prompt, R.raw.do_brighness_up, R.raw.brightness_up_normal);
        isPromptCompleted = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BrightnessUpPrompt", "onResume");

            setBrightnessToDefault();
            startBrightnessCheck();

    }
    @Override
    public void onPause() {
        super.onPause();
        stopBrightnessCheck();
    }
    private void startBrightnessCheck() {
        handlerThread = new HandlerThread("BrightnessCheckThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(brightnessCheckRunnable);
    }

    private void stopBrightnessCheck() {
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
        }
    }

    private Runnable brightnessCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkBrightness();
            if (!isPromptCompleted()) {
                handler.postDelayed(this, 10); // Check every 1 second
            }
        }
    };



    private void checkBrightness() {
        Context context = requireContext().getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();
        int currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0);
        int startingBrightness = (int) (255*0.5);

        if (currentBrightness > startingBrightness) {
            isPromptCompleted = true;
            callBackVictorious();
        }
    }

    private void setBrightnessToDefault() {
        Log.d("BrightnessUpPrompt", "setBrightnessToDefault");
        Context context = requireContext().getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();
        int maxBrightness = 255;
        int newBrightness = (int) (maxBrightness * 0.5);
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, newBrightness);
    }

    private boolean isPromptCompleted() {
        return isPromptCompleted;
    }


}
