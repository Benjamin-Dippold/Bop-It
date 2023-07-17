package at.iver.bop_it.prompts;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import at.iver.bop_it.R;

public class VolumeUpPrompt extends AbstractPrompt {

    public VolumeUpPrompt() {
        super(R.layout.volumeup_prompt);
    }

    @Override
    protected void playSound() {
        final MediaPlayer player = MediaPlayer.create(getContext(), R.raw.volume_up);
        player.start();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onVolumeUp();
                    return true;
                }
                return false;
            }
        });
    }

    private void onVolumeUp() {
        callBackVictorious();
    }
}