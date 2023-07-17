/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;

public class DoubleTapPrompt extends AbstractPrompt {
    public DoubleTapPrompt() {
        super(R.layout.double_tap_prompt);
    }

    @Override
    protected void playSound() {
        final MediaPlayer player = MediaPlayer.create(getContext(), R.raw.double_tap);
        player.start();
    }

    @Override
    protected void onDoubleTap() {
        callBackVictorious();
    }
}
