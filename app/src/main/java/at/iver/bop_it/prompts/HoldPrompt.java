/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;

public class HoldPrompt extends AbstractPrompt {

    public HoldPrompt() {
        super(R.layout.hold_prompt);
    }

    @Override
    protected void playSound() {
        final MediaPlayer player = MediaPlayer.create(getContext(), R.raw.hold);
        player.start();
    }
    @Override
    protected void onLongPress() {
        callBackVictorious();
    }
}
