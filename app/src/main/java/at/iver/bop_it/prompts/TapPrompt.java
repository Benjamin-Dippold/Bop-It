/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.prompts;

import android.media.MediaPlayer;

import at.iver.bop_it.R;
import at.iver.bop_it.sound.SoundProvider;

public class TapPrompt extends AbstractPrompt {

    public TapPrompt() {
        super(R.layout.tap_promt);
    }

    @Override
    protected void playSound() {
        SoundProvider soundProvider = SoundProvider.getInstance();
        soundProvider.playSingletap(getContext());
    }
    @Override
    protected void onSingleTapUp() {
        callBackVictorious();
    }
}
