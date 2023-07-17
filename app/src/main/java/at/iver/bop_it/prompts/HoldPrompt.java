/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;
import at.iver.bop_it.sound.SoundProvider;

public class HoldPrompt extends AbstractPrompt {

    public HoldPrompt() {
        super(R.layout.hold_prompt);
    }

    @Override
    protected void playSound() {
        SoundProvider soundProvider = SoundProvider.getInstance();
        soundProvider.playHold(getContext());
    }
    @Override
    protected void onLongPress() {
        callBackVictorious();
    }
}
