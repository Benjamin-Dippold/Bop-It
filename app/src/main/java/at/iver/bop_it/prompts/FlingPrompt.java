/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;
import at.iver.bop_it.sound.SoundProvider;

public class FlingPrompt extends AbstractPrompt {

    public FlingPrompt() {
        super(R.layout.fling_prompt);
    }

    @Override
    protected void playSound() {
        SoundProvider soundProvider = SoundProvider.getInstance();
        soundProvider.playFling(getContext());
    }
    @Override
    protected void onFling() {
        callBackVictorious();
    }
}
