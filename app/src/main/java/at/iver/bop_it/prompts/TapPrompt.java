/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;

public class TapPrompt extends AbstractPrompt {

    public TapPrompt() {
        super(R.layout.tap_promt);
    }

    @Override
    protected void playSound() {
        final MediaPlayer player = MediaPlayer.create(getContext(), R.raw.single_tap);
        player.start();
    }
    @Override
    protected void onSingleTapUp() {
        callBackVictorious();
    }
}
